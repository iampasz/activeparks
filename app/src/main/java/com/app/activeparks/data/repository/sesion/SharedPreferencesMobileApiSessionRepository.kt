package com.app.activeparks.data.repository.sesion

import android.content.SharedPreferences
import com.app.activeparks.data.repository.sesion.model.GsonSession
import com.app.activeparks.data.repository.sesion.model.ISession

/**
 * Created by O.Dziuba on 28.11.2023.
 */

const val ACCESS = "safasdf"
const val REFRESH = "sfsdf"

class SharedPreferencesMobileApiSessionRepository(
    private val sharedPreferences: SharedPreferences
) :
    MobileApiSessionRepository {
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    override fun save(session: ISession): ISession {
        editor.putString(
            ACCESS,
            session.accessToken()
        )
            .apply()
        editor.putString(
            REFRESH,
            session.refreshToken()
        )
            .apply()
        return session
    }

    override fun load(): ISession {
        val access = sharedPreferences.getString(ACCESS, "") ?: ""
        val refresh = sharedPreferences.getString(REFRESH, "") ?: ""

        return GsonSession(
            access,
            refresh
        )
    }

    override fun clear() {
        save(GsonSession("", ""))
    }
}
