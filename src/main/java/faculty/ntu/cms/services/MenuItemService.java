package faculty.ntu.cms.services;

import faculty.ntu.cms.models.MenuItem;
import faculty.ntu.cms.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    // Lấy menu hiển thị theo nhóm
    public List<MenuItem> getActiveMenuItemsByMenuName(String menuName) {
        return menuItemRepository.findByMenuNameAndActiveTrueOrderByOrder(menuName);
    }

    // Lấy toàn bộ menu
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    // Lấy menu dựa vào id
    public Optional<MenuItem> getMenuItemById(int id) {
        return menuItemRepository.findById(id);
    }

    // Lưu hoặc cập nhật 1 menu item
    public MenuItem saveMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    // Xóa 1 menu item dựa vào id
    public void deleteMenuItem(int id) {
        menuItemRepository.deleteById(id);
    }

    // Lấy menu con dựa vào id cha
    public List<MenuItem> getSubMenuItems(int parentId) {
        return menuItemRepository.findByParentId(parentId);
    }
    public List<String> getMenuNameList(){
        return menuItemRepository.findDistinctMenuNames();
    }
}
