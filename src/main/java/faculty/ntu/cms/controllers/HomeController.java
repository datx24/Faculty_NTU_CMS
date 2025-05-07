package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Event;
import faculty.ntu.cms.models.MenuItem;
import faculty.ntu.cms.models.Notification;
import faculty.ntu.cms.services.EventService;
import faculty.ntu.cms.services.MenuItemService;
import faculty.ntu.cms.services.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
	@Autowired
	private EventService eventService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private MenuItemService menuItemService;
	
	@GetMapping("/")
	public String getHome(Model m, HttpServletRequest request) {
		String requestURI = request != null ? request.getRequestURI() : "/";
		m.addAttribute("currentPath", requestURI);
		m.addAttribute("events", eventService.getAllActiveEvents());
		m.addAttribute("notifications", notificationService.getAllNotifications());
		m.addAttribute("menuItems", menuItemService.getActiveMenuItemsByMenuName("primary"));
		return "pages/user/home";
	}
}
