package faculty.ntu.cms.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "link_type", nullable = false, length = 20)
    private LinkType linkType;

    @ManyToOne
    @JoinColumn(name = "menu_items_link_type_external")
    private MenuItem menuItemsLinkTypeExternal;

    @Column(name = "custom_url", length = 255)
    private String customUrl;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private MenuItem parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<MenuItem> children;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enum definition
    public enum LinkType {
        INTERNAL, EXTERNAL, CUSTOM
    }

    // Constructors
    public MenuItem() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MenuItem(Integer id, String label, LinkType linkType, MenuItem menuItemsLinkTypeExternal, String customUrl, MenuItem parent, List<MenuItem> children, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this();
        this.id = id;
        this.label = label;
        this.linkType = linkType;
        this.menuItemsLinkTypeExternal = menuItemsLinkTypeExternal;
        this.customUrl = customUrl;
        this.parent = parent;
        this.children = children;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public MenuItem getMenuItemsLinkTypeExternal() {
        return menuItemsLinkTypeExternal;
    }

    public void setMenuItemsLinkTypeExternal(MenuItem menuItemsLinkTypeExternal) {
        this.menuItemsLinkTypeExternal = menuItemsLinkTypeExternal;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public MenuItem getParent() {
        return parent;
    }

    public void setParent(MenuItem parent) {
        this.parent = parent;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItem> children) {
        this.children = children;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
