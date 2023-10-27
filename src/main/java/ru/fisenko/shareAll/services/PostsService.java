package ru.fisenko.shareAll.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.fisenko.shareAll.integration.FileCRUD;
import ru.fisenko.shareAll.integration.RestClient;
import ru.fisenko.shareAll.models.Post;
import ru.fisenko.shareAll.repositories.PostsRepository;
import ru.fisenko.shareAll.security.PersonDetails;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostsService {
    private PostsRepository postsRepository;
    private PersonDetailsService personDetailsService;
    private RestClient restClient;
    private FileCRUD fileCRUD;

    @Autowired
    public PostsService(PostsRepository postsRepository, PersonDetailsService personDetailsService, RestClient restClient, FileCRUD fileCRUD) {
        this.postsRepository = postsRepository;
        this.personDetailsService = personDetailsService;
        this.restClient = restClient;
        this.fileCRUD = fileCRUD;
    }


    public List<Post> findAll() {
        return postsRepository.findAll();
    }

    @Transactional
    public Post findOne(String id) {
        Optional<Post> foundPost = postsRepository.findById(id);
        return foundPost.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @Transactional
    public void save(String storageData, PersonDetails personDetails) {
        Post post = new Post();
        post.setCreateTime(currentDate());
        post.setExpiredTime(currentDate(1));
        post.setId(restClient.sendGetRequest());
        post.setPathData(post.getId());
        if (personDetails != null)
            post.setPerson(personDetailsService.getPersonByUsername(personDetails.getUsername()));
        else post.setPerson(personDetailsService.getPersonByUsername("anonymous"));
        fileCRUD.create(post.getPathData(), storageData);
        postsRepository.save(post);
    }

    @Transactional
    public void update(String storageData, String id) {
        if (postsRepository.findById(id).isPresent())
            fileCRUD.update(postsRepository.findById(id).get().getPathData(), storageData);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    }

    @Transactional
    public void delete(String id) {
        if (postsRepository.findById(id).isPresent()) {
            fileCRUD.delete(postsRepository.findById(id).get().getPathData());
            postsRepository.deleteById(id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
    }

    public String getStorageData(String path) {
        return fileCRUD.read(path);
    }

    @Transactional
    public int deleteExpiredPosts() {
        int count = 0;
        List<Post> all = findAll();
        for (Post p : all
        ) {
            if (p.getExpiredTime().before(currentDate())) {
                delete(p.getId());
                count++;
            }
        }
        return count;
    }

    public List<String> findIdByList(List<String> urls) {
        return postsRepository.findByInventoryIds(urls);
    }

    private Date currentDate() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    private Date currentDate(int addDays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, addDays);
        return c.getTime();
    }
}

