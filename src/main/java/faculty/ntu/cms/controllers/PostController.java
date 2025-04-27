package faculty.ntu.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import faculty.ntu.cms.models.Post;
import faculty.ntu.cms.services.PostService;

@Controller
@RequestMapping("/admin/posts")
public class PostController {
    
    @Autowired
    private PostService postService;


    public PostController() {
 
    }
    
    @GetMapping
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "admin/posts/list"; 
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "admin/posts/create"; 
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("post") Post post) {
        postService.savePost(post);
        return "redirect:/admin/posts";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        postService.getPostById(id)
                .ifPresentOrElse(
                        post -> model.addAttribute("post", post),
                        () -> {
                            // Handle not found scenario (e.g., redirect with error message)
                            // For simplicity, redirecting back to the list
                            throw new IllegalArgumentException("Post with ID " + id + " not found.");
                        }
                );
        return "admin/posts/edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Integer id, @ModelAttribute("post") Post updatedPost) {
        Post savedPost = postService.updatePost(id, updatedPost);
        if (savedPost == null) {
            // Handle update failure (e.g., post not found)
            // For simplicity, redirecting back to the list
            throw new IllegalArgumentException("Post with ID " + id + " not found for update.");
        }
        return "redirect:/admin/posts";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return "redirect:/admin/posts";
    }
}
