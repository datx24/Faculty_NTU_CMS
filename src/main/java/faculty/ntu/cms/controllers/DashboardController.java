package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
	
	@GetMapping("/dashboard")
	public String getDashBoard(Model m) {
		String name = "";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) { // Ép kiểu trực tiếp về User
			name = ((User) principal).getName();
		}
		m.addAttribute("name", name);

		return "pages/admin/dashboard";
	}
}
