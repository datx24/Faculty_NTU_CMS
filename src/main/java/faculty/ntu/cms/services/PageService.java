package faculty.ntu.cms.services;

import faculty.ntu.cms.models.Page;
import faculty.ntu.cms.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PageService {

    private final PageRepository pageRepository;

    @Autowired
    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public List<Page> getAllPages() {
        return pageRepository.findAll();
    }

    public Optional<Page> getPageById(Integer id) {
        return pageRepository.findById(id);
    }

    public Page savePage(Page page) {
        // Set timestamps if new page
        if (page.getId() == null) {
            page.setCreatedAt(LocalDateTime.now());
        }
        page.setUpdatedAt(LocalDateTime.now());
        return pageRepository.save(page);
    }

    public Page updatePage(Integer id, Page updatedPage) {
        Optional<Page> existingPageOptional = pageRepository.findById(id);
        if (existingPageOptional.isPresent()) {
            Page existingPage = existingPageOptional.get();
            
            // Update only the fields that should change
            existingPage.setTitle(updatedPage.getTitle());
            existingPage.setSlug(updatedPage.getSlug());
            existingPage.setContent(updatedPage.getContent());
            existingPage.setIsActive(updatedPage.getIsActive());
            existingPage.setUpdatedAt(LocalDateTime.now());
            
            return pageRepository.save(existingPage);
        }
        return null;
    }

    public void deletePage(Integer id) {
        pageRepository.deleteById(id);
    }

    public Page findBySlug(String slug) {
        return pageRepository.findBySlug(slug);
    }

    public List<Page> findAllActivePages() {
        return pageRepository.findByIsActive(true);
    }
}