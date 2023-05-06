package com.example.forum.repository;

import com.example.forum.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testFindByName() {
        Category category = Category.builder()
                .name("test")
                .build();

        categoryRepository.save(category);

        Category savedCategory = categoryRepository.findByName(category.getName()).get();

        assertThat(category).isEqualTo(savedCategory);
    }
}
