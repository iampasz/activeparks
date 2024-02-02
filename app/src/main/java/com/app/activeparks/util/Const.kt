package com.app.activeparks.util

import java.util.UUID

/**
 * Created by O.Dziuba on 23.11.2023.
 */
const val MIN_PASSWORD_LENGTH = 8
const val MIN_ACTIVITY_TITLE_LENGTH = 5
const val ANDROID_TYPE_ID = 1
const val ADMIN = "631db81f-fa07-42e2-b394-062c9be66b09"
const val COORDINATOR = "5dcf0363-d171-45db-9280-cb337ca5e101"

const val SESSION_REPOSITORY = "com.app.activeparks"

const val URL_INFO_LIST = "https://ap.sportforall.gov.ua/infolist/start"
const val URL_PERSONAL_DATE = "https://ap.sportforall.gov.ua/info/Hz0lhIus"

val HR_MEASUREMENT_UUID: UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
val HR_SERVICE_UUID: UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
val CLIENT_CHARACTERISTIC_CONFIG_UUID: UUID =
    UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

const val DEVICE_IS_DISCONNECTED = 19