package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.MenuItem;
import faculty.ntu.cms.services.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/menu")
public class MenuItemController {
    @Autowired
    private MenuItemService menuItemService;

    // Hiển thị danh sách menu cho admin dựa vào role
    @GetMapping()
    public String getMenuItems(Model m) {
        m.addAttribute("menuItems", menuItemService.getAllMenuItems());
        m.addAttribute("distinctMenuNames",menuItemService.getMenuNameList());
        return "pages/admin/menu/list";
    }

    // Hiển thị form tạo menu cho admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String showCreateForm(Model m) {
        m.addAttribute("menuItem", new MenuItem());
        m.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "pages/admin/menu/create";
    }

    // Xử lý yêu cầu tạo menu mới từ form (chỉ dành cho ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public String createMenuItem(@ModelAttribute MenuItem menuItem) {
        menuItemService.saveMenuItem(menuItem);
        return "redirect:/admin/menu";
    }

    //Hiển thị form chỉnh sửa menu item
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id,Model m) {
        Optional<MenuItem> menuItem = menuItemService.getMenuItemById(id);
        if(menuItem.isPresent()){
            m.addAttribute("menuItem", menuItem.get());
            m.addAttribute("parentItems", menuItemService.getAllMenuItems());
            return "pages/admin/menu/edit";
        }
        return "redirect:/admin/menu";
    }

    //Xử lý cập nhật menu item
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateMenuItem(@PathVariable Integer id, @ModelAttribute MenuItem menuItem) {
        Optional<MenuItem> existingMenuItem = menuItemService.getMenuItemById(id);
        if(existingMenuItem.isPresent()) {
            menuItem.setId(id);
            menuItem.setCreatedAt(existingMenuItem.get().getCreatedAt());
            menuItemService.saveMenuItem(menuItem);
        }
        return "redirect:/admin/menu";
    }

    //Xử lý xóa menu item
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable int id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/admin/menu";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{name}")
    public String getMenuByMenuName(@PathVariable int id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/admin/menu";
    }
}
