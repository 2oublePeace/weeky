package com.emiryanvl.weeky.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/library")
class LibraryController {
    @GetMapping
    fun getLibrary(): String = "library"


}