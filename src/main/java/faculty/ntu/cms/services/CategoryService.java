package faculty.ntu.cms.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import faculty.ntu.cms.models.Category;
import faculty.ntu.cms.models.CategoryWithPostDTO;
import faculty.ntu.cms.models.Post;
import faculty.ntu.cms.repositories.CategoryRepository;
import faculty.ntu.cms.repositories.PostRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    
    @Autowired
    private PostRepository postRepository;

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
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    public List<CategoryWithPostDTO> getAllCategoriesWithNewestPost() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> {
            Optional<Post> newestPostOpt = postRepository.findFirstByCategoryOrderByUpdatedAtDesc(category);
            Post newestPost = newestPostOpt.orElse(null);
            return CategoryWithPostDTO.fromEntity(category, newestPost);
            
        }).collect(Collectors.toList());
    }

}
