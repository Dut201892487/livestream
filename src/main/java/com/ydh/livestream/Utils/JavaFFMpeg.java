package com.ydh.livestream.Utils;

import cn.hutool.core.util.StrUtil;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.ydh.livestream.service.impl.FollowServiceImpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * @Author 殷德好
 * @Date 2023/6/10 10:50
 * @Version 1.0
 */
public class JavaFFMpeg extends Thread {
    private String cmd;
    private String url;
    private String fileName;
    private long pid;
    public JavaFFMpeg(String url, String fileName) { // 使用视频文件创建直播
        this.url = url;
        this.fileName = fileName;
        //cmd = "ffmpeg -re -i " + fileName + " -c copy -f flv " + url;
        this.cmd = "ffmpeg -re -i "+fileName+" -vcodec libx264 -acodec aac -f flv "+url;
        System.out.println(cmd);
    }
    public JavaFFMpeg(String url, String videoDeviceName, String audioDeviceName) { // 使用摄像头创建直播
        this.url = url;
        if (videoDeviceName.equals("desktop")){
            this.cmd = "ffmpeg -f gdigrab -framerate 30 -i desktop -c:v libx264 -preset ultrafast -tune " +
                    "zerolatency -pix_fmt yuv420p -c:a aac -b:a 128k -f flv "+url;
            if (StrUtil.isNotEmpty(audioDeviceName)){
                this.cmd = "ffmpeg -f gdigrab -framerate 30 -i desktop -f dshow -i audio=\"" + audioDeviceName + "\" " +
                        "-c:v libx264 -preset ultrafast -tune zerolatency " +
                        "-pix_fmt yuv420p -c:a aac -b:a 128k -f flv "+url;
            }
        }else {
            // ffmpeg -f dshow -i video="HD Pro Webcam C920":audio="麦克风 (Realtek(R) Audio)" -c:v libx264 -preset ultrafast
            // -tune zerolatency -pix_fmt yuv420p -c:a aac -b:a 128k -f flv rtmp://localhost:1935/live/123
            this.cmd = "ffmpeg -f dshow -i video=\""+videoDeviceName+"\" -c:v libx264 " +
                    "-preset ultrafast -tune zerolatency -pix_fmt yuv420p -c:a aac -b:a 128k -f flv "+url;
            if (StrUtil.isNotEmpty(audioDeviceName)){
                this.cmd = "ffmpeg -f dshow -i video=\""+videoDeviceName+"\":audio=\""+audioDeviceName+"\" -c:v libx264 " +
                        "-preset ultrafast -tune zerolatency -pix_fmt yuv420p -c:a aac -b:a 128k -f flv "+url;
            }
        }
        System.out.println(cmd);
    }
    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    @Override
    public void run(){
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            this.pid = getProcessID(process);
            BufferedInputStream in = new BufferedInputStream(process.getErrorStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                System.out.println(lineStr);
            }
            br.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getProcessID(Process p)
    {
        long result = -1;
        try
        {
            //for windows
            if (p.getClass().getName().equals("java.lang.Win32Process") ||
                    p.getClass().getName().equals("java.lang.ProcessImpl"))
            {
                Field f = p.getClass().getDeclaredField("handle");
                f.setAccessible(true);
                long handl = f.getLong(p);
                Kernel32 kernel = Kernel32.INSTANCE;
                WinNT.HANDLE hand = new WinNT.HANDLE();
                hand.setPointer(Pointer.createConstant(handl));
                result = kernel.GetProcessId(hand);
                f.setAccessible(false);
            }
            //for unix based operating systems
            else if (p.getClass().getName().equals("java.lang.UNIXProcess"))
            {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                result = f.getLong(p);
                f.setAccessible(false);
            }
        }
        catch(Exception ex)
        {
            result = -1;
        }
        return result;
    }

    public String getCmd() {
        return cmd;
    }
}
