package com.ydh.livestream.controller;


import com.ydh.livestream.entity.Student;
import com.ydh.livestream.result.Result;
import com.ydh.livestream.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yin Dehao
 * @since 2023-05-26
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @PostMapping("login")
    public Result login(@RequestBody Student student) {
        return studentService.login(student);
    }
    @PostMapping("register")
    public Result register(@RequestBody Student student) {
        return studentService.register(student);
    }
}

