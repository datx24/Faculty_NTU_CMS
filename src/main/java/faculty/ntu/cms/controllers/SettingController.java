package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Setting;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.SettingService;
import faculty.ntu.cms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private UserService userService;

    // Danh sách tất cả setting
    @GetMapping
    public String listSettings(Model m) {
        List<Setting> settings = settingService.getAllSettings();
        m.addAttribute("settings", settings);
        return "pages/admin/settings/list";
    }

    // Hiển thị form để thêm 1 setting mới
    @GetMapping("/new")
    public String showCreateForm(Model m) {
        m.addAttribute("setting", new Setting());
        return "pages/admin/settings/form";
    }

    // Thêm mới hoặc cập nhật setting
    @PostMapping
    public String saveSetting(@ModelAttribute Setting setting) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            setting.setCreatedBy((User) userDetails);
        } else {
            throw new RuntimeException("No authenticated user found");
        }
        if (setting.getId() == null) {
            settingService.createSetting(setting, setting.getCreatedBy());
        } else {
            settingService.updateSetting(setting.getId(), setting, setting.getCreatedBy());
        }
        return "redirect:/admin/settings";
    }

    // Xử lý lưu hàng loạt các setting
    @PostMapping("/bulk")
    public String saveSettings(@RequestParam Map<String, String> settingsMap) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User createdBy;
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            createdBy = (User) userDetails;
        } else {
            throw new RuntimeException("No authenticated user found");
        }
        settingsMap.remove("createdById");
        settingsMap.remove("_csrf");
        for (Map.Entry<String, String> entry : settingsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String description = "Updated via settings form";
            settingService.createOrUpdateSetting(key, value, description, createdBy);
        }
        return "redirect:/admin/settings";
    }
}
