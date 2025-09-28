package com.aicodinator.backend.domain.community.mapper;

import com.aicodinator.backend.domain.community.domain.dto.response.PostDetailResponse;
import com.aicodinator.backend.domain.community.domain.dto.response.PostFileResponse;
import com.aicodinator.backend.domain.community.domain.dto.response.PostListResponse;
import com.aicodinator.backend.domain.community.domain.entity.Post;
import com.aicodinator.backend.domain.community.domain.entity.PostFile;
import com.aicodinator.backend.domain.user.domain.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {PostFileMapper.class}
)
public interface PostMapper {
    @Mapping(source = "id", target = "postId")
    @Mapping(source = "user.name", target = "authorName")
    @Mapping(target = "isOwner", expression = "java(post.getUser().getId().equals(currentUser.getId()))")
    PostDetailResponse toPostDetailResponse(Post post, @Context User currentUser);

    @Mapping(source = "id", target = "postId")
    @Mapping(source = "files", target = "file", qualifiedByName = "thumbnailFile")
    PostListResponse toPostListResponse(Post post);

    List<PostListResponse> toPostListResponseList(List<Post> posts);

    @Named("thumbnailFile")
    default PostFile getThumbnailFile(List<PostFile> files) {
        if (files == null || files.isEmpty()) {
            return null;
        }

        return files.stream()
            .filter(f -> isImage(f.getStoredKey()))
            .findFirst()
            .orElse(null);
    }

    default boolean isImage(String storedKey) {
        if (storedKey == null) return false;

        return storedKey.endsWith(".jpg") || storedKey.endsWith(".jpeg") || storedKey.endsWith(".png");
    }
}
