package com.booking.classdef

import com.booking.classtype.ClassType
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

class ClassDefListItem(
    val id: Int,
    val name: String,
    val priceCredits: Int?,
    val classType: ClassType
)

interface ClassDefRepo : CrudRepository<ClassDefinition, Int> {
    fun findByOrderByNameAsc(): List<ClassDefinition>

    @Query("SELECT new com.booking.classdef.ClassDefListItem(id, name, priceCredits, classType) FROM ClassDefinition ORDER BY name ASC")
    fun listClassDefs(): List<ClassDefListItem>
}
