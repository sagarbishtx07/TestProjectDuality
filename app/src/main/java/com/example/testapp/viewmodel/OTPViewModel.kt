package com.example.testapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.testapp.data.OtpScreenState
import com.example.testapp.interfaces.OtpAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
private const val VALID_OTP_CODE = "1414"

class OTPViewModel : ViewModel() {
    private val _state = MutableStateFlow(OtpScreenState())
    val state = _state.asStateFlow()

    fun onAction(action: OtpAction) {
        when (action) {
            is OtpAction.onChangeFieldFocused -> {
                _state.update {
                    it.copy(
                        focusIndex = action.index
                    )
                }
            }

            is OtpAction.onEnterNumber -> {
                enterNumber(action.number, action.index)
            }

            OtpAction.onKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusIndex)
                _state.update {
                    it.copy(
                        code = it.code.mapIndexed { index, number ->
                            if (index == previousIndex){
                                null
                            }else{
                                number
                            }
                        },
                        focusIndex = previousIndex
                    )
                }
            }
        }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun enterNumber(
        number: Int?, index: Int
    ) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _state.update {
            it.copy(
                code = newCode,
                focusIndex = if (wasNumberRemoved || it.code.getOrNull(index) != null) {
                    it.focusIndex
                } else {
                    getNextFocusedTextFieldIndex(
                        currentCode = it.code,
                        currentFocusedIndex = it.focusIndex
                    )
                },
                isValid = if (newCode.none
                    { it == null }
                ) {
                    newCode.joinToString("") == VALID_OTP_CODE
                } else null
            )

        }
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if (currentFocusedIndex == null) {
            return null
        }
        if (currentFocusedIndex == 3)
            return currentFocusedIndex

        return getFirstEmptyIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if (index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if (number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }
}