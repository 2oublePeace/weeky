package com.emiryanvl.weeky.controllers

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