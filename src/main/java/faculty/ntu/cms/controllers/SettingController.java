package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.Setting;
import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.FileStorageService;
import faculty.ntu.cms.services.SettingService;
import faculty.ntu.cms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

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
    public String saveSetting(@ModelAttribute Setting setting, @RequestParam(value = "image", required = false) MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User createdBy;
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            createdBy = (User) userDetails;
            setting.setCreatedBy(createdBy);
        } else {
            throw new RuntimeException("No authenticated user found");
        }

        // Handle image upload if provided
        if (file != null && !file.isEmpty()) {
            if (!file.getContentType().startsWith("image/")) {
                throw new RuntimeException("Invalid file type. Only images are allowed.");
            }
            String fileUrl = fileStorageService.storeFile(file);
            setting.setSettingValue(fileUrl);
        }

        if (setting.getId() == null) {
            settingService.createSetting(setting, createdBy);
        } else {
            settingService.updateSetting(setting.getId(), setting, createdBy);
        }
        return "redirect:/admin/settings";
    }

    // Xử lý lưu hàng loạt các setting
    @PostMapping("/bulk")
    public String saveSettings(@RequestParam Map<String, String> settingsMap, @RequestParam(value = "site_logo", required = false) MultipartFile siteLogo,@RequestParam(value = "site_banner", required = false) MultipartFile siteBanner) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User createdBy;
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            createdBy = (User) userDetails;
        } else {
            throw new RuntimeException("No authenticated user found");
        }

        // Handle site_logo file upload
        if (siteLogo != null && !siteLogo.isEmpty()) {
            if (!siteLogo.getContentType().startsWith("image/")) {
                throw new RuntimeException("Invalid file type for site_logo. Only images are allowed.");
            }
            String fileUrl = fileStorageService.storeFile(siteLogo);
            settingsMap.put("site_logo", fileUrl);
        }

        if (siteBanner != null && !siteBanner.isEmpty()) {
            if (!siteBanner.getContentType().startsWith("image/")) {
                throw new RuntimeException("Invalid file type for site_logo. Only images are allowed.");
            }
            String fileUrl = fileStorageService.storeFile(siteBanner);
            settingsMap.put("site_banner", fileUrl);
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