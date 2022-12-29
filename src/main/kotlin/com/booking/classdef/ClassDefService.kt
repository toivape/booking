package com.booking.classdef

import com.booking.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ClassDefService(val classDefRepo: ClassDefRepo) {

    fun listClassDefinitions() = classDefRepo.findByOrderByNameAsc()

    fun getClassDefinition(id: Int): ClassDefinition {
        return classDefRepo.findByIdOrNull(id) ?: throw NotFoundException()
    }

    fun saveClassDefinition(form: ClassDefinitionForm): ClassDefinition {
        TODO("NOT IMPLEMENTED")
    }
}
