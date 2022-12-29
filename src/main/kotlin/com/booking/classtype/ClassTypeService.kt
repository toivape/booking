package com.booking.classtype

import com.booking.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ClassTypeService(val classTypeRepo: ClassTypeRepo) {

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
}
