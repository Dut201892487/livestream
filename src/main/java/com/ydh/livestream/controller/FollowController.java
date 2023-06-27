package com.ydh.livestream.controller;


import com.ydh.livestream.result.Result;
import com.ydh.livestream.service.FollowService;
import com.ydh.livestream.service.impl.FollowServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/livestream")
public abstract class FollowController {
    @Autowired
    private FollowService followService;
    @PostMapping("{id}/createLive")
    public Result createLive(@PathVariable Long id, String videoDeviceName, String audioDeviceName) {
        return followService.createLive(id,videoDeviceName, audioDeviceName);
    }
    @GetMapping("{id}/getLiveUrl")
    public Result getLiveUrl(@PathVariable Long id) {
        return followService.getLiveUrl(id);
    }


    @PostMapping("{id}/endLive")
    public Result endLive(@PathVariable Long id){
        return followService.endLive(id);
    }

}

