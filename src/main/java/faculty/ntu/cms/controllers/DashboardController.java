package faculty.ntu.cms.controllers;

import faculty.ntu.cms.models.User;
import faculty.ntu.cms.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
	@Autowired
	private SettingService settingService;

}
