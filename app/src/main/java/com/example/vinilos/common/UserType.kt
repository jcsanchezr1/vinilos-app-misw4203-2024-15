package com.example.vinilos.common

enum class UserType(val type: String) {
    VISITOR("visitor"),
    COLLECTOR("collector");

    companion object {
        fun fromString(value: String): UserType? {
            return values().find { it.type == value }
        }
    }
}