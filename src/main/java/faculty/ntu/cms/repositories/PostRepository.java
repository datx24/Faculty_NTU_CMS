package faculty.ntu.cms.repositories;

import faculty.ntu.cms.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // You can add custom query methods here if needed
    Post findBySlug(String slug); // Example of a custom query
    List<Post> findByStatus(Post.PostStatus status);
} 
