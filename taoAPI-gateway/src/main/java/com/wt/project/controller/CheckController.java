package com.wt.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xcx
 * @date
 */
@RestController
@RequestMapping("/check")
public class CheckController {

    @GetMapping("/check")
    public String check(){
        return "success";
    }
}
