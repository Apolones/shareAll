package ru.fisenko.shareAll.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.fisenko.shareAll.models.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();

      //      post.setId(rs.getInt("id"));
            post.setS(rs.getString("text"));
            post.setData(rs.getDate("data"));
            post.setExpired(rs.getDate("expired"));
            post.setId(rs.getString("id"));

        return post;
    }
}
