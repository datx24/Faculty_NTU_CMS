package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Event;
import faculty.ntu.cms.models.Notification;
import faculty.ntu.cms.services.EventService;
import faculty.ntu.cms.services.NotificationService;
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

	@GetMapping("/")
	public String getHome(Model m) {
		List<Event> events = eventService.getAllActiveEvents();
		List<Notification> notifications = notificationService.getAllNotifications();
		m.addAttribute("events", events);
		m.addAttribute("notifications", notifications);
		return "pages/user/home";
	}
}
