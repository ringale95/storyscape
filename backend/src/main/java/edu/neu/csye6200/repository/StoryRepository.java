package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.Story;
import edu.neu.csye6200.entity.StoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    /**
     * Find stories by author
     */
    List<Story> findByAuthorId(Long authorId);

    /**
     * Find stories by status
     */
    List<Story> findByStatus(StoryStatus status);

    /**
     * Find published stories ordered by published date
     */
    List<Story> findByStatusOrderByPublishedAtDesc(StoryStatus status);

    /**
     * Find stories by author and status
     */
    List<Story> findByAuthorIdAndStatus(Long authorId, StoryStatus status);

    /**
     * Find featured stories
     */
    List<Story> findByIsFeaturedTrueAndStatus(StoryStatus status);

    /**
     * Find boosted stories
     */
    List<Story> findByIsBoostedTrueAndStatus(StoryStatus status);

    /**
     * Find stories published after a certain date
     */
    @Query("SELECT s FROM Story s WHERE s.status = 'PUBLISHED' AND s.publishedAt > :date ORDER BY s.publishedAt DESC")
    List<Story> findRecentPublished(@Param("date") LocalDateTime date);

    /**
     * Find stories by tag
     */
    @Query("SELECT DISTINCT s FROM Story s JOIN s.tags t WHERE t.id = :tagId AND s.status = 'PUBLISHED'")
    List<Story> findByTagId(@Param("tagId") Long tagId);

    /**
     * Find stories by tag name
     */
    @Query("SELECT DISTINCT s FROM Story s JOIN s.tags t WHERE t.name = :tagName AND s.status = 'PUBLISHED'")
    List<Story> findByTagName(@Param("tagName") String tagName);

    /**
     * Find trending stories (most viewed in last 7 days)
     */
    @Query("SELECT s FROM Story s WHERE s.status = 'PUBLISHED' AND s.publishedAt > :date ORDER BY s.viewCount DESC")
    List<Story> findTrending(@Param("date") LocalDateTime date);

    /**
     * Search stories by title or body
     */
    @Query("SELECT s FROM Story s WHERE s.status = 'PUBLISHED' AND " +
            "(LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.body) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Story> searchStories(@Param("query") String query);

    /**
     * Count stories by author
     */
    long countByAuthorId(Long authorId);

    /**
     * Count published stories by author
     */
    long countByAuthorIdAndStatus(Long authorId, StoryStatus status);

    /**
     * Find the most recent story for a user, ordered by creation date descending.
     * 
     * @param userId The ID of the user
     * @return Optional containing the most recent story, or empty if none found
     */
    @Query("SELECT s FROM Story s WHERE s.author.id = :userId ORDER BY s.createdAt DESC")
    Optional<Story> findTopByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
