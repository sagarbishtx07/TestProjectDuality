package com.example.testapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.network.loginApi
import com.example.testapp.network.signUpApi
import com.example.testapp.utils.AppButton
import com.example.testapp.utils.CustomTextField
import com.example.testapp.utils.InputType
import com.example.testapp.utils.MyButton
import com.example.testapp.utils.Preference
import com.example.testapp.utils.ProgressDialog
import com.example.testapp.utils.isValidEmail
import com.example.testapp.viewmodel.MainViewModel


@Composable
fun CreateAccountScreen(
    navController: NavHostController, viewModel: MainViewModel, prefs: Preference
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm_password by remember { mutableStateOf("") }
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(R.drawable.appicon),
                contentDescription = "Banner",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .clipToBounds(),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(R.string.create_your_account),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                stringResource(R.string.enter_your_details),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                ),
                color = Color.Black
            )

            CustomTextField(
                value = email,
                onValueChange = { it ->
                    email = it
                },
                label = stringResource(R.string.email_id),
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
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(R.string.password),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                startIcon = painterResource(R.drawable.password),
                inputType = InputType.Password
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                value = confirm_password,
                onValueChange = { confirm_password = it },
                label = stringResource(R.string.confirm_password),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                startIcon = painterResource(R.drawable.password),
                inputType = InputType.Password
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                stringResource(R.string.forgot_password),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("forgot_password")
                    },
                textAlign = TextAlign.End,
                fontSize = 11.sp,
                color = Color.Gray
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
            Spacer(modifier = Modifier.height(20.dp))
            AppButton(
                label = stringResource(R.string.register),
                onClickAction = {
                    isLoading = true
                    if (isValidEmail(email) && password.isNotBlank() && confirm_password.isNotBlank() && password == confirm_password) {
                        isError = false
                        signUpApi(viewModel, email, password, navController) {
                            isLoading = false
                            if (it != null) {
                                if (it.status == 200||it.status==201) {
                                    prefs.saveStringValue("token", it.data.accessToken)
                                    (navController.context as MainActivity).updateRetrofitInstance()
                                    Toast.makeText(navController.context,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                                    navController.navigate("register_profile")
                                } else {
                                    isError = true
                                    errorMessage = "Something Went Wrong!! Please Retry"
                                }
                            } else {
                                isError = true
                                errorMessage = "SignUp Failed!! Check Email or Password!!"
                            }

                        }
                    } else {
                        isLoading = false
                        isError = true
                        if (password != confirm_password)
                            errorMessage = "Password does not match!!"
                        else if (!isValidEmail(email))
                            errorMessage = "Invalid email!!"
                        else
                            errorMessage = "Invalid password"
                    }
                    Log.d("Validation", "$email\n$password")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f), // Occupy available space on the left
                    thickness = 0.5.dp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.or),
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(
                    modifier = Modifier.weight(1f), // Occupy available space on the left
                    thickness = 0.5.dp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            MyButton(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth(),
                iconId = R.drawable.google,
                label = stringResource(R.string.google)
            )
            Spacer(modifier = Modifier.height(20.dp))
            MyButton(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth(),
                iconId = R.drawable.iconfb,
                label = stringResource(R.string.facebook)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.already_have_an_account),
                    textAlign = TextAlign.End,
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )

                Text(
                    stringResource(R.string.login),
                    textAlign = TextAlign.End,
                    fontSize = 12.sp,
                    color = colorResource(R.color.themeColor),
                    modifier = Modifier.clickable {
                        navController.navigate("login_screen")
                    }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            ProgressDialog(onDismissRequest = { isLoading = false }, isLoading = isLoading)

        }

    }
}
