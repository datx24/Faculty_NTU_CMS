package faculty.ntu.cms.config;

import faculty.ntu.cms.models.Setting;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private SettingService settingService;

    @ModelAttribute
    public void addSettingToModel(Model m) {
        Map<String, String> settingMap = new HashMap<>();
        for (Setting s : settingService.getAllSettings()) {
            settingMap.put(s.getSettingKey(), s.getSettingValue());
        }
        m.addAttribute("settingMap", settingMap);

        // Hiện tên người quản trị trong toàn bộ controller
        String name = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            name = ((User) principal).getName();
        }
        m.addAttribute("name", name);
    }
}
