package faculty.ntu.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import faculty.ntu.cms.models.Event;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer>{
    //Tìm tất cả sự kiện đang hoạt động (isActive = True) và chưa kết thúc
    List<Event> findByIsActiveTrueAndEndTimeAfter(LocalDateTime now);
}

