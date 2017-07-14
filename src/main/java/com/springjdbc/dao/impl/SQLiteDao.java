package com.springjdbc.dao.impl;

import com.springjdbc.dao.interfaces.MP3Dao;
import com.springjdbc.dao.objects.Author;
import com.springjdbc.dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SQLiteDao implements MP3Dao {

    private static final String mp3Table = "mp3";
    private static final String mp3View = "mp3_view";
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertMP3(MP3 mp3) {
        System.out.println(TransactionSynchronizationManager
                .isActualTransactionActive());
        int author_id=insertAuthor(mp3.getAuthor());

        String sqlInsMP3 = "insert into mp3 (author_id, name) values (:authorId, :mp3Name)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mp3Name", mp3.getName());
        params.addValue("authorId", author_id);

        return jdbcTemplate.update(sqlInsMP3, params);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertAuthor(Author author) {
        System.out.println(TransactionSynchronizationManager
                .isActualTransactionActive());
        String sqlInsAuth = "insert into author (name) values (:authorName)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("authorName", author.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sqlInsAuth, params, keyHolder);
        return keyHolder.getKey().intValue();

    }

    @Override
    public void insert(List<MP3> mp3List) {
        for (MP3 mp3 : mp3List) {
            insertMP3(mp3);
        }
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM mp3 WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbcTemplate.update(sql, params);
    }

    @Override
    public int delete(MP3 mp3) {
        return delete(mp3.getId());
    }

    @Override
    public MP3 getMp3ById(int id) {
        String sql = "select * from "+mp3View+" WHERE mp3_id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbcTemplate.queryForObject(sql, params, new Mp3RowMapper());
    }

    @Override
    public List<MP3> getMp3ListByName(String name) {
        String sql = "select * from "+ mp3View+" WHERE LOWER (mp3_name) LIKE :name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", "%" + name.toLowerCase() + "%");

        return jdbcTemplate.query(sql, params, new Mp3RowMapper());
    }

    @Override
    public List<MP3> getMp3ListByAuthor(String author) {
        String sql = "select * from "+ mp3View+" WHERE LOWER (author_name) LIKE :author";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", "%" + author.toLowerCase() + "%");

        return jdbcTemplate.query(sql, params, new Mp3RowMapper());
    }

    @Override
    public int getMp3Count() {
        String sql = "select count(*) from "+mp3Table;
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, Integer.class);
    }

    private static final class Mp3RowMapper implements RowMapper<MP3> {
        @Override
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {

            Author author=new Author();
            author.setId(resultSet.getInt("author_id"));
            author.setName(resultSet.getString("author_name"));

            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("mp3_id"));
            mp3.setName(resultSet.getString("mp3_name"));
            mp3.setAuthor(author);
            return mp3;
        }
    }
}
