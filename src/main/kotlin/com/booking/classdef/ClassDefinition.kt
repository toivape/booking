package com.booking.classdef

import com.booking.classtype.ClassType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "class_definition")
class ClassDefinition(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,

    @Version
    val version: Int?,

    @Column(name = "name", length = NAME_MAX_LEN, nullable = false)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "class_type_code", nullable = false)
    val classType: ClassType,

    @Column(name = "location", length = LOCATION_MAX_LEN)
    val location: String?,

    @Column(name = "price_credits")
    val priceCredits: Int?,

    @Column(name = "max_people")
    val maxPeople: Int?,

    @Column(name = "description", length = DESC_MAX_LEN)
    val description: String?,

    @Column(name = "recurrence_days", columnDefinition = "varchar(3)[]")
    val recurrenceWeekDays: Array<String>?,

    @Column(name = "recurrence_start_date", columnDefinition = "DATE")
    val recurrenceStartDate: LocalDate?,

    @Column(name = "recurrence_end_date", columnDefinition = "DATE")
    val recurrenceEndDate: LocalDate?,

    @Column(name = "start_time", columnDefinition = "TIME")
    val startTime: String?,

    @Column(name = "end_time", columnDefinition = "TIME")
    val endTime: String?,

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    val createdAt: LocalDateTime?,

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    val updatedAt: LocalDateTime?
) {
    companion object {
        fun fromForm(f: ClassDefinitionForm, classType: ClassType, now: LocalDateTime = LocalDateTime.now()) =
            ClassDefinition(
                id = f.id,
                version = f.version,
                name = f.name,
                classType = classType,
                location = f.location,
                priceCredits = f.priceCredits,
                maxPeople = f.maxPeople,
                description = f.description,
                recurrenceWeekDays = f.recurrenceWeekDays?.toStringArray(),
                recurrenceStartDate = f.recurrenceStartDate,
                recurrenceEndDate = f.recurrenceEndDate,
                startTime = f.startTime,
                endTime = f.endTime,
                createdAt = now,
                updatedAt = now
            )

        const val NAME_MAX_LEN = 300
        const val LOCATION_MAX_LEN = 500
        const val DESC_MAX_LEN = 3000
    }
}
