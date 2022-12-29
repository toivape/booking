package com.booking.classdef

import com.booking.classtype.ClassType
import org.springframework.data.repository.CrudRepository

class ClassDefListItem(
    val id: Int,
    val name: String,
    val classType: ClassType
)

interface ClassDefRepo : CrudRepository<ClassDefinition, Int> {
    fun findByOrderByNameAsc(): List<ClassDefinition>

    // fun listClassDefinitions(): List<ClassDefinitionListItem>
}
