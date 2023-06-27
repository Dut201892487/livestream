package com.ydh.livestream.service;

import com.ydh.livestream.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydh.livestream.result.Result;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yin Dehao
 * @since 2023-05-26
 */
public interface FollowService extends IService<Follow> {

    Result createLive(Long id, HttpSession session);

    Result createLive(Long id, String videoDeviceName, String audioDeviceName);

    Result getLiveUrl(Long id);

    Result endLive(Long id);
}
