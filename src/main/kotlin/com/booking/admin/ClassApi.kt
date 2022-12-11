package com.booking.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ClassApi(val classService: ClassService) {

    @GetMapping("/classtypes")
    fun getClassTypes() = classService.getClassTypes()

    @GetMapping("/classdefinitions")
    fun getClassDefinitions() = classService.getClassDefinitions()
}
