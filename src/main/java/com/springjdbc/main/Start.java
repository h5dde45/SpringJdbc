package com.springjdbc.main;

import com.springjdbc.dao.impl.SQLiteDao;
import com.springjdbc.dao.objects.MP3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Start {
    public static void main(String[] args) {
        MP3 mp3=new MP3();
        mp3.setName("n1");
        mp3.setAuthor("a1");
        MP3 mp2=new MP3();
        mp2.setName("n3");
        mp2.setAuthor("a3");
        mp2.setId(3);
        List<MP3> mp3List=new ArrayList<>();
        mp3List.add(mp3);
        mp3List.add(mp2);

        ApplicationContext context=new ClassPathXmlApplicationContext("context.xml");
        SQLiteDao sqLiteDao=(SQLiteDao) context.getBean("SQLiteDao");

        System.out.println(sqLiteDao.insert(mp3));

//        System.out.println(sqLiteDao.getMp3ListByName("1"));
//        System.out.println(sqLiteDao.getMp3ListByAuthor("a3"));
//        System.out.println(sqLiteDao.getMp3Count());

    }
}
