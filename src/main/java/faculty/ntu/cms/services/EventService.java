package faculty.ntu.cms.services;

import faculty.ntu.cms.models.Event;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.repositories.EventRepository;
import faculty.ntu.cms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    //Lấy tất cả sự kiện
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    //Lấy tất cả sự kiện đang hoạt động và chưa kết thúc
    public List<Event> getAllActiveEvents() {
        return eventRepository.findByIsActiveTrueAndEndTimeAfter(LocalDateTime.now());
    }

    //Lấy id sự kiện
    public Optional<Event> getEventById(int id) {
        return eventRepository.findById(id);
    }

    //Tạo sự kiện
    public Event createEvent(Event event, MultipartFile bannerFile) {
        //Lấy thông tin người tạo sự kiện từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UsernameNotFoundException("No authenticated user found");
        }

        String currentUserName;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            currentUserName = ((UserDetails) principal).getUsername();
        } else {
            currentUserName = principal.toString();
        }

        // Tìm đối tượng User từ cơ sở dữ liệu
        User currentUser = userRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUserName));

        // Gán người tạo
        event.setCreatedBy(currentUser);

        //Nếu có file banner, lưu file và cập nhật đường dẫn
        if (bannerFile != null && !bannerFile.isEmpty()) {
            String bannerPath = fileStorageService.storeFile(bannerFile);
            event.setBanner(bannerPath);
        }
        return eventRepository.save(event);
    }

    //Cập nhật sự kiện
    public Event updateEvent(int id,Event eventDetail, MultipartFile bannerFile) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sự kiện không tồn tại"));

        //Cập nhật các trường thông tin
        event.setTitle(eventDetail.getTitle());
        event.setDescription(eventDetail.getDescription());
        event.setStartTime(eventDetail.getStartTime());
        event.setEndTime(eventDetail.getEndTime());
        event.setIsActive(eventDetail.getIsActive());
        event.setRegistrationUrl(eventDetail.getRegistrationUrl());
        event.setUpdatedAt(eventDetail.getUpdatedAt());

        //Nếu có file banner mới, lưu file và cập nhật đường dẫn
        if(bannerFile != null && !bannerFile.isEmpty()){
            String bannerPath = fileStorageService.storeFile(bannerFile);
            event.setBanner(bannerPath);
        }

        return eventRepository.save(event);
    }

    //Xóa sự kiện theo id
    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }
}
