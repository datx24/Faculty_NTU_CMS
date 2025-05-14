package faculty.ntu.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import faculty.ntu.cms.models.MenuItem;
import faculty.ntu.cms.models.Page;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.PageService;
import faculty.ntu.cms.services.FileStorageService;
import faculty.ntu.cms.services.MenuItemService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private MenuItemService menuItemService;

    // Admin List View
    @GetMapping("/admin/pages")
    public String listPages(Model model) {
        model.addAttribute("pages", pageService.getAllPages());
        return "pages/admin/pages/list";
    }

    // Create Form
    @GetMapping("/admin/pages/create")
    public String showCreateForm(Model model, HttpServletRequest request) {
        String requestURI = request != null ? request.getRequestURI() : "/";
        model.addAttribute("page", new Page());
        model.addAttribute("currentPath", requestURI);

        Map<String, List<MenuItem>> menuItemsByMenuName = menuItemService.getAllMenuItems().stream()
        .collect(Collectors.groupingBy(MenuItem::getMenuName));
        model.addAttribute("menuItemsByMenuName", menuItemsByMenuName);
        model.addAttribute("menuNames", new ArrayList<>(menuItemsByMenuName.keySet()));
        return "pages/admin/pages/form";
    }

    // Handle Creation
    @PostMapping("/admin/pages")
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
        String requestURI = "/";
        model.addAttribute("page", new Page());
        model.addAttribute("currentPath", requestURI);

        Map<String, List<MenuItem>> menuItemsByMenuName = menuItemService.getAllMenuItems().stream()
        .collect(Collectors.groupingBy(MenuItem::getMenuName));
        model.addAttribute("menuItemsByMenuName", menuItemsByMenuName);
        model.addAttribute("menuNames", new ArrayList<>(menuItemsByMenuName.keySet()));
        pageService.getPageById(id)
            .ifPresentOrElse(
                page -> model.addAttribute("page", page),
                () -> {
                    throw new IllegalArgumentException("Page with ID " + id + " not found.");
                }
            );
        return "pages/admin/pages/form";
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