package com.emiryanvl.persistence.entities

import jakarta.persistence.*

@Entity
@Table(name = "_user")
class UserEntity (
    var username: String,
    var password: String,
    var role: String
) : BaseEntity<Long>()