package faculty.ntu.cms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import faculty.ntu.cms.models.Page;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    
    Page findBySlug(String slug); 
    List<Page> findByIsActive(Boolean b);
} 
