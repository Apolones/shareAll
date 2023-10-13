package ru.fisenko.shareAll.dao;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;
import ru.fisenko.shareAll.models.Post;
import ru.fisenko.shareAll.dao.PostDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class HashDAO {
    private static JdbcTemplate jdbcTemplate;
    @Autowired
    private PostDAO postDAO;

    private static final int HASHCOUNT=100;

    public HashDAO(JdbcTemplate jdbcTemplate) {
        HashDAO.jdbcTemplate = jdbcTemplate;
    }

    public String getHash() {
        String hash;
       hash = jdbcTemplate.query(
                       "SELECT * FROM hash_db LIMIT 1", (rs, rowNum) -> rs.getString("empty")).stream().findAny().orElse(null);
       if(hash==null){
           newHash();
           System.out.println("null " + hash);
           return getHash();
       }
        System.out.println("not null" + hash);
       jdbcTemplate.update(
               "DELETE FROM hash_db WHERE `empty`=?" , hash);
       return hash;
    }

    private void newHash() {
        String salt = "dasdgksda2345efg32";
        long time = Calendar.getInstance().getTimeInMillis();
        List<String> hash = new ArrayList<>();
        for(int i = 0; i < HASHCOUNT; i++)
            hash.add(DigestUtils.md5Hex(salt + i + time));
        jdbcTemplate.batchUpdate(
                "INSERT INTO hash_db VALUES (?)", new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, hash.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return hash.size();
                    }
                }
        );
    }


}
