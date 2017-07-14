package com.springjdbc.main;

import com.springjdbc.dao.impl.SQLiteDao;
import com.springjdbc.dao.objects.Author;
import com.springjdbc.dao.objects.MP3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
    public static void main(String[] args) {

        Author author=new Author();
        author.setName("a3");

        MP3 mp3=new MP3();
        mp3.setName("n1");
        mp3.setAuthor(author);

        ApplicationContext context=new ClassPathXmlApplicationContext("context.xml");
        SQLiteDao sqLiteDao=(SQLiteDao) context.getBean("SQLiteDao");

        System.out.println(sqLiteDao.insertMP3(mp3));

    }
}
