package com.aicodinator.backend.domain.community.controller;

import com.aicodinator.backend.domain.community.domain.dto.request.CommentEditRequest;
import com.aicodinator.backend.domain.community.domain.dto.request.CommentRequest;
import com.aicodinator.backend.domain.community.domain.dto.response.CommentResponse;
import com.aicodinator.backend.domain.community.service.CommentService;
import com.aicodinator.backend.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request,user));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                              @RequestBody CommentEditRequest request,
                                              @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request, user));
    }

    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal User user) {
        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }

    // TODO: CustomOAuth2UserPrincipal로 변경
}
