package faculty.ntu.cms.models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 270)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String excerpt;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PostStatus status = PostStatus.draft;

    @Enumerated(EnumType.STRING)
    @Column(name = "posts_status", length = 20)
    private PostsStatus postsStatus = PostsStatus.active;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(length = 255)
    private String thumbnail;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum PostStatus {
        published, draft, pending
    }

    public enum PostsStatus {
        active, inactive
    }
}