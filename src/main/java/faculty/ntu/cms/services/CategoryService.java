package faculty.ntu.cms.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import faculty.ntu.cms.models.Category;
import faculty.ntu.cms.repositories.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category Category) {
        Category.setCreatedAt(LocalDateTime.now());
        Category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(Category);
    }

}
