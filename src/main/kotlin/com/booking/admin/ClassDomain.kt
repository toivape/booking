package com.booking.admin

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
@Table(name = "class_type")
class ClassType(
    @Id
    val code: String,

    @Column(name = "name")
    val name: String
)

@Entity
@Table(name = "class_definition")
class ClassDefinition(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int,

    @Version
    val version: Int,

    @Column(name = "name", length = 300, nullable = false)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "class_type_code", nullable = false)
    val classType: ClassType,

    @Column(name = "location", length = 500)
    val location: String?,

    @Column(name = "price_credits")
    val priceCredits: Int?,

    @Column(name = "max_people")
    val maxPeople: Int?,

    @Column(name = "description", length = 3000)
    val description: String?,

    @Column(name = "recurrence_days", columnDefinition = "varchar(3)[]")
    val recurrenceDays: Array<String>?,

    @Column(name = "recurrence_start_date", columnDefinition = "DATE")
    val recurrenceStartDate: LocalDate?,

    @Column(name = "recurrence_end_date", columnDefinition = "DATE")
    val recurrenceEndDate: LocalDate?,

    @Column(name = "start_time", columnDefinition = "TIME")
    val startTime: String?,

    @Column(name = "end_time", columnDefinition = "TIME")
    val endTime: String?,

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    val updatedAt: LocalDateTime
)
