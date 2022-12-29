package com.booking.classtype

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "class_type")
class ClassType(
    @Id
    val code: String,

    @Column(name = "name")
    val name: String
)
