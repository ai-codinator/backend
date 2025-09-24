package com.aicodinator.backend.domain.community.controller;

import com.aicodinator.backend.domain.community.domain.dto.request.PostEditRequest;
import com.aicodinator.backend.domain.community.domain.dto.request.PostRequest;
import com.aicodinator.backend.domain.community.domain.dto.response.MainPageResponse;
import com.aicodinator.backend.domain.community.domain.dto.response.PostDetailResponse;
import com.aicodinator.backend.domain.community.domain.dto.response.PostListResponse;
import com.aicodinator.backend.domain.community.service.PostService;
import com.aicodinator.backend.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private PostService postService;

    /**
     * 메인 페이지 게시글 목록 조회
     */
    @GetMapping("/main/posts")
    public ResponseEntity<MainPageResponse> getMainPagePosts(@RequestParam("regionId") Long regionId) {
        return ResponseEntity.ok(postService.getMainPagePosts(regionId));
    }

    /**
     * 특정 게시판의 게시글 목록 페이징 조회
     */
    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<Page<PostListResponse>> getPostList(@PathVariable Long boardId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(postService.getPostList(boardId, pageable));
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.getPostDetail(postId, user));
    }

    /**
     * 게시글 작성
     */
    @PostMapping(value = "/posts", consumes = {"multipart/form-data"})
    public ResponseEntity<PostDetailResponse> createPost(@RequestPart("request") PostRequest request,
                                                         @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                                         @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request, files, user));
    }

    /**
     * 게시글 수정
     */
    @PatchMapping(value = "/posts/{postId}", consumes = {"multipart/form-data"})
    public ResponseEntity<PostDetailResponse> updatePost(@PathVariable Long postId,
                                                         @RequestPart("request") PostEditRequest request,
                                                         @RequestPart(value = "newFiles", required = false) List<MultipartFile> newFiles,
                                                         @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.updatePost(postId, request, newFiles, user));
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        postService.deletePost(postId, user);
        return ResponseEntity.noContent().build();
    }

    /**
     * 게시글 좋아요 여부 조회
     */
    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<Boolean> checkLiked(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.isLikedPost(postId, user));
    }

    /**
     * 게시글 좋아요
     */
    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<Boolean> like(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.likePost(postId, user));
    }

    /**
     * 게시글 좋아요 취소
     */
    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity<Boolean> unlike(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.unlikePost(postId, user));
    }

    // TODO: CustomOAuth2UserPrincipal로 변경
}
