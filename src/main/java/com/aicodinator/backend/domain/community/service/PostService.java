package com.aicodinator.backend.domain.community.service;

import com.aicodinator.backend.domain.infrastructure.s3.domain.constant.UploadType;
import com.aicodinator.backend.domain.community.domain.dto.request.PostEditRequest;
import com.aicodinator.backend.domain.community.domain.dto.request.PostRequest;
import com.aicodinator.backend.domain.community.domain.dto.response.MainPageResponse;
import com.aicodinator.backend.domain.community.domain.dto.response.PostDetailResponse;
import com.aicodinator.backend.domain.community.domain.dto.response.PostListResponse;
import com.aicodinator.backend.domain.community.domain.entity.Board;
import com.aicodinator.backend.domain.community.domain.entity.Post;
import com.aicodinator.backend.domain.community.domain.entity.PostFile;
import com.aicodinator.backend.domain.community.domain.entity.PostLike;
import com.aicodinator.backend.domain.community.mapper.PostMapper;
import com.aicodinator.backend.domain.community.repository.PostFileRepository;
import com.aicodinator.backend.domain.community.repository.PostLikeRepository;
import com.aicodinator.backend.domain.community.repository.PostRepository;
import com.aicodinator.backend.domain.infrastructure.s3.service.FileValidator;
import com.aicodinator.backend.domain.infrastructure.s3.service.S3Uploader;
import com.aicodinator.backend.domain.region.domain.entity.Region;
import com.aicodinator.backend.domain.region.service.RegionService;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;
    private final S3Uploader s3Uploader;
    private final PostMapper postMapper;
    private final FileValidator fileValidator;
    private final RegionService regionService;
    private final PostLikeRepository postLikeRepository;
    private final PostFileRepository postFileRepository;

    @Transactional(readOnly = true)
    public Post findById(long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "요청하신 게시글을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId, User user) {
        Post post = postRepository.findByIdWithDetails(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "요청하신 게시글을 찾을 수 없습니다."));
        post.increaseViewCount();

        return postMapper.toPostDetailResponse(post, user);
    }

    @Transactional(readOnly = true)
    public Page<PostListResponse> getPostList(Long boardId, Pageable pageable) {
        Board board = boardService.findById(boardId);
        return postRepository.findByBoard(board, pageable)
            .map(postMapper::toPostListResponse);
    }

    @Transactional(readOnly = true)
    public MainPageResponse getMainPagePosts(Long regionId) {
        Region region = regionService.findById(regionId);

        Pageable limit3 = PageRequest.of(0, 3);
        List<Post> latestPosts = postRepository.findTop3ByRegionOrderByCreatedAtDesc(region, limit3);
        List<Post> popularPosts = postRepository.findTop3PopularPosts(region, limit3);
        List<Post> latestCommentedPosts = postRepository.findTop3LatestCommentedPosts(region, limit3);
        List<Post> mostCommentedPosts = postRepository.findTop3MostCommentedPosts(region, limit3);

        return MainPageResponse.builder()
            .latestPosts(postMapper.toPostListResponseList(latestPosts))
            .popularPosts(postMapper.toPostListResponseList(popularPosts))
            .latestCommentPosts(postMapper.toPostListResponseList(latestCommentedPosts))
            .mostCommentedPosts(postMapper.toPostListResponseList(mostCommentedPosts))
            .build();
    }

    @Transactional
    public PostDetailResponse createPost(PostRequest request, List<MultipartFile> files, User user) {
        fileValidator.validate(files);

        Board board = boardService.findById(request.getBoardId());
        Post post = Post.builder()
            .board(board)
            .title(request.getTitle())
            .content(request.getContent())
            .user(user)
            .region(board.getRegion())
            .build();

        postRepository.save(post);
        uploadFiles(files, post);

        return postMapper.toPostDetailResponse(post, user);
    }

    @Transactional
    public PostDetailResponse updatePost(Long postId, PostEditRequest request, List<MultipartFile> newFiles, User user) {
        Post post = findPostAndValidate(postId, user);

        int currentFileCount = post.getFiles().size();
        int deleteFileCount = request.getDeleteFileIds() != null ? request.getDeleteFileIds().size() : 0;
        fileValidator.validate(newFiles, currentFileCount, deleteFileCount);

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        if (request.getDeleteFileIds() != null && !request.getDeleteFileIds().isEmpty()) {
            List<PostFile> filesToDelete = post.getFiles()
                .stream().filter(file -> request.getDeleteFileIds().contains(file.getId()))
                .toList();

            deleteFiles(filesToDelete, post);
        }

        uploadFiles(newFiles, post);
        return postMapper.toPostDetailResponse(post, user);
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = findPostAndValidate(postId, user);

        deleteFiles(post.getFiles(), post);

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Boolean isLikedPost(Long postId, User user) {
        return postLikeRepository.existsByPostIdAndUser(postId, user);
    }

    @Transactional
    public Boolean likePost(Long postId, User user) {
        Post post = findById(postId);
        if (postLikeRepository.existsByPostIdAndUser(postId, user)) {
            return true;
        }

        postLikeRepository.save(PostLike.builder().post(post).user(user).build());
        return true;
    }

    @Transactional
    public Boolean unlikePost(Long postId, User user) {
        postLikeRepository.deleteByPostIdAndUser(postId, user);
        return false;
    }

    private Post findPostAndValidate(Long postId, User user) {
        Post post = findById(postId);

        if (!user.getId().equals(post.getUser().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "요청하신 게시글에 대한 권한이 없습니다.");
        }

        return post;
    }

    private void uploadFiles(List<MultipartFile> files, Post post) {
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String storedKey = s3Uploader.upload(file, UploadType.COMMUNITY_POST);

                PostFile postFile = PostFile.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedKey(storedKey)
                    .fileSize(file.getSize())
                    .build();

                post.addFile(postFile);
            }
        }
        postFileRepository.saveAll(post.getFiles());
    }

    private void deleteFiles(List<PostFile> files, Post post) {
        if (!files.isEmpty()) {
            for (PostFile file : files) {
                s3Uploader.delete(file.getStoredKey());
            }
        }
        post.getFiles().removeAll(files);
    }
}
