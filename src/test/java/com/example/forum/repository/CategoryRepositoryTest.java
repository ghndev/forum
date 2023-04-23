package com.example.forum.repository;

import com.example.forum.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("test")
                .build();

        categoryRepository.save(category);
    }

    @Test
    void testFindByName() {
        Category savedCategory = categoryRepository.findByName(category.getName()).get();

        assertThat(category).isEqualTo(savedCategory);
    }
}
