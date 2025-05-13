package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Event;
import faculty.ntu.cms.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/events")
public class EventController {
    @Autowired
    private EventService eventService;

    //Hiển thị danh sách sự kiện cho admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public String adminListEvents(Model m) {
        //Lấy tất cả danh sách sự kiện
        List<Event> events = eventService.getAllEvents();
        m.addAttribute("events",events);
        return "pages/admin/events/list";
    }

    //Hiển thị form tạo sự kiện mới cho admin
    @GetMapping("/create")
    public String showCreateForm(Model m) {
        m.addAttribute("event", new Event());
        return "pages/admin/events/list";
    }

    //Xử lý tạo sự kiện mới với file banner
    @PostMapping()
    public String createEvent(@ModelAttribute Event event,
                              @RequestParam("bannerFile")
                              MultipartFile bannerFile){
        eventService.createEvent(event, bannerFile);
        return "redirect:admin/events";
    }

    //Hiển thị form chỉnh sửa sự kiện
    @GetMapping("/edit/{id}")
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
    @PostMapping("edit/{id}")
    public String updateEvent(@PathVariable Integer id,
                              @ModelAttribute Event event,
                              @RequestParam("bannerFile") MultipartFile bannerFile,
                              RedirectAttributes redirectAttributes) {
        try {
            eventService.updateEvent(id, event, bannerFile);
            redirectAttributes.addFlashAttribute("message","Cập nhật sự kiện thành công !");
            return "redirect:/admin/events";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error","Lỗi khi cập nhật sự kiện: " + e.getMessage());
            return "redirect:/admin/events/edit" + id;
        }
    }

    //Xử lý xóa sự kiện
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Integer id,
                              RedirectAttributes redirectAttributes) {
        try {
            eventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("message", "Xóa sự kiện thành công!");
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa sự kiện " + e.getMessage());
        }
        return "redirect:/admin/events";
    }
}
