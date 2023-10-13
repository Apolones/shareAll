package ru.fisenko.shareAll.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.fisenko.shareAll.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Component
public class PostDAO {
    private static JdbcTemplate jdbcTemplate;
    @Autowired
    private HashDAO hashDAO;

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
                "INSERT INTO posts_db(text, data, expired, url) VALUES (?, ?, ?, ?)",
                post.getS(), post.getData(),post.getExpired(),hashDAO.getHash());

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

    public List<String> hashCheck (List<String> url){
        String inSql = String.join(",", Collections.nCopies(url.size(), "?"));
       return jdbcTemplate.query(String.format("SELECT url FROM posts_db WHERE 'url' IN (%s)", inSql),
               url.toArray(),
               ResultSet::getString);

    }

}
