package com.booking.classdef

import arrow.core.Either
import arrow.core.flatMap
import com.booking.NotFoundException
import com.booking.classtype.ClassTypeService
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class ClassDefService(val classDefRepo: ClassDefRepo, val classTypeService: ClassTypeService) {

    fun listClassDefs() = classDefRepo.listClassDefs()

    fun getClassDef(id: Int): Either<NotFoundException, ClassDefinition> {
        val classDef = classDefRepo.findByIdOrNull(id)
        return if (classDef != null) Either.Right(classDef) else Either.Left(NotFoundException())
    }

    fun deleteClassDef(id: Int) = classDefRepo.deleteById(id)

    fun createClassDef(form: ClassDefinitionForm): Either<Throwable, ClassDefinition> {
        return classTypeService.getClassType(form.classTypeCode)
            .map { ClassDefinition.fromForm(form, it) }
            .flatMap { saveClassDef(it) }
        // TODO Toimisko tämä: .mapLeft { Either.Left(BadRequestException("Unknown classTypeCode ${form.classTypeCode}")) }
        /*val classType = classTypeService.getClassType(form.classTypeCode).getOrHandle {
            return Either.Left(BadRequestException("Unknown classTypeCode ${form.classTypeCode}"))
        }

        val classDef = ClassDefinition.fromForm(form, classType)*/
    }

    private fun saveClassDef(classDef: ClassDefinition): Either<Throwable, ClassDefinition> {
        return Either.catch { classDefRepo.save(classDef) }.mapLeft {
            logger.error(it) { "Failed to save ClassDefinition $classDef: ${it.message}" }
            it
        }
    }

    fun updateClassDef(id: Int, form: ClassDefinitionForm): Either<Throwable, ClassDefinition> {
        TODO("Not implemented")
    }
}
