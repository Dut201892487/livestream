package com.ydh.livestream.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Yin Dehao
 * @since 2023-05-26
 */
@Data
public class Follow implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long studentId;

    private Long teacherId;


}
