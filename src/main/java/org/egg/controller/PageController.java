package org.egg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/18 11:29
 */
@Controller
@RequestMapping("/")
public class PageController {
    @RequestMapping("/")
    public String main() {
        return "main";
    }
}
