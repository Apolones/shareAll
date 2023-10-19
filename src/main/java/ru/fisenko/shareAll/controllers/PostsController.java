package ru.fisenko.shareAll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fisenko.shareAll.models.Post;
import ru.fisenko.shareAll.services.PostsService;

@Controller
@RequestMapping("/posts")
public class PostsController {
    private final PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") String id, Model model){
        model.addAttribute("post", postsService.findOne(id));
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
    public String savePost(@ModelAttribute("post") Post post){
        postsService.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String editPost(@PathVariable("id") String id, Model model){
        model.addAttribute("post", postsService.findOne(id));
        return "posts/edit";
    }

    @PatchMapping("/{id}")
    public String updatePost(@PathVariable("id") String id, @ModelAttribute("post") Post post){
        postsService.update(post, id);
        return "redirect:/posts";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") String id){
        postsService.delete(id);
        return "redirect:/posts";
    }



}
