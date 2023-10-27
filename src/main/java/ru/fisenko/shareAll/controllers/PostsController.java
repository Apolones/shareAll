package ru.fisenko.shareAll.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.fisenko.shareAll.dto.PostDTO;
import ru.fisenko.shareAll.models.Post;
import ru.fisenko.shareAll.security.PermissionManager;
import ru.fisenko.shareAll.security.PersonDetails;
import ru.fisenko.shareAll.services.PostsService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/posts")
public class PostsController {
    private PostsService postsService;
    private PermissionManager permissionManager;

    @Autowired
    public PostsController(PostsService postsService, PermissionManager permissionManager) {
        this.postsService = postsService;
        this.permissionManager = permissionManager;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") String id, Model model, @AuthenticationPrincipal PersonDetails personDetails) {
        Post post = postsService.findOne(id);
        model.addAttribute("postDTO", convertToPostDTO(post));
        //Check permission to access the 'Delete' and 'Edit' buttons
        if (personDetails != null)
            if (permissionManager.isAdmin(personDetails))
                model.addAttribute("showDelete", true);
            else if (Objects.equals(post.getPerson().getLogin(), personDetails.getUsername())) {
                model.addAttribute("showDelete", true);
                model.addAttribute("showEdit", true);
            }
        return "posts/show";
    }

    @GetMapping()
    public String index(Model model) {
        List<PostDTO> postDTOS = postsService.findAll().stream()
                .map(this::convertToPostDTO)
                .toList();
        model.addAttribute("postsDTO", postDTOS);
        return "posts/index";
    }

    @GetMapping("/new")
    public String newPost(@ModelAttribute("postDTO") PostDTO postDTO) {
        return "posts/new";
    }

    @PostMapping("")
    public String savePost(@ModelAttribute("postDTO") PostDTO postDTO, @AuthenticationPrincipal PersonDetails personDetails) {
        postsService.save(postDTO.getStorageData(), personDetails);
        return "redirect:/posts";
    }


    @GetMapping("/{id}/edit")
    public String editPost(@PathVariable("id") String id, Model model) {
        model.addAttribute("postDTO", convertToPostDTO(postsService.findOne(id)));
        return "posts/edit";
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}")
    public String updatePost(@PathVariable("id") String id, @ModelAttribute("postDTO") PostDTO postDTO, @AuthenticationPrincipal PersonDetails personDetails) {
        if (permissionManager.isUserPostOwner(id, personDetails)) {
            postsService.update(postDTO.getStorageData(), id);
            return "redirect:/posts";
        } else {
            throw new AccessDeniedException("You do not have permission to access this page");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") String id, @AuthenticationPrincipal PersonDetails personDetails) {
        if (permissionManager.isUserPostOwner(id, personDetails) || permissionManager.isAdmin(personDetails)) {
            postsService.delete(id);
            return "redirect:/posts";
        } else {
            throw new AccessDeniedException("You do not have permission to access this page");
        }
    }

    @ResponseBody
    @PostMapping("/url")
    public List<String> isUrlAvailable(@RequestBody List<String> urls) {
        return postsService.findIdByList(urls);
    }

    //TODO авторизацию для микросервисов с использованием csrf токена
  /*
    @ResponseBody
    @GetMapping("/csrf_token")
    public CsrfToken getCsrfToken(CsrfToken token) {
        return token;
    }*/

    private PostDTO convertToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setCreateTime(post.getCreateTime());
        postDTO.setExpiredTime(post.getExpiredTime());
        postDTO.setStorageData(postsService.getStorageData(post.getPathData()));
        return postDTO;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e, Model model) {
        model.addAttribute("error", e);
        return "posts/error";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException e, Model model) {
        model.addAttribute("error", e);
        return "posts/error";
    }
}
