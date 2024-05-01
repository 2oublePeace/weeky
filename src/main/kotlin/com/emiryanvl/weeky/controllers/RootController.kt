package com.emiryanvl.weeky.controllers

import org.springframework.security.config.annotation.web.AnonymousDsl
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/")
class RootController {
    @GetMapping
    fun getRoot(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return if(authentication != null && authentication.principal == ANONYMOUS_USER) {
            "redirect:/signin"
        } else {
            "redirect:/${authentication.name}/home"
        }
    }

    companion object {
        const val ANONYMOUS_USER = "anonymousUser"
    }
}