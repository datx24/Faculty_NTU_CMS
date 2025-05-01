package faculty.ntu.cms.controllers;

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
import faculty.ntu.cms.models.Post;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.CategoryService;
import faculty.ntu.cms.services.PostService;


@Controller
@RequestMapping("/admin/posts")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private CategoryService categoryService;


    public PostController() {
        super();
    }
    
    @GetMapping("")
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "pages/admin/posts/list"; 
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("post", new Post());
        return "pages/admin/posts/create"; 
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("post") Post post) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof UserDetails){
            UserDetails userdetails = (UserDetails) auth.getPrincipal();
            // User author = userService.loadUserByUsername(userdetails.getUsername());
            post.setAuthor((User) userdetails);
        }

        postService.savePost(post);
        return "redirect:/admin/posts";
    }

    @GetMapping("/edit/{id}")
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

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Integer id, @ModelAttribute("post") Post updatedPost) {
        Post savedPost = postService.updatePost(id, updatedPost);
        if (savedPost == null) {
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
