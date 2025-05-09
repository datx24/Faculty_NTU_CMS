package faculty.ntu.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import faculty.ntu.cms.models.Page;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.PageService;
import faculty.ntu.cms.services.FileStorageService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private FileStorageService fileStorageService;

    // Admin List View
    @GetMapping("/admin/pages")
    public String listPages(Model model) {
        model.addAttribute("pages", pageService.getAllPages());
        return "pages/admin/pages/list";
    }

    // Create Form
    @GetMapping("/admin/pages/create")
    public String showCreateForm(Model model) {
        model.addAttribute("page", new Page());
        return "pages/admin/pages/create";
    }

    // Handle Creation
    @PostMapping("/admin/pages/create")
    public String createPage(@ModelAttribute("page") Page page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            page.setCreatedBy((User) userDetails);
        }
        page.setCreatedAt(LocalDateTime.now());
        page.setUpdatedAt(LocalDateTime.now());
        pageService.savePage(page);
        return "redirect:/admin/pages";
    }

    // Edit Form
    @GetMapping("/admin/pages/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        pageService.getPageById(id)
            .ifPresentOrElse(
                page -> model.addAttribute("page", page),
                () -> {
                    throw new IllegalArgumentException("Page with ID " + id + " not found.");
                }
            );
        return "pages/admin/pages/edit";
    }

    // Handle Update
    @PostMapping("/admin/pages/edit/{id}")
    public String updatePage(@PathVariable Integer id, @ModelAttribute("page") Page updatedPage) {
        Page existingPage = pageService.getPageById(id)
            .orElseThrow(() -> new IllegalArgumentException("Page with ID " + id + " not found."));
        
        existingPage.setTitle(updatedPage.getTitle());
        existingPage.setSlug(updatedPage.getSlug());
        existingPage.setContent(updatedPage.getContent());
        existingPage.setIsActive(updatedPage.getIsActive());
        existingPage.setUpdatedAt(LocalDateTime.now());
        
        pageService.savePage(existingPage);
        return "redirect:/admin/pages";
    }

    // Handle Deletion
    @GetMapping("/admin/pages/delete/{id}")
    public String deletePage(@PathVariable Integer id) {
        pageService.deletePage(id);
        return "redirect:/admin/pages";
    }

    // Public View
    @GetMapping("/{slug}")
    @ResponseBody
    public String viewPage(@PathVariable String slug, Model model) {
        Page page = pageService.findBySlug(slug);
        if (page == null || !page.getIsActive()) {
            return "error"; 
        }
        // 2 cachs render page content tùy thuộc thiết kế
        //neeus full raw html có cả <!DOCTYPE>
        return page.getContent();
        
        //nếu chỉ lưu body tag
        //model.addAttribute("page", page);
        //return "pages/user/page/page_detail";
    }
}