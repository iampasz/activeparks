package com.app.activeparks.ui.participants.util

enum class ParticipantsTypes(val type:String) {
    ACCEPT("accept"),
    REJECT("reject"),
    LEAVE("leave"),
    SET_ACTION("set-acting"),
    REMOVE_ACTION("remove-acting"),
    APPROVE_USER("approve-user"),
    REJECT_USER("reject-user"),
    REMOVE_USER("remove-user"),
}
