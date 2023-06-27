package com.ydh.livestream;

import com.ydh.livestream.entity.Student;
import com.ydh.livestream.service.StudentService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author 殷德好
 * @Date 2023/5/26 21:04
 * @Version 1.0
 */
@SpringBootTest
public class TestConnection {
    @Autowired
    private StudentService studentService;
    @Test
    void addStu() {
        Student student = new Student();
        student.setName("刘承东");
        student.setPassword("123456");
        student.setPhoneNumber("12345678910");
        studentService.save(student);
        val list = studentService.list();
        list.forEach(System.out::println);
    }
}
