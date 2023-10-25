package ru.fisenko.shareAll.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fisenko.shareAll.integration.RestClient;
import ru.fisenko.shareAll.models.Person;
import ru.fisenko.shareAll.models.Post;
import ru.fisenko.shareAll.repositories.PostsRepository;
import ru.fisenko.shareAll.security.PersonDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @Mock
    private PostsRepository postsRepository;
    @Mock
    private PersonDetailsService personDetailsService;
    @Mock
    private RestClient restClient;

    private PostsService postsService;


    @BeforeEach
    void setUp() {
        postsService = new PostsService(postsRepository, personDetailsService, restClient);
    }

    @Test
    public void findAll_ReturnsListOfPosts() {
        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(postsRepository.findAll()).thenReturn(posts);

        List<Post> result = postsService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findOne_UserFound_ReturnUser() {
        String id = "postId";
        Post post = new Post();
        when(postsRepository.findById(id)).thenReturn(Optional.of(post));

        Post result = postsService.findOne(id);

        assertNotNull(result);
        assertEquals(result, post);
    }

    @Test
    void findOne_UserNotFound_ReturnNull() {
        String id = "nonExistentId";
        when(postsRepository.findById(id)).thenReturn(Optional.empty());

        Post result = postsService.findOne(id);

        assertNull(result);
    }

    @Test
    void save() {
        Post post = new Post();
        Person person = new Person();
        person.setLogin("testUser");
        person.setPassword("testPassword");
        PersonDetails personDetails =new PersonDetails(person);
        when(restClient.sendGetRequest()).thenReturn("testId");
        when(personDetailsService.getPersonByUsername("testUser")).thenReturn(person);

        postsService.save(post, personDetails);

        verify(postsRepository, times(1)).save(post);
        assertNotNull(post.getData());
        assertNotNull(post.getExpired());
        assertEquals("testId", post.getId());
        assertEquals("testUser", post.getPerson().getLogin());

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteExpiredPosts() {
    }

    @Test
    void findIdByList() {
    }
}