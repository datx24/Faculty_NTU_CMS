package faculty.ntu.cms.models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "setting_key", nullable = false, unique = true, length = 100)
    private String key;

    @Lob
    @Column(name = "setting_value")
    private String value;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
