package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Category;
import faculty.ntu.cms.models.CategoryWithPostDTO;
import faculty.ntu.cms.models.Event;
import faculty.ntu.cms.models.Post;
import faculty.ntu.cms.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/")
	public String getHome(Model m, HttpServletRequest request) {
		String requestURI = request != null ? request.getRequestURI() : "/";
		m.addAttribute("currentPath", requestURI);
		m.addAttribute("menuItems", menuItemService.getActiveMenuItemsByMenuName("primary"));
		m.addAttribute("events", eventService.getAllActiveEvents());
		m.addAttribute("notifications", notificationService.getAllNotifications());
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

		//danh sách thể loại với bài viết mới nhất 
		List<CategoryWithPostDTO> categoriesWithPosts = categoryService.getAllCategoriesWithNewestPost();
		m.addAttribute("categoriesWithPosts", categoriesWithPosts);
		return "pages/user/home";
	}

	//Xử lý xem chi tiết sự kiện
	@GetMapping("events/{id}")
	public String viewEvent(@PathVariable Integer id,
							Model m,
							RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String requestURI = request != null ? request.getRequestURI() : "/";
		m.addAttribute("currentPath", requestURI);
		m.addAttribute("menuItems", menuItemService.getActiveMenuItemsByMenuName("primary"));
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
	public String viewNotification(@PathVariable Integer id, Model m, HttpServletRequest request) {
		String requestURI = request != null ? request.getRequestURI() : "/";
		m.addAttribute("currentPath", requestURI);
		m.addAttribute("menuItems", menuItemService.getActiveMenuItemsByMenuName("primary"));
		notificationService.getNotificationById(id).ifPresent(n -> m.addAttribute("notification", n));
		m.addAttribute("menuItems", menuItemService.getActiveMenuItemsByMenuName("primary"));
		return "pages/user/notifications/detail";
	}
	@GetMapping("post-categories")
	public String viewPostMenu(Model m) {
		List<Category> categories = categoryService.getAllCategories();
		m.addAttribute("categories", categories);
		return "pages/user/post/categories_post";
	}
	@GetMapping("post-categories/{slug}")
	public String viewPostsByCategory(@PathVariable String slug, Model m) {
		// Lấy danh mục theo slug
		Optional<Category> categoryOpt = categoryService.getAllCategories().stream()
						.filter(cat -> cat.getSlug().equals(slug))
						.findFirst();
		if (categoryOpt.isPresent()) {
			Category category = categoryOpt.get();
			m.addAttribute("category", category);

			// Lấy danh sách bài viết thuộc danh mục (chỉ lấy published và active)
			List<Post> posts = postService.getPublishedPosts().stream()
							.filter(post -> post.getCategory() != null && post.getCategory().getId().equals(category.getId()))
							.filter(post -> post.getPostsStatus() == Post.PostsStatus.active)
							.sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
							.toList();
			m.addAttribute("posts", posts);

			// Lấy 1-3 bài viết nổi bật thuộc danh mục (theo viewCount)
			List<Post> featuredPosts = posts.stream()
							.sorted(Comparator.comparingInt(Post::getViewCount).reversed())
							.limit(3)
							.toList();
			m.addAttribute("featuredPosts", featuredPosts);

			// Lấy tất cả danh mục để hiển thị trong sidebar
			m.addAttribute("categories", categoryService.getAllCategories());
		} else {
			m.addAttribute("error", "Danh mục không tồn tại!");
		}
		return "pages/user/post/categories_post";
	}
	@GetMapping("/search")
	public String searchPosts(@RequestParam("query") String query, Model m) {
		// Lấy tất cả bài viết đã xuất bản
		List<Post> publishedPosts = postService.getPublishedPosts();

		// Tìm kiếm trong tiêu đề và excerpt
		List<Post> searchResults = publishedPosts.stream()
						.filter(post -> (post.getTitle() != null && post.getTitle().toLowerCase().contains(query.toLowerCase())) ||
										(post.getExcerpt() != null && post.getExcerpt().toLowerCase().contains(query.toLowerCase())))
						.sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
						.toList();

		m.addAttribute("posts", searchResults);
		m.addAttribute("categories", categoryService.getAllCategories());
		m.addAttribute("searchQuery", query); // Để hiển thị từ khóa tìm kiếm
		m.addAttribute("category", null); // Không gắn với danh mục cụ thể

		// Lấy 1-3 bài viết nổi bật từ kết quả tìm kiếm
		List<Post> featuredPosts = searchResults.stream()
						.sorted(Comparator.comparingInt(Post::getViewCount).reversed())
						.limit(3)
						.toList();
		m.addAttribute("featuredPosts", featuredPosts);

		return "pages/user/post/categories_post";
	}
	@GetMapping("/search-suggestions")
	@ResponseBody
	public ResponseEntity<List<String>> getSearchSuggestions(@RequestParam("query") String query) {
		List<Post> publishedPosts = postService.getPublishedPosts();
		List<String> suggestions = publishedPosts.stream()
						.filter(post -> post.getTitle() != null && post.getTitle().toLowerCase().contains(query.toLowerCase()))
						.map(Post::getTitle)
						.distinct()
						.limit(5) // Giới hạn số gợi ý tối đa là 5
						.collect(Collectors.toList());
		return ResponseEntity.ok(suggestions);
	}
}
