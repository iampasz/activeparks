package com.app.activeparks.data.repository.sesion

import com.app.activeparks.data.repository.sesion.model.ISession

/**
 * Created by O.Dziuba on 28.11.2023.
 */
interface MobileApiSessionRepository {
    fun save(session: ISession): ISession
    fun load(): ISession
    fun clear()
}