package com.example.forum.domain.category.initializer;

import com.example.forum.domain.category.Category;
import com.example.forum.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Category dailyCategory = Category.builder()
                .name("daily")
                .displayName("일상")
                .build();

        Category qnaCategory = Category.builder()
                .name("qna")
                .displayName("Q&A")
                .build();

        categoryRepository.saveAll(Arrays.asList(dailyCategory, qnaCategory));
    }
}
