package com.demo.kafka.controller;

/**
 * 功能描述：
 *
 * @author: lijie
 * @date: 2021/7/16 13:59
 * @version: V1.0
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class KafkaController {

    @GetMapping("/sendMsg")
    public String sendMsg() {
        log.info("接口调用成功");
        return "success";
    }
}
