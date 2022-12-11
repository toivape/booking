package com.booking.admin

import org.springframework.stereotype.Service

@Service
class ClassService(val classTypeRepo: ClassTypeRepository, val classDefinitionRepo: ClassDefinitionRepository) {

    fun getClassTypes() = classTypeRepo.findByOrderByNameAsc()

    fun getClassDefinitions() = classDefinitionRepo.findByOrderByNameAsc()
}
