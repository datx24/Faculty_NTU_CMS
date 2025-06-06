package faculty.ntu.cms.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import faculty.ntu.cms.services.CategoryService;
import faculty.ntu.cms.models.Category;
import faculty.ntu.cms.models.CategoryWithPostDTO;


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
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id,
                               Model m,
                               RedirectAttributes redirectAttributes) {
        try{
            Category category = categoryService.getCategoryById(id)
                    .orElseThrow(() -> new RuntimeException("Sự kiện không tồn tại"));
            m.addAttribute("category", category);
            return "pages/admin/categories/edit";
        }catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error","Sự kiện không tồn tại !");
            return "redirect:/categories/admin";
        }
    }

    
    @PostMapping("edit/{id}")
    public String updateEvent(@PathVariable Integer id,
                              @ModelAttribute Category category,
                              RedirectAttributes redirectAttributes) {
        
        try {
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("message","Cập nhật thể loại thành công !");
            return "redirect:/admin/categories";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error","Lỗi khi cập nhật: " + e.getMessage());
            return "redirect:/admin/categories/edit" + id;
        }
    }

    
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Integer id,
                              RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("message", "Xóa thành công!");
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa  " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

}
