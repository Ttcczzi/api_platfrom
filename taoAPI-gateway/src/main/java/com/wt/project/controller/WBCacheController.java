package com.wt.project.controller;

import com.wt.project.filters.APIGlobalFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author xcx
 * @date
 */
@RestController
@RequestMapping("/wbset")
@Slf4j
public class WBCacheController {
    @Autowired
    private APIGlobalFilter filter;

    @GetMapping("/reload")
    public String reload(){
        filter.loadWBList();
        Set<String> wbSet = filter.getWBSet();
        StringBuilder info = new StringBuilder("黑白名单已经重新加载: ");
        info.append(wbSet.toString());
        return info.toString();
    }
}
