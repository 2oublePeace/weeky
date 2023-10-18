package com.emiryanvl.weeky.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/index")
class IndexController {
    @GetMapping
    fun getIndex(): String {
        return "index"
    }
}