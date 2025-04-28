package faculty.ntu.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import faculty.ntu.cms.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    
}