package com.booking.admin

import org.springframework.stereotype.Service

@Service
class ClassService(val classTypeRepo: ClassTypeRepo, val classDefinitionRepo: ClassDefinitionRepo) {

    fun getClassTypes() = classTypeRepo.findByOrderByNameAsc()

    fun getClassDefinitions() = classDefinitionRepo.findByOrderByNameAsc()
}
