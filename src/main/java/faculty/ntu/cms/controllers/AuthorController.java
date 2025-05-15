package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Role;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.RoleService;
import faculty.ntu.cms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/authorization")
public class AuthorController {
  private final RoleService roleService;
  private final UserService userService;
  @Autowired
  public AuthorController(RoleService roleService, UserService userService) {
    this.roleService = roleService;
    this.userService = userService;
  }
  @GetMapping
  public String showAuthorizationPage(Model model) {
    List<User> users = userService.findAll();
    List<Role> roles = roleService.getAllRoles();
    model.addAttribute("users", users);
    model.addAttribute("roles", roles);
    return "pages/admin/authorization"; // Trả về tên file template (authorization.html)
  }
  @PostMapping("/assign")
  public String assignRole(@RequestParam("userId") Integer userId, @RequestParam("roleId") Integer roleId, Model model) {
    try {
      User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
      Role role = roleService.getRoleById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
      user.setRole(role);
      userService.registerUser(user); // Lưu lại người dùng với vai trò mới
      return "redirect:/admin/authorization?success=true";
    } catch (Exception e) {
      model.addAttribute("error", "Failed to assign role: " + e.getMessage());
      model.addAttribute("users", userService.findAll());
      model.addAttribute("roles", roleService.getAllRoles());
      return "pages/admin/authorization";
    }
  }
}
