package faculty.ntu.cms.models;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menu_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "menu_name", nullable = false)
    private String menuName = "primary";

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private String path = "#";

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private MenuItem parent;

    @Column(name = "menu_order", nullable = false)
    private Integer order = 0;

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "parent")
    private List<MenuItem> children;

    public void addChild(MenuItem child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(MenuItem child) {
        children.remove(child);
        child.setParent(null);
    }
}