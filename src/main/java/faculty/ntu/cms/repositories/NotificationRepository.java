package faculty.ntu.cms.repositories;

import faculty.ntu.cms.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
