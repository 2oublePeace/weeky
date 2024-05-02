package com.emiryanvl.webapp.controllers

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient


@Controller
@RequestMapping("/")
class RootController(private val restClient: RestClient) {
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