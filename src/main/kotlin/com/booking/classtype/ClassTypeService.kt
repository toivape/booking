package com.booking.classtype

import arrow.core.Either
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

    fun getClassType(code: String): Either<NotFoundException, ClassType> {
        val classType = classTypeRepo.findByIdOrNull(code)
        return if (classType != null) Either.Right(classType) else Either.Left(NotFoundException("Class type $code not found"))
    }

    fun getClassTypeOrThrow(code: String): ClassType = classTypeRepo.findByIdOrNull(code) ?: throw NotFoundException()
}
