package ru.fisenko.shareAll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fisenko.shareAll.models.Post;
import ru.fisenko.shareAll.security.PermissionManager;
import ru.fisenko.shareAll.security.PersonDetails;
import ru.fisenko.shareAll.services.PostsService;

@Controller
@RequestMapping("/posts")
public class PostsController {
    private final PostsService postsService;
    private final PermissionManager permissionManager;

    @Autowired
    public PostsController(PostsService postsService, PermissionManager permissionManager) {
        this.postsService = postsService;
        this.permissionManager = permissionManager;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") String id, Model model, @AuthenticationPrincipal PersonDetails personDetails){
        model.addAttribute("post", postsService.findOne(id));
        if(personDetails!=null){
            if (permissionManager.isAdmin(personDetails))
                model.addAttribute("username", "ADMIN");
            else model.addAttribute("username", personDetails.getUsername());
        } else model.addAttribute("username", "*");
        return "posts/show";
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("posts", postsService.findAll());
        return "posts/index";
    }
    @GetMapping("/new")
    public String newPost(@ModelAttribute("post") Post post){
        return "posts/new";
    }

    @PostMapping("")
    public String savePost(@ModelAttribute("post") Post post, @AuthenticationPrincipal PersonDetails personDetails){
        postsService.save(post, personDetails);
        return "redirect:/posts";
    }


    @GetMapping("/{id}/edit")
    public String editPost(@PathVariable("id") String id, Model model){
        model.addAttribute("post", postsService.findOne(id));
        return "posts/edit";
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public String updatePost(@PathVariable("id") String id, @ModelAttribute("post") Post post, @AuthenticationPrincipal PersonDetails personDetails){
        if(permissionManager.isUserPostOwner(id, personDetails)){
            postsService.update(post, id);
            return "redirect:/posts";
        } else {
            throw new AccessDeniedException("You do not have permission to access this page");
        }
    }
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") String id, @AuthenticationPrincipal PersonDetails personDetails){
        if(permissionManager.isUserPostOwner(id, personDetails) || permissionManager.isAdmin(personDetails)){
            postsService.delete(id);
            return "redirect:/posts";
        } else{
            throw new AccessDeniedException("You do not have permission to access this page");
        }
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e) {

        return "posts/error";
    }
}
