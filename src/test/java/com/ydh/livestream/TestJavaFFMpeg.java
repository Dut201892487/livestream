package com.ydh.livestream;

import com.ydh.livestream.Utils.JavaFFMpeg;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author 殷德好
 * @Date 2023/6/10 15:13
 * @Version 1.0
 */
public class TestJavaFFMpeg {
    public static void main(String[] args) {
        JavaFFMpeg javaFFMpeg = new JavaFFMpeg("rtmp://localhost:1935/live/desktop",
                "desktop", "");

        //javaFFMpeg.start();
        System.out.println(javaFFMpeg.getCmd());
        javaFFMpeg = new JavaFFMpeg("rtmp://192.168.31.79:1935/hls/123",
                "desktop", "麦克风 (Realtek(R) Audio)");
        System.out.println(javaFFMpeg.getCmd());
//        javaFFMpeg = new JavaFFMpeg("rtmp://localhost:1935/hls/123",
//                "HD Pro Webcam C920", "麦克风 (Realtek(R) Audio)");
//        System.out.println(javaFFMpeg.getCmd());
        javaFFMpeg.start();
//        javaFFMpeg = new JavaFFMpeg("rtmp://localhost:1935/hls/123", "E:\\livestream\\movie1.mp4");
//        System.out.println(javaFFMpeg.getCmd());
        try {
            while (javaFFMpeg.getPid()==0)
            {
                //System.out.println("进程ID："+javaFFMpeg.getPid());;
            }
            System.out.println("进程ID："+javaFFMpeg.getPid());
            // sleep for 3 minutes and auto close
            Thread.sleep(3*60*1000);
            Runtime.getRuntime().exec("taskkill /F /PID "+javaFFMpeg.getPid());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("结束");
        }

    }
}
