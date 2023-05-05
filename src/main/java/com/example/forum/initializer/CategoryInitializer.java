package com.example.forum.initializer;

import com.example.forum.domain.Category;
import com.example.forum.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createCategoryIfNotExists("daily", "일상");
        createCategoryIfNotExists("qna", "Q&A");
    }

    private void createCategoryIfNotExists(String name, String displayName) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isEmpty()) {
            Category category = Category.builder()
                    .name(name)
                    .displayName(displayName)
                    .build();
            categoryRepository.save(category);
        }
    }
}