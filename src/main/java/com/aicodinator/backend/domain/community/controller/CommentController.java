
package com.aicodinator.backend.domain.community.controller;

import com.aicodinator.backend.domain.community.domain.dto.request.CommentEditRequest;
import com.aicodinator.backend.domain.community.domain.dto.request.CommentRequest;
import com.aicodinator.backend.domain.community.domain.dto.response.CommentResponse;
import com.aicodinator.backend.domain.community.service.CommentService;
import com.aicodinator.backend.global.security.CustomOAuth2User;
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
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId, oAuth2User.getUser()));
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request, @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request, oAuth2User.getUser()));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                              @RequestBody CommentEditRequest request,
                                              @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request, oAuth2User.getUser()));
    }

    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        commentService.deleteComment(commentId, oAuth2User.getUser());
        return ResponseEntity.noContent().build();
    }
}
