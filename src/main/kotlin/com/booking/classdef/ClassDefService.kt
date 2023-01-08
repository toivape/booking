package com.booking.classdef

import com.booking.NotFoundException
import com.booking.classtype.ClassTypeRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ClassDefService(val classDefRepo: ClassDefRepo, val classTypeRepo: ClassTypeRepo) {

    fun listClassDefs() = classDefRepo.listClassDefs()

    fun getClassDef(id: Int): ClassDefinition = classDefRepo.findByIdOrNull(id) ?: throw NotFoundException()

    fun deleteClassDef(id: Int) = classDefRepo.deleteById(id)

    fun saveClassDef(form: ClassDefinitionForm): ClassDefinition {

        val classType = classTypeRepo.findByIdOrNull(form.classTypeCode)?:throw IllegalArgumentException("Invalid class type code ${form.classTypeCode}")

        TODO("not ready")
    }
}
