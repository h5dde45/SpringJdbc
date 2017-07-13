package com.springjdbc.dao.interfaces;

import com.springjdbc.dao.objects.MP3;

import java.util.List;

public interface MP3Dao {
    int insert(MP3 mp3);
    void insert(List<MP3> mp3);
    int delete(int id);
    int delete(MP3 mp3);
    MP3 getMp3ById(int id);
    List<MP3> getMp3ListByName(String name);
    List<MP3> getMp3ListByAuthor(String author);
    int getMp3Count();

}
