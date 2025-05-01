package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "pages/admin/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model m) {
        m.addAttribute("user", new User());
        return "pages/admin/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
    	if (user.getPassword() == null || user.getPassword().isEmpty()) {
            model.addAttribute("error", "Password không được để trống.");
            return "pages/admin/register"; // quay lại trang đăng ký
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.registerUser(user);
        return "redirect:/login";
    }
}
