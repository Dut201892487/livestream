package com.ydh.livestream.service;

import com.ydh.livestream.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydh.livestream.result.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yin Dehao
 * @since 2023-05-26
 */
public interface StudentService extends IService<Student> {

    Result register(Student student);

    Result login(Student student);
}
