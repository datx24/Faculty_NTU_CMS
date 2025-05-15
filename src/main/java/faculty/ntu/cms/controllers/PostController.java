package faculty.ntu.cms.controllers;

import faculty.ntu.cms.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import faculty.ntu.cms.models.Post;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.CategoryService;
import faculty.ntu.cms.services.PostService;
import faculty.ntu.cms.services.FileStorageService;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private EventService eventService;
    private final String uploadDir = "/uploads/";

    public PostController() {
        super();
    }

    @GetMapping("/admin/posts")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "pages/admin/posts/list";
    }

    @GetMapping("/admin/posts/create")
    public String showCreateForm(Model model) {
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("post", new Post());
        return "pages/admin/posts/create";
    }

    @PostMapping("/admin/posts/create")
    public String createPost(@ModelAttribute("post") Post post,  @RequestParam("thumbnailFile") MultipartFile thumbnailFile) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof UserDetails){
            UserDetails userdetails = (UserDetails) auth.getPrincipal();
            // User author = userService.loadUserByUsername(userdetails.getUsername());
            post.setAuthor((User) userdetails);
        }
        if (!thumbnailFile.isEmpty()) {
            try {
                String thumbnailPath = fileStorageService.storeFile(thumbnailFile);
                post.setThumbnail(thumbnailPath);
            } catch (Exception e) {

                return "redirect:/admin/posts/create";
            }
        }

        postService.savePost(post);
        return "redirect:/admin/posts";
    }

    @GetMapping("/admin/posts/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categories",categoryService.getAllCategories());
        postService.getPostById(id)
                .ifPresentOrElse(
                        post -> model.addAttribute("post", post),
                        () -> {
                            throw new IllegalArgumentException("Post with ID " + id + " not found.");
                        }
                );
        return "pages/admin/posts/edit";
    }

    @PostMapping("/admin/posts/edit/{id}")
    public String updatePost(@PathVariable Integer id, @ModelAttribute("post") Post updatedPost,
                             @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {
        // Lấy bài viết hiện tại để kiểm tra ảnh cũ
        Post existingPost = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết với ID " + id + " không tồn tại để cập nhật."));

        // Xử lý thay thế ảnh nếu có ảnh mới
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                // Xóa ảnh cũ nếu tồn tại
                if (existingPost.getThumbnail() != null && !existingPost.getThumbnail().isEmpty()) {
                    Path oldFilePath = Paths.get(existingPost.getThumbnail().substring(1)); // Bỏ dấu "/" đầu tiên
                    Files.deleteIfExists(oldFilePath);
                }
                // Lưu ảnh mới
                String thumbnailPath = fileStorageService.storeFile(thumbnailFile);
                updatedPost.setThumbnail(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
            }
        } else {
            // Giữ nguyên ảnh cũ nếu không upload ảnh mới
            updatedPost.setThumbnail(existingPost.getThumbnail());
        }

        Post savedPost = postService.updatePost(id, updatedPost);
        if (savedPost == null) {
            throw new IllegalArgumentException("Post with ID " + id + " not found for update.");
        }
        return "redirect:/admin/posts";
    }

    @GetMapping("/admin/posts/delete/{id}")
    public String deletePost(@PathVariable Integer id) {
        // Lấy bài viết để kiểm tra
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bài viết với ID " + id + " không tồn tại."));

        // Xóa các sự kiện liên quan trước
        eventService.deleteEventsByRecapPostId(id);

        // Xóa ảnh liên quan
        if (post.getThumbnail() != null && !post.getThumbnail().isEmpty()) {
            try {
                Path filePath = Paths.get(post.getThumbnail().substring(1));
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // Log lỗi nếu cần
            }
        }
        postService.deletePost(id);
        return "redirect:/admin/posts";
    }
    @GetMapping("posts/{slug}")
    public String viewPost(@PathVariable String slug, Model model) {
        System.out.println("Processing slug: " + slug); // Debug
        Post post = postService.findBySlug(slug);
        if (post == null) {
            System.out.println("Post not found for slug: " + slug); // Debug
            return "error"; // Trang lỗi tùy chỉnh
        }
        // Tăng lượt xem
        post.setViewCount(post.getViewCount() + 1);
        postService.savePost(post);

        // Lấy danh sách bài viết đã published, trừ bài viết hiện tại
        List<Post> publishedPosts = postService.getPublishedPosts()
                .stream()
                .filter(p -> !p.getSlug().equals(slug)) // Loại trừ bài viết hiện tại
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt())) // Sắp xếp theo createdAt giảm dần
                .limit(9) // Giới hạn 3 bài viết
                .collect(Collectors.toList());

        model.addAttribute("post", post);
        model.addAttribute("relatedPosts", publishedPosts); // Truyền danh sách bài viết liên quan vào model
        return "pages/user/post/post_detail";
    }
}