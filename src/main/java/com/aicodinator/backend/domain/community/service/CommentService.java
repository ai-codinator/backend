package com.aicodinator.backend.domain.community.service;

import com.aicodinator.backend.domain.community.domain.dto.request.CommentEditRequest;
import com.aicodinator.backend.domain.community.domain.dto.request.CommentRequest;
import com.aicodinator.backend.domain.community.domain.dto.response.CommentResponse;
import com.aicodinator.backend.domain.community.domain.entity.Comment;
import com.aicodinator.backend.domain.community.domain.entity.Post;
import com.aicodinator.backend.domain.community.mapper.CommentMapper;
import com.aicodinator.backend.domain.community.repository.CommentRepository;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.global.exception.CustomException;
import com.aicodinator.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentMapper.toCommentResponseList(commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId));
    }

    @Transactional
    public CommentResponse createComment(CommentRequest request, User user) {
        Post post = postService.findById(request.getPostId());

        Comment comment = commentRepository.save(Comment.builder()
            .post(post)
            .user(user)
            .content(request.getContent())
            .build());

        return commentMapper.toCommentResponse(comment, user);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentEditRequest request, User user) {
        Comment comment = findCommentAndValidate(commentId, user);
        comment.setContent(request.getContent());
        return commentMapper.toCommentResponse(commentRepository.save(comment), user);
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        commentRepository.delete(findCommentAndValidate(commentId, user));
    }

    private Comment findCommentAndValidate(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "요청하신 댓글을 찾을 수 없습니다."));

        if (!user.equals(comment.getUser())) {
            throw new CustomException(ErrorCode.FORBIDDEN, "해당 댓글에 대한 권한이 없습니다.");
        }

        return comment;
    }
}
