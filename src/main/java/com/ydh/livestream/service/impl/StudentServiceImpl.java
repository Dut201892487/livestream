package com.ydh.livestream.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ydh.livestream.entity.Student;
import com.ydh.livestream.mapper.StudentMapper;
import com.ydh.livestream.result.Result;
import com.ydh.livestream.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.beans.Encoder;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yin Dehao
 * @since 2023-05-26
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private PasswordEncoder encoder;
    private Student getByPhoneNumber(String phoneNumber) {
        if (StrUtil.isEmpty(phoneNumber)) {
            return null;
        }
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getPhoneNumber, phoneNumber);
        return this.getOne(wrapper);
    }
    @Override
    public Result register(Student student) {
        Student stu = this.getByPhoneNumber(student.getPhoneNumber());
        if (stu == null) {
            String encode = encoder.encode(student.getPassword());
            student.setPassword(encode);
            this.save(student);
            return Result.ok(student);
        }
        return Result.fail(null);
    }

    @Override
    public Result login(Student student) {
        Student stu = this.getByPhoneNumber(student.getPhoneNumber());
        String encode = encoder.encode(student.getPassword());
        if (stu!=null && StrUtil.isNotEmpty(student.getPassword())) {
            if (encoder.matches(student.getPassword(), stu.getPassword())) {
                return Result.ok(stu);
            }
        }
        return Result.fail(null);
    }
}
