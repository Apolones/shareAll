package ru.fisenko.shareAll.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fisenko.shareAll.models.Post;
import java.util.List;


@Repository
public interface PostsRepository extends JpaRepository<Post,String> {

    @Query(value = "SELECT id FROM posts_db WHERE id IN :ids", nativeQuery = true)
    List<String> findByInventoryIds(@Param("ids") List<String> id);
}
