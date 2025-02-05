package com.example.testapp.interfaces



sealed interface OtpAction {
    data class onEnterNumber(
        val number: Int?,
        val index: Int
    ) : OtpAction

    data class onChangeFieldFocused(val index: Int) : OtpAction

    data object onKeyboardBack : OtpAction
}