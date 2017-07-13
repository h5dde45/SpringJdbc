package com.springjdbc.dao.impl;

import com.springjdbc.dao.interfaces.MP3Dao;
import com.springjdbc.dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SQLiteDao implements MP3Dao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public int insert(MP3 mp3) {
        String sql = "insert into mp3 (name, author) values(:name,:author)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name",mp3.getName());
        params.addValue("author",mp3.getAuthor());

        KeyHolder keyHolder=new GeneratedKeyHolder();

        jdbcTemplate.update(sql,params,keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public void insert(List<MP3> mp3List) {
        for (MP3 mp3 : mp3List) {
            insert(mp3);
        }
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM mp3 WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id",id);

        return jdbcTemplate.update(sql,params);
    }

    @Override
    public int delete(MP3 mp3) {
        return delete(mp3.getId());
    }

    @Override
    public MP3 getMp3ById(int id) {
        String sql="select * from mp3 WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id",id);

        return jdbcTemplate.queryForObject(sql, params, new Mp3RowMapper());
    }

    @Override
    public List<MP3> getMp3ListByName(String name) {
        String sql="select * from mp3 WHERE LOWER (name) LIKE :name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name","%"+name.toLowerCase()+"%");

        return jdbcTemplate.query(sql, params, new Mp3RowMapper());
    }

    @Override
    public List<MP3> getMp3ListByAuthor(String author) {
        String sql="select * from mp3 WHERE LOWER (author) LIKE :author";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author","%"+author.toLowerCase()+"%");

        return jdbcTemplate.query(sql, params, new Mp3RowMapper());
    }

    @Override
    public int getMp3Count() {
        String sql="select count(*) from mp3";
        return jdbcTemplate.getJdbcOperations().queryForObject(sql,Integer.class);
    }

    private static final class Mp3RowMapper implements RowMapper<MP3>{
        @Override
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {
            MP3 mp3=new MP3();
            mp3.setId(resultSet.getInt("id"));
            mp3.setAuthor(resultSet.getString("author"));
            mp3.setName(resultSet.getString("name"));
            return mp3;
        }
    }
}
