package com.booking.admin

import org.springframework.data.repository.CrudRepository

interface ClassTypeRepository : CrudRepository<ClassType, String> {
    fun findByOrderByNameAsc(): List<ClassType>
}

interface ClassDefinitionRepository : CrudRepository<ClassDefinition, Int> {
    fun findByOrderByNameAsc(): List<ClassDefinition>
}
