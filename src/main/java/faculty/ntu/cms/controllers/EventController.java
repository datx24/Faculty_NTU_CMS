package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Event;
import faculty.ntu.cms.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    //Hiển thị danh sách sự kiện cho người dùng
    @GetMapping
    public String listEvents(Model m) {
        //Lấy danh sách sự kiện đang hoạt động
        List<Event> events = eventService.getAllActiveEvents();
        m.addAttribute("events", events);
        return "pages/user/events/list";
    }

    //Hiển thị danh sách sự kiện cho admin
    @GetMapping("/admin")
    public String adminListEvents(Model m) {
        //Lấy tất cả danh sách sự kiện
        List<Event> events = eventService.getAllEvents();
        m.addAttribute("events",events);
        return "pages/admin/events/list";
    }

    //Hiển thị form tạo sự kiện mới cho admin
    @GetMapping("/admin/create")
    public String showCreateForm(Model m) {
        m.addAttribute("event", new Event());
        return "pages/admin/events/create";
    }

    //Xử lý tạo sự kiện mới với file banner
    @PostMapping("/admin")
    public String createEvent(@ModelAttribute Event event,
                              @RequestParam("bannerFile")
                              MultipartFile bannerFile){
        eventService.createEvent(event, bannerFile);
        return "redirect:/events/admin";
    }

    //Xử lý xem chi tiết sự kiện đối với người dùng
    @GetMapping("/{id}")
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

    //Hiển thị form chỉnh sửa sự kiện
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Integer id,
                               Model m,
                               RedirectAttributes redirectAttributes) {
        try{
            Event event = eventService.getEventById(id)
                    .orElseThrow(() -> new RuntimeException("Sự kiện không tồn tại"));
            m.addAttribute("event", event);
            return "pages/admin/events/edit";
        }catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error","Sự kiện không tồn tại !");
            return "redirect:/events/admin";
        }
    }

    //Xử lý cập nhật sự kiện
    @PostMapping("admin/edit/{id}")
    public String updateEvent(@PathVariable Integer id,
                              @ModelAttribute Event event,
                              @RequestParam("bannerFile") MultipartFile bannerFile,
                              RedirectAttributes redirectAttributes) {
        try {
            eventService.updateEvent(id, event, bannerFile);
            redirectAttributes.addFlashAttribute("message","Cập nhật sự kiện thành công !");
            return "redirect:/events/admin";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error","Lỗi khi cập nhật sự kiện: " + e.getMessage());
            return "redirect:/events/admin/edit" + id;
        }
    }

    //Xử lý xóa sự kiện
    @GetMapping("/admin/delete/{id}")
    public String deleteEvent(@PathVariable Integer id,
                              RedirectAttributes redirectAttributes) {
        try {
            eventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("message", "Xóa sự kiện thành công!");
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sự kiện " + e.getMessage());
        }
        return "redirect:/events/admin";
    }
}
