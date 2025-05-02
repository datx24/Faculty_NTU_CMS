package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Notification;
import faculty.ntu.cms.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    // Hiển thị danh sách thông báo cho admin
    @GetMapping
    public String listNotification(Model m) {
        m.addAttribute("notifications", notificationService.getAllNotifications());
        return "pages/admin/notifications/list";
    }

    // Hiển thị form tạo mới thông báo cho admin
    @GetMapping("/new")
    public String newNotifications(Model m) {
        m.addAttribute("notification", new Notification());
        return "pages/admin/notifications/form";
    }

    //Lưu hoặc cập nhật thông báo mới cho admin
    @PostMapping
    public String saveNotifications(@ModelAttribute Notification notification) {
        notificationService.saveNotification(notification);
        return "redirect:/admin/khoa-it/notifications";
    }

    //Sửa thông tin cho admin
    @GetMapping("/{id}/edit")
    public String editNotificationForm(@PathVariable Integer id, Model m) {
        notificationService.getNotificationById(id)
                .ifPresent(n -> m.addAttribute("notification",n));
        return "pages/admin/notifications/form";
    }

    //Xóa thông tin cho admin
    @GetMapping("{id}/delete")
    public String deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return "redirect:/admin/khoa-it/notifications";
    }
}
