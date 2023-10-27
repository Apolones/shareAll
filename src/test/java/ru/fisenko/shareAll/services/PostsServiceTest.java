package ru.fisenko.shareAll.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.fisenko.shareAll.integration.FileCRUD;
import ru.fisenko.shareAll.integration.RestClient;
import ru.fisenko.shareAll.models.Person;
import ru.fisenko.shareAll.models.Post;
import ru.fisenko.shareAll.repositories.PostsRepository;
import ru.fisenko.shareAll.security.PersonDetails;

import java.util.*;

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
    @Mock
    private FileCRUD fileCRUD;

    private PostsService postsService;


    @BeforeEach
    void setUp() {
        postsService = new PostsService(postsRepository, personDetailsService, restClient, fileCRUD);
    }

    @Test
    void findAll_ReturnsListOfPosts() {
        List<Post> posts = Arrays.asList(new Post(), new Post());
        when(postsRepository.findAll()).thenReturn(posts);

        List<Post> result = postsService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findOne_PostFound_ReturnPost() {
        String id = "postId";
        Post post = new Post();
        when(postsRepository.findById(id)).thenReturn(Optional.of(post));

        Post result = postsService.findOne(id);

        assertNotNull(result);
        assertEquals(result, post);
    }

    @Test
    void findOne_PostNotFound_ThrowException() {
        String id = "nonExistentId";
        when(postsRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->postsService.findOne(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());
    }

    @Test
    void save_WithPersonDetails() {
        String storageData = "Sample storage data";
        Person person = new Person();
        person.setLogin("testUser");
        PersonDetails personDetails = new PersonDetails(person);
        when(restClient.sendGetRequest()).thenReturn("generatedId");
        when(personDetailsService.getPersonByUsername("testUser")).thenReturn(new Person());

        postsService.save(storageData, personDetails);

        verify(fileCRUD, times(1)).create("generatedId", storageData);
        verify(postsRepository, times(1)).save(any(Post.class));
    }

    @Test
    void save_WithoutPersonDetails() {
        String storageData = "Sample storage data";
        PersonDetails personDetails = null;
        when(restClient.sendGetRequest()).thenReturn("generatedId");
        when(personDetailsService.getPersonByUsername("anonymous")).thenReturn(new Person());

        postsService.save(storageData, personDetails);

        verify(fileCRUD, times(1)).create("generatedId", storageData);
        verify(postsRepository, times(1)).save(any(Post.class));
    }

    @Test
    void update_PostFound() {
        String id = "postId";
        String storageData = "Sample storage data";
        Post post = new Post();
        post.setPathData("path");
        when(postsRepository.findById(id)).thenReturn(Optional.of(post));

        postsService.update(storageData, id);

        verify(fileCRUD, times(1)).update("path", storageData);
    }

    @Test
    void update_PostNotFound_ThrowException() {
        String id = "nonExistentId";
        String storageData = "Sample storage data";
        when(postsRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> postsService.update(storageData, id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());
    }

    @Test
    void delete_PostFound() {
        String postId = "postId";
        Post existingPost = new Post();
        when(postsRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        postsService.delete(postId);

        verify(fileCRUD, times(1)).delete(existingPost.getPathData());
        verify(postsRepository, times(1)).deleteById(postId);
    }

    @Test
    void delete_PostNotFound_ThrowException() {
        String postId = "nonExistentId";
        Post existingPost = new Post();
        when(postsRepository.findById(postId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> postsService.delete(postId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post not found", exception.getReason());
    }

    @Test
    void deleteExpiredPosts_PostsFound_ReturnCountDeletedPosts() {
        List<Post> posts = new ArrayList<>();
        Date currentDate = new Date();
        Date expiredDate = new Date(currentDate.getTime() - 86400000);
        Date NotExpiredDate = new Date(currentDate.getTime() + 86400000);
        Post post1 = new Post();
        post1.setId("post1");
        post1.setExpiredTime(expiredDate);
        posts.add(post1);
        Post post2 = new Post();
        post2.setId("post2");
        post2.setExpiredTime(NotExpiredDate);
        posts.add(post2);
        when(postsRepository.findAll()).thenReturn(posts);
        when(postsRepository.findById(anyString())).thenReturn(Optional.of(new Post()));


        int result = postsService.deleteExpiredPosts();

        assertEquals(1, result);
        verify(postsRepository, times(1)).findAll();
        verify(fileCRUD, times(1)).delete(post1.getPathData());
    }

    @Test
    public void findIdByList() {
        List<String> urls = Arrays.asList("url1", "url2", "url3");
        List<String> expectedResult = Arrays.asList("id1", "id2", "id3");
        when(postsRepository.findByInventoryIds(urls)).thenReturn(expectedResult);

        List<String> result = postsService.findIdByList(urls);

        assertEquals(expectedResult, result);
        verify(postsRepository, times(1)).findByInventoryIds(urls);
    }
}