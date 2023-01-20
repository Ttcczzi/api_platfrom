package com.wt.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xcx
 * @date
 */
@RestController
@RequestMapping("/t/test")
public class Test {
    @GetMapping
    public String re(String te){
        return "ttest";
    }
}
