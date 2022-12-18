package com.booking.admin

import org.springframework.data.repository.CrudRepository

interface ClassTypeRepository : CrudRepository<ClassType, String> {
    fun findByOrderByNameAsc(): List<ClassType>
}


class ClassDefinitionListItem(
    val id: Int,
    val name: String,
    val classType: ClassType
)


interface ClassDefinitionRepository : CrudRepository<ClassDefinition, Int> {
    fun findByOrderByNameAsc(): List<ClassDefinition>

    fun listClassDefinitions(): List<ClassDefinitionListItem>
}
