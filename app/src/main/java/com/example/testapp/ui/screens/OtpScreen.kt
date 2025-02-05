package com.example.testapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.testapp.R
import com.example.testapp.data.OtpScreenState
import com.example.testapp.interfaces.OtpAction
import com.example.testapp.utils.OtpInputField

@Composable
fun OtpScreen(
    state: OtpScreenState,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(color = colorResource(R.color.bgColor)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            state.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    onFocusChanged = { isFocused ->
                        if (isFocused)
                            onAction(OtpAction.onChangeFieldFocused(index))
                    },
                    focusRequester = focusRequesters[index],
                    onNumberChanged = { newNumber ->
                        onAction(OtpAction.onEnterNumber(newNumber, index))
                    },
                    onKeyboardBackPress = {
                        onAction(OtpAction.onKeyboardBack)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}