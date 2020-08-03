package org.egg.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dataochen
 * @Description
 * @date: 2020/8/3 10:14
 */
@RestController
@RequestMapping("/prize")
@Slf4j
public class PrizeController extends BaseController {

    @PostMapping("/r/{activeNo}")
    public void run(@PathVariable(value = "activeNo") String activeNo) {

    }
}
