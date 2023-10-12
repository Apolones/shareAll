package ru.fisenko.shareAll.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.fisenko.shareAll.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class PostDAO {
    private static JdbcTemplate jdbcTemplate;

    public PostDAO(JdbcTemplate jdbcTemplate) {
        PostDAO.jdbcTemplate = jdbcTemplate;
    }

    public List<Post> index() {
        return jdbcTemplate.query(
                "SELECT * FROM posts_db",
                new PostMapper());
    }

    public Post show(int id){
        return jdbcTemplate.query(
                "SELECT * FROM posts_db WHERE id=?",
                new Integer[]{id}, new PostMapper())
                .stream().findAny().orElse(null);
    }

    public void save(Post post){
        Calendar calendar = Calendar.getInstance();
        post.setData(new java.sql.Date(calendar.getTimeInMillis()));
        calendar.add(Calendar.DATE,7);
        post.setExpired(new java.sql.Date(calendar.getTimeInMillis()));
        jdbcTemplate.update(
                "INSERT INTO posts_db(text, data, expired) VALUES (?, ?, ?)",
                post.getS(), post.getData(),post.getExpired());
    }

    public void update(int id, Post post){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,7);
        post.setExpired(new java.sql.Date(calendar.getTimeInMillis()));
        jdbcTemplate.update(
                "UPDATE posts_db SET text = ?, expired = ? WHERE id=?",
                post.getS(), post.getExpired(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update(
                "DELETE FROM posts_db WHERE id =?",
                id);
    }

}
