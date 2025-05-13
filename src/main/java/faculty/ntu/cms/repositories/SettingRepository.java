package faculty.ntu.cms.repositories;

import faculty.ntu.cms.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Integer> {
    Optional<Setting> findByKey(String settingKey);
}
