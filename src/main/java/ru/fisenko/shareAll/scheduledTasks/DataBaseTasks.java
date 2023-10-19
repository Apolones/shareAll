package ru.fisenko.shareAll.scheduledTasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.fisenko.shareAll.services.PostsService;

@EnableScheduling
@Component
public class DataBaseTasks {
    PostsService postsService;

    @Autowired
    public DataBaseTasks(PostsService postsService) {
        this.postsService = postsService;
    }

    @Scheduled(cron = "${cron.delete.posts}")
    public void deleteExpiredPosts()
    {
        int count = postsService.deleteExpiredPosts();
        System.out.println("Delete " + count + " posts (time expired)");
    }

}
