package ru.fisenko.shareAll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fisenko.shareAll.dao.PostDAO;
import ru.fisenko.shareAll.models.Post;

@Controller
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostDAO postDAO;

    @GetMapping("/{id}")
    public String show(@PathVariable("id") String id, Model model){
        model.addAttribute("post", postDAO.show(id));
        return "posts/show";
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("posts", postDAO.index());
        return "posts/index";
    }
    @GetMapping("/new")
    public String newPost(@ModelAttribute("post") Post post){
        return "posts/new";
    }

    @PostMapping("")
    public String savePost(@ModelAttribute("post") Post post){
        postDAO.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String editPost(@PathVariable("id") String id, Model model){
        model.addAttribute("post", postDAO.show(id));
        return "posts/edit";
    }

    @PatchMapping("/{id}")
    public String updatePost(@PathVariable("id") String id, @ModelAttribute("post") Post post){
        postDAO.update(id, post);
        return "redirect:/posts";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") String id){
        postDAO.delete(id);
        return "redirect:/posts";
    }



}
