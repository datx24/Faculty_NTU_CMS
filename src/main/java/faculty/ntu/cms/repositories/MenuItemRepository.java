package faculty.ntu.cms.repositories;

import faculty.ntu.cms.models.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    // Lấy danh sách menu theo tên, đang hiển thị, có sắp xếp
    // Dùng để render toàn bộ menu chính
    List<MenuItem> findByMenuNameAndActiveTrueOrderByOrder(String menuName);
    // Lấy danh sách menu con theo menu cha
    // Dùng để hiển thị menu cấp 2, cấp 3
    List<MenuItem> findByParentId(Long parentId);
}
