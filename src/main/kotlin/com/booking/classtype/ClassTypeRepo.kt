package com.booking.classtype

import org.springframework.data.repository.CrudRepository

interface ClassTypeRepo : CrudRepository<ClassType, String> {
    fun findByOrderByNameAsc(): List<ClassType>
}

