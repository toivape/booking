package com.booking.admin

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFoundException : RuntimeException("Requested data not found with id")

@Service
class ClassService(val classTypeRepo: ClassTypeRepo, val classDefinitionRepo: ClassDefinitionRepo) {

    fun listClassTypes() = classTypeRepo.findByOrderByNameAsc()

    fun saveClassType(code: String, name: String) =
        classTypeRepo.save(
            ClassType(
                code = code,
                name = name
            )
        )

    fun deleteClassType(code: String) = classTypeRepo.deleteById(code)

    fun getClassType(code: String): ClassType = classTypeRepo.findByIdOrNull(code) ?: throw NotFoundException()

    fun listClassDefinitions() = classDefinitionRepo.findByOrderByNameAsc()

    fun getClassDefinition(id: Int): ClassDefinition {
        return classDefinitionRepo.findByIdOrNull(id) ?: throw NotFoundException()
    }

    fun saveClassDefinition(form: ClassDefinitionForm): ClassDefinition {
        TODO("NOT IMPLEMENTED")
    }
}
