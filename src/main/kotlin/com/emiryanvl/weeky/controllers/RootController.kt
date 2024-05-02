package com.emiryanvl.weeky.controllers

import com.emiryanvl.weeky.dto.ArticleDto
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient
import org.springframework.web.client.body


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