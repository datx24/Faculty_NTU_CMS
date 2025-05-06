package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Event;
import faculty.ntu.cms.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
	@Autowired
	private EventService eventService;

	@GetMapping("/")
	public String getHome(Model m) {
		List<Event> events = eventService.getAllActiveEvents();
		m.addAttribute("events", events);
		return "pages/user/home";
	}
}
