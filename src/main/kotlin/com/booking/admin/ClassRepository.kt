package com.booking.admin

import org.springframework.data.repository.CrudRepository

interface ClassTypeRepo : CrudRepository<ClassType, String> {
    fun findByOrderByNameAsc(): List<ClassType>
}

class ClassDefinitionListItem(
    val id: Int,
    val name: String,
    val classType: ClassType
)

interface ClassDefinitionRepo : CrudRepository<ClassDefinition, Int> {
    fun findByOrderByNameAsc(): List<ClassDefinition>

    // fun listClassDefinitions(): List<ClassDefinitionListItem>
}
