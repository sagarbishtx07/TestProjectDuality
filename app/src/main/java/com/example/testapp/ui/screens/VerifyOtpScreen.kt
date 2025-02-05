package com.example.testapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.testapp.R
import com.example.testapp.interfaces.OtpAction
import com.example.testapp.network.forgotPasswordApi
import com.example.testapp.network.verifyOtpApi
import com.example.testapp.utils.AppButton
import com.example.testapp.utils.CustomTextField
import com.example.testapp.utils.InputType
import com.example.testapp.utils.Preference
import com.example.testapp.utils.ProgressDialog
import com.example.testapp.utils.isValidEmail
import com.example.testapp.viewmodel.MainViewModel
import com.example.testapp.viewmodel.OTPViewModel
import timber.log.Timber

@Composable
fun VerifyOtpScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    preference: Preference
) {

    val viewModel = viewModel<OTPViewModel>()
    var email by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val state by viewModel.state.collectAsState()
    val focusRequesters = remember {
        List(4) {
            FocusRequester()
        }
    }
    val keyboardManager = LocalSoftwareKeyboardController.current

    LaunchedEffect(
        state.focusIndex
    ) {
        state.focusIndex?.let { index ->
            focusRequesters.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(
        state.code, keyboardManager
    ) {
        val allNumberEntered = state.code.none { it != null }
        if (allNumberEntered) {
            focusRequesters.forEach {
                it.freeFocus()
            }
            keyboardManager?.hide()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()),
        color = colorResource(R.color.bgColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(R.string.cancel),
                fontSize = 12.sp,
                color = colorResource(R.color.themeColor),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth().
                clickable {
                    navController.popBackStack()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(R.string.verify_number),
                fontSize = 12.sp,
                color = colorResource(R.color.black),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(R.string.enter_the_otp),
                fontSize = 11.sp,
                color = colorResource(R.color.darkgray),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))
            OtpScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        is OtpAction.onEnterNumber -> {
                            if (action.number != null) {
                                focusRequesters[action.index].freeFocus()
                            }
                        }

                        else -> Unit
                    }
                    viewModel.onAction(action)
                },
                focusRequesters = focusRequesters,
                modifier = Modifier.padding(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (isError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                stringResource(R.string.resend_otp),
                fontSize = 12.sp,
                color = colorResource(R.color.themeColor),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth().clickable {
                        isLoading = true
                        if (email.isNotBlank()) {
                            isError = false
                            forgotPasswordApi(mainViewModel, email, navController) {
                                isLoading = false
                                if (it != null) {
                                    if (it.status == 200) {
                                        //prefs.saveStringValue("token",it.data.token)
                                        Toast.makeText(
                                            navController.context,
                                            "OTP Sent",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        //   navController.navigate("forgot_password_screen")
                                    } else {
                                        isError = true
                                        errorMessage = it.message
                                    }
                                } else {
                                    isError = true
                                    errorMessage =
                                        "Something went wrong, Please check the email again!!"
                                }

                            }
                        } else {
                            isLoading = false
                            isError = true
                            errorMessage = "Something went wrong!!"
                        }
                    }
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppButton(
                label = stringResource(R.string.continue_text),
                onClickAction = {

                    val otpCode = state.code.filterNotNull()
                        .joinToString("") // Convert list to string and remove null values
                    if (otpCode.length != 4) {
                        isError = true
                        errorMessage = "Invalid OTP length!!"
                    } else {
                        Timber.tag("OTP").d(otpCode)
                        isLoading = true
                        verifyOtpApi(
                            navController = navController,
                            viewModel = mainViewModel,
                            email = preference.getStringValue("email"),
                            otp = otpCode,
                        ) {
                            isLoading = false
                            if (it != null) {
                                if (it.status == 200 || it.status == 201) {
                              //      navController.navigate("enter_new_password_screen")
                                    Toast.makeText(navController.context,"OTP Verified Successfully!!",Toast.LENGTH_SHORT).show()
                                } else {
                                    isError = true
                                    errorMessage = it.message
                                }
                            } else {
                                isError = true
                                errorMessage = "Something went wrong!!"
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))


            ProgressDialog(onDismissRequest = { isLoading = false }, isLoading = isLoading)

        }
    }
}