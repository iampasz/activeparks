package com.app.activeparks.data.model.user

/**
 * Created by O.Dziuba on 11.01.2024.
 */
enum class UserRole(val role: String, val keyId: String) {
    ADMIN("Адміністратор", "631db81f-fa07-42e2-b394-062c9be66b09"),
    VERIFIED_USER("Верифікований користувач", "62fe0318-c64a-490c-859d-9d313eacbf41"),
    VIDEO_LIBRARY_CONTENT_MANAGER("Контент-менеджер відеотеки", "3fbea050-7049-4682-92f6-15c2bad91972"),
    INFORMATION_MATERIALS_CONTENT_MANAGER("Контент-менеджер інформаційних матеріалів", "9952ac5a-fd43-4e8f-86ff-2bf9cb2a7804"),
    SPORTS_FACILITIES_COORDINATOR("Координатор спортивних майданчиків", "09efbeb2-f45a-418d-89b0-b2a4c37f6122"),
    USER("Користувач", "a2c99acd-0014-4fb3-8274-ad6a842f50ac"),
    EVENT_MODERATOR("Модератор заходів", "5dcf0363-d171-45db-9280-cb337ca5e101"),
    CLUB_MODERATOR("Модератор клубів", "d379ecaa-fee7-48a4-84df-a176f47820e6"),
    USER_VIDEOS_MODERATOR("Модератор користувальницьких відео", "e9789dde-94a9-4ad3-88e8-374ec41f8c1a"),
    OFFICIAL_VIDEO_LIBRARY_MODERATOR("Модератор офіційної відеотеки", "2f5a84bc-52b5-4777-8790-e2c32daa6a34"),
    SPORTS_FACILITIES_REGISTER_MODERATOR("Модератор реєстру майданчиків", "66d22cb4-2cf9-4aa7-806b-c50b50f27020"),
    UNAUTHORIZED_USER("Неавторизований користувач", "370006ef-1823-46af-8126-9b06d2cecbc3"),
    COORDINATOR_APPLICANT("Подається на координатора", "a9329d2e-42b1-4ad3-88e8-442ea4123cfa"),
    SPORTS_FACILITIES_REGISTRY_SPECIALIST("Спеціаліст реєстру майданчиків", "460553c9-3c42-443c-afb3-24308587fe62"),
    SUPPORT_SERVICE_SPECIALIST("Фахівець служби підтримки", "48633388-3277-4c7b-8d39-d421e9e90599");

    companion object {
        fun getRoleByKeyId(keyId: String?): String {
            return values().find { it.keyId == keyId }?.role ?: ""
        }
    }
}