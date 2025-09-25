package com.aicodinator.backend.domain.community.mapper;

import com.aicodinator.backend.domain.community.domain.dto.response.CommentResponse;
import com.aicodinator.backend.domain.community.domain.entity.Comment;
import com.aicodinator.backend.domain.user.domain.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "id", target = "commentId")
    @Mapping(source = "user.name", target = "authorName")
    @Mapping(target = "isOwner", expression = "java(comment.getUser().getId().equals(currentUser.getId()))")
    CommentResponse toCommentResponse(Comment comment, @Context User currentUser);

    List<CommentResponse> toCommentResponseList(List<Comment> commentList, @Context User currentUser);
}
