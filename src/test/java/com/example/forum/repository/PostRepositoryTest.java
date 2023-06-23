package com.example.forum.repository;

import com.example.forum.domain.Category;
import com.example.forum.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testFindByCategory() {
        Post postA = Post.builder()
                .title("test")
                .build();

        Category category = Category.builder()
                .name("test")
                .build();

        postA.setCategory(category);

        categoryRepository.save(category);
        postRepository.save(postA);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> findPosts = postRepository.findByCategory(category, pageable);

        assertThat(postA.getTitle()).isEqualTo(findPosts.getContent().get(0).getTitle());
    }

    @Test
    void testFindByTitleContaining() {
        Post postA = Post.builder()
                .title("testA")
                .build();

        Post postB = Post.builder()
                .title("testB")
                .build();

        List<Post> posts = Arrays.asList(postA, postB);
        List<Post> savedPosts = postRepository.saveAll(posts);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> findPosts = postRepository.findByTitleContaining("test", pageable);

        assertThat(findPosts.getTotalElements()).isEqualTo(savedPosts.size());
    }
}
