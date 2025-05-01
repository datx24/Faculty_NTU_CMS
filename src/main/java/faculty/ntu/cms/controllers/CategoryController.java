package faculty.ntu.cms.controllers;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import faculty.ntu.cms.services.CategoryService;
import faculty.ntu.cms.models.Category;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    
    @Autowired
    CategoryService categoryService;

    public CategoryController() {
        super();
    }

    @GetMapping("")
    public String listCategory(Model model){
        model.addAttribute("categories", categoryService.getAllCategories());
        return "pages/admin/categories/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "pages/admin/categories/create"; 
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("category") Category category) {
        

        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

}
