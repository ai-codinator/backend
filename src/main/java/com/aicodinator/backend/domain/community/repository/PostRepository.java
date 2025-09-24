package com.aicodinator.backend.domain.community.repository;

import com.aicodinator.backend.domain.community.domain.entity.Board;
import com.aicodinator.backend.domain.community.domain.entity.Post;
import com.aicodinator.backend.domain.region.domain.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * 게시판별 게시글 페이지네이션 조회
     */
    Page<Post> findByBoard(Board board, Pageable pageable);

    /**
     * 게시글 상세 조회 Fetch Join
     */
    @Query("SELECT p FROM Post p " +
        "LEFT JOIN FETCH p.user " +
        "LEFT JOIN FETCH p.files " +
        "LEFT JOIN FETCH p.comments c " +
        "LEFT JOIN FETCH c.user " +
        "WHERE p.id = :postId")
    Optional<Post> findByIdWithDetails(@Param("postId") Long postId);

    /**
     * 최신글 3개 조회
     */
    List<Post> findTop3ByRegionOrderByCreatedAtDesc(Region region, Pageable pageable);

    /**
     * 인기글 3개 조회
     */
    @Query("SELECT p FROM Post p WHERE p.region = :region ORDER BY p.likeCount DESC")
    List<Post> findTop3PopularPosts(@Param("region") Region region, Pageable pageable);

    /**
     * 댓글 많은 글 3개 조회
     */
    @Query("SELECT p FROM Post p WHERE p.region = :region ORDER BY SIZE(p.comments) DESC")
    List<Post> findTop3MostCommentedPosts(@Param("region") Region region, Pageable pageable);

    /**
     * 최신 댓글이 달린 글 3개 조회
     */
    @Query("SELECT c.post FROM Comment c WHERE c.post.region = :region GROUP BY c.post ORDER BY MAX(c.createdAt) DESC")
    List<Post> findTop3LatestCommentedPosts(@Param("region") Region region, Pageable pageable);
}
