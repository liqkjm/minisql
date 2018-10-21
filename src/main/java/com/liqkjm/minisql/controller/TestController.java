package com.liqkjm.minisql.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Describe TODO
 * @Author Liqkjm
 * @Date 2018/10/21 11:24
 */
@RestController
public class TestController {
    @GetMapping("/")
    public String hello() {
        return "Hello Mini sql Test";
    }
}
