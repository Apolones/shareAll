package ru.fisenko.shareAll.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final PostsRepository postsRepository;
    private final UrlService urlService;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public PostsService(PostsRepository postsRepository, UrlService urlService, PersonDetailsService personDetailsService) {
        this.postsRepository = postsRepository;
        this.urlService = urlService;
        this.personDetailsService = personDetailsService;
    }


    public List<Post> findAll(){
         return postsRepository.findAll();
    }

    @Transactional
    public Post findOne(String id){
        Optional <Post> foundPost = postsRepository.findById(id);
         return foundPost.orElse(null);
    }

    @Transactional
    public void save(Post post, PersonDetails personDetails){
        post.setData(currentDate());
        post.setExpired(currentDate(1));
        post.setId(urlService.getUrl());
        if(personDetails!=null) post.setPerson(personDetailsService.getPersonByUsername(personDetails.getUsername()));
        else post.setPerson(personDetailsService.getPersonByUsername("ADMIN"));
        postsRepository.save(post);
    }

    @Transactional
    public void update(Post post, String id){
        postsRepository.findById(id).ifPresent(postDb -> postDb.setS(post.getS()));
    }

    @Transactional
    public void delete(String id){
        postsRepository.deleteById(id);
    }

    @Transactional
    public int deleteExpiredPosts(){
        int count=0;
        List<Post> all= findAll();
        for (Post p: all
             ) {
            if(p.getExpired().before(currentDate())){
                postsRepository.deleteById(p.getId());
                count++;
            }
        }
        return count;
    }

    private Date currentDate(){
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }
    private Date currentDate(int addDays){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, addDays);
        return c.getTime();
    }
}

