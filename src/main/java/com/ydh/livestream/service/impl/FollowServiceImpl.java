package com.ydh.livestream.service.impl;

import cn.hutool.core.lang.UUID;
import com.ydh.livestream.Utils.JavaFFMpeg;
import com.ydh.livestream.entity.Follow;
import com.ydh.livestream.mapper.FollowMapper;
import com.ydh.livestream.result.Result;
import com.ydh.livestream.service.FollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.ydh.livestream.Utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yin Dehao
 * @since 2023-05-26
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Deprecated
    public Result createLive(Long id, HttpSession session) {
        // 1. 生成直播间
        String url = "rtmp://localhost:1935/live/" + id;
        // 2. 启动ffmpeg
        JavaFFMpeg javaFFMpeg = new JavaFFMpeg(url, "e:\\livestream\\orange.mp4");
        javaFFMpeg.start();
        // 2. 将直播间地址存入session
        session.setAttribute("live_url",url);
        // 3. 将直播间地址存入redis
        stringRedisTemplate.opsForValue().set(LIVE_URL_PREFIX + id,url, LIVE_URL_EXPIRE, TimeUnit.MINUTES);
        log.debug("直播间地址："+url);
        String token = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForHash().put(LIVE_TOKEN, id, token);
        String oldToken = stringRedisTemplate.opsForHash().get(LIVE_TOKEN, id).toString();
        stringRedisTemplate.opsForValue().set(LIVE_TOKEN + id, token, LIVE_URL_EXPIRE, TimeUnit.MINUTES);
        return Result.ok(url);
    }

    /**
     * 创建直播
     * @param id
     * @param videoDeviceName 视频设备名称，桌面/摄像头
     * @param audioDeviceName 音频设备名称，麦克风
     * @return
     */
    @Override
    public Result createLive(Long id, String videoDeviceName, String audioDeviceName) {
        // 1. 查询Redis中是否有正在直播的课程
        Object o = stringRedisTemplate.opsForHash().get(LIVE_TOKEN, id.toString());
        if (o != null){
            return Result.fail("您已经有正在直播的课程，不能同时直播两门课程");
        }
        // 2. 创建直播课程token
        String token = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForHash().put(LIVE_TOKEN, id.toString(), token);
        // 3. 生成直播间地址
        String url = "rtmp://localhost:1935/hls/" + token;
        // 4. 启动ffmpeg，创建直播线程
        JavaFFMpeg javaFFMpeg = new JavaFFMpeg(url, videoDeviceName, audioDeviceName);
        stringRedisTemplate.opsForHash().put(LIVE_PID, id.toString(), javaFFMpeg.getPid()+"");
        javaFFMpeg.start();
        return Result.ok(url);
    }
    @Override
    public Result getLiveUrl(Long id) {
        Object token = stringRedisTemplate.opsForHash().get(LIVE_TOKEN, id.toString());
        if (token != null) {
            return Result.ok("http://192.168.31.79:8000/hls/" + token +".m3u8");
        }
        return Result.fail(null);
    }

    @Override
    public Result endLive(Long id) {
        Object o = stringRedisTemplate.opsForHash().get(LIVE_PID, id.toString());
        if (o != null) {
            String pid = o.toString();
            stringRedisTemplate.opsForHash().delete(LIVE_PID, id.toString());
            stringRedisTemplate.opsForHash().delete(LIVE_TOKEN, id.toString());
            try {
                Runtime.getRuntime().exec("taskkill /F /PID "+pid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Result.ok("成功结束直播");
    }
}
