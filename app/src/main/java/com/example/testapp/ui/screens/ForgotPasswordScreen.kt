package com.example.testapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.testapp.R
import com.example.testapp.network.forgotPasswordApi
import com.example.testapp.utils.AppButton
import com.example.testapp.utils.CustomTextField
import com.example.testapp.utils.InputType
import com.example.testapp.utils.Preference
import com.example.testapp.utils.ProgressDialog
import com.example.testapp.utils.isValidEmail
import com.example.testapp.viewmodel.MainViewModel

@Composable
fun ForgotPasswordScreen(
    navController: NavHostController, viewModel: MainViewModel, prefs: Preference
) {
    var email by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    Surface(
        color = colorResource(R.color.bgColor),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.vector7),
                    contentDescription = "Back Btn",
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    stringResource(R.string.back),
                    fontSize = 12.sp,
                    color = colorResource(R.color.black),
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(R.string.forgot_your_password),
                fontSize = 12.sp,
                color = colorResource(R.color.black),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(R.string.enter_registered_email),
                fontSize = 12.sp,
                color = colorResource(R.color.black),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))
            CustomTextField(
                value = email,
                onValueChange = { it ->
                    email = it
                },
                label = stringResource(R.string.emailphone),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                inputType = InputType.Email,
                startIcon = painterResource(R.drawable.email),
                isValid = {
                    isValidEmail(it)
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                stringResource(R.string.you_may_recieve),
                fontSize = 11.sp,
                color = colorResource(R.color.darkgray),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
            if (isError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            AppButton(
                label = stringResource(R.string.continue_text),
                onClickAction = {
                    isLoading = true
                    if (email.isNotBlank()) {
                        prefs.saveStringValue("email", email)
                        isError = false
                        forgotPasswordApi(viewModel, email, navController) {
                            isLoading = false
                            if (it != null) {
                                if (it.status == 200) {
                                    //prefs.saveStringValue("token",it.data.token)
                                    Toast.makeText(
                                        navController.context,
                                        "OTP Sent",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                      navController.navigate("verify_otp")
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
                        errorMessage = "Invalid email or number!!"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Spacer(modifier = Modifier.height(50.dp))
            ProgressDialog(onDismissRequest = { isLoading = false }, isLoading = isLoading)
        }
    }
}