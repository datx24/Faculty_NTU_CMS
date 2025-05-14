package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Event;
import faculty.ntu.cms.models.Post;
import faculty.ntu.cms.services.EventService;
import faculty.ntu.cms.services.MenuItemService;
import faculty.ntu.cms.services.NotificationService;
import faculty.ntu.cms.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;


@Controller
public class HomeController {
	@Autowired
	private EventService eventService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private MenuItemService menuItemService;
	@Autowired
	private PostService postService;

	@GetMapping("/")
	public String getHome(Model m, HttpServletRequest request) {
		String requestURI = request != null ? request.getRequestURI() : "/";
		m.addAttribute("currentPath", requestURI);
		m.addAttribute("events", eventService.getAllActiveEvents());
		m.addAttribute("notifications", notificationService.getAllNotifications());
		m.addAttribute("menuItems", menuItemService.getActiveMenuItemsByMenuName("primary"));
		// Lấy tất cả bài viết đã xuất bản
		List<Post> publishedPosts = postService.getPublishedPosts();
		publishedPosts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt())); // Sắp xếp theo createdAt giảm dần

		// Bài viết nổi bật (có view_count cao nhất)
		Post featuredPost = publishedPosts.stream()
						.max(Comparator.comparingInt(Post::getViewCount))
						.orElse(null);
		m.addAttribute("featuredPost", featuredPost);

		// Các bài viết khác (loại trừ bài nổi bật), giữ thứ tự theo createdAt
		List<Post> otherPosts = publishedPosts.stream()
						.filter(post -> !post.equals(featuredPost))
						.sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt())) // Đảm bảo sắp xếp lại
						.toList();
		m.addAttribute("otherPosts", otherPosts);

		return "pages/user/home";
	}

	//Xử lý xem chi tiết sự kiện
	@GetMapping("events/{id}")
	public String viewEvent(@PathVariable Integer id,
							Model m,
							RedirectAttributes redirectAttributes) {
		try {
			Event event = eventService.getEventById(id)
					.orElseThrow(() -> new RuntimeException("Sự kiện không tồn tại"));
			m.addAttribute("event", event);
			return "pages/user/events/view";
		}catch (RuntimeException e) {
			redirectAttributes.addAttribute("error","Sự kiện không tồn tại!");
			return "redirect:/events";
		}
	}

	//Xử lý xem chi tiết thông báo
	@GetMapping("notifications/{id}")
	public String viewNotification(@PathVariable Integer id, Model m) {
		notificationService.getNotificationById(id).ifPresent(n -> m.addAttribute("notification", n));
		return "pages/user/notifications/detail";
	}
}
