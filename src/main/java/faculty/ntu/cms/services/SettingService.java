package faculty.ntu.cms.services;

import faculty.ntu.cms.models.Setting;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.repositories.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;
    // Tạo mới hoặc cập nhật 1 setting theo setting key
    public Setting createOrUpdateSetting(String key, String value, String description, User createdBy) {
        Optional<Setting> existingSetting = settingRepository.findBySettingKey(key);
        Setting setting = existingSetting.orElse(new Setting());
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        setting.setDescription(description);
        setting.setCreatedBy(createdBy);
        return settingRepository.save(setting);
    }

    // Tạo mới setting
    public Setting createSetting(Setting setting, User createdBy) {
        setting.setCreatedBy(createdBy);
        return settingRepository.save(setting);
    }

    // Cập nhật setting
    public Setting updateSetting(Integer id, Setting updatedSetting, User updatedBy) {
        Optional<Setting> existingSetting = settingRepository.findById(id);
        if (existingSetting.isPresent()) {
            Setting setting = existingSetting.get();
            setting.setSettingKey(updatedSetting.getSettingKey());
            setting.setSettingValue(updatedSetting.getSettingValue());
            setting.setDescription(updatedSetting.getDescription());
            setting.setCreatedBy(updatedBy);
            return settingRepository.save(setting);
        } else {
            throw new RuntimeException("Setting not found with id: " + id);
        }
    }

    // Lấy setting dựa vào id
    public Optional<Setting> getSettingById(Integer id) {
        return settingRepository.findById(id);
    }

    // Lấy setting dựa vào settingKey
    public Optional<Setting> getSettingByKey(String key) {
        return settingRepository.findBySettingKey(key);
    }

    // Lấy tất cả setting
    public List<Setting> getAllSettings() {
        return settingRepository.findAll();
    }

    // Xóa setting dựa vào id
    public void deleteSetting(Integer id) {
        if(settingRepository.existsById(id)){
            settingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Setting not found with id: " + id);
        }
    }

}
