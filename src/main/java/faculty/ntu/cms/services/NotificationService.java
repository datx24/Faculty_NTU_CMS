package faculty.ntu.cms.services;

import faculty.ntu.cms.models.Notification;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.repositories.NotificationRepository;
import faculty.ntu.cms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    NotificationRepository notificationRepository;

    //Lấy tất cả thông báo
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    //Lấy thông báo dựa vào id
    public Optional<Notification> getNotificationById(int id) {
        return notificationRepository.findById(id);
    }

    // Lưu thông báo
    public Notification saveNotification(Notification notification) {
        // Lấy thông tin người dùng hiện tại từ SecurityContextHolder
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
        notification.setCreatedBy(currentUser);

        // Nếu là thêm mới thì set createdAt
        if (notification.getId() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }

        // Cập nhật updatedAt
        notification.setUpdatedAt(LocalDateTime.now());

        // Lưu vào database
        return notificationRepository.save(notification);
    }

    //Xóa thông báo
    public void deleteNotification(int id) {
        notificationRepository.deleteById(id);
    }
}
