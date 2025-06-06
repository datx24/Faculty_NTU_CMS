package faculty.ntu.cms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import faculty.ntu.cms.models.Category;
import faculty.ntu.cms.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // You can add custom query methods here if needed
    Post findBySlug(String slug); // Example of a custom query
    List<Post> findByStatus(Post.PostStatus status);
    Optional<Post> findFirstByCategoryOrderByUpdatedAtDesc(Category category);
} 
