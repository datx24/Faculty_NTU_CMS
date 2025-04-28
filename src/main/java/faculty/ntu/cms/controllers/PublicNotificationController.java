package faculty.ntu.cms.controllers;

import faculty.ntu.cms.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/khoa-it/thong-bao")
public class PublicNotificationController {
    @Autowired
    private NotificationService notificationService;

    // Hiển thị danh sách thông báo cho user
    @GetMapping
    public String listPublicNotification(Model m) {
        m.addAttribute("notifications", notificationService.getAllNotifications());
        return "pages/user/notifications/list";
    }

    @GetMapping("/{id}")
    public String viewNotification(@PathVariable Integer id, Model m) {
        notificationService.getNotificationById(id).ifPresent(n -> m.addAttribute("notification", n));
        return "pages/user/notifications/detail";
    }
}
