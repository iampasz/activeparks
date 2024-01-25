package com.app.activeparks.data.model.user

/**
 * Created by O.Dziuba on 11.01.2024.
 */
enum class UserRole(val role: String) {
    USER("Користувач"),
    TRAINER("Тренер"),
    COORDINATOR("Координатор")
}