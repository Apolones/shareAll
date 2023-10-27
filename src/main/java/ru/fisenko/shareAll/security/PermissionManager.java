package ru.fisenko.shareAll.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.fisenko.shareAll.services.PostsService;

import java.util.Objects;

@Service
public class PermissionManager {
    private PostsService postsService;

    @Autowired
    public PermissionManager(PostsService postsService) {
        this.postsService = postsService;
    }

    public boolean isUserPostOwner(String id, PersonDetails personDetails) {
        return Objects.equals(personDetails.getUsername(), postsService.findOne(id).getPerson().getLogin());
    }

    public boolean isAdmin(PersonDetails personDetails) {
        return personDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

}
