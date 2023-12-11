package com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.additionalValue

/**
 * Created by O.Dziuba on 24.11.2023.
 */
class Gender {
    companion object {
        const val MALE = "male"
        const val FEMALE = "female"
        fun getGenders() = listOf("Чоловіча", "Жіноча")
    }
}