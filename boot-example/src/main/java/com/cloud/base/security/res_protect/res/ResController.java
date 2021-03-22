package com.cloud.base.security.res_protect.res;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lh0811
 * @date 2021/3/22
 */
@RestController
@RequestMapping("/res")
public class ResController {

    @GetMapping("/res1")
    public String res1() {
        return "res1 success";
    }

    @GetMapping("/res2")
    public String res2() {
        return "res2 success";
    }
}
