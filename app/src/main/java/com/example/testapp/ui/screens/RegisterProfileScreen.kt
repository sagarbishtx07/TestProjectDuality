package com.example.testapp.ui.screens


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.testapp.R
import com.example.testapp.network.registerApi
import com.example.testapp.utils.AppButton
import com.example.testapp.utils.CustomTextField
import com.example.testapp.utils.InputType
import com.example.testapp.utils.Preference
import com.example.testapp.utils.ProgressDialog
import com.example.testapp.viewmodel.MainViewModel


@Composable
fun RegisterProfileScreen(
    navController: NavHostController, viewModel: MainViewModel, prefs: Preference
) {
    var profile by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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
            Text(
                stringResource(R.string.profile),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                color = Color.Black,
            )
            Text(
                stringResource(R.string.setup_profile),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                ),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clickable {
                    }
                    .clip(CircleShape)
                    .background(colorResource(R.color.grayColor))
            ) {
                Icon(
                    painter = painterResource(R.drawable.takephoto),
                    contentDescription = "Add Profile Picture",
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            CustomTextField(
                value = name,
                onValueChange = { it ->
                    name = it
                },
                label = stringResource(R.string.name),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                value = gender,
                onValueChange = { gender = it },
                label = stringResource(R.string.Gender),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                inputType = InputType.AlphaNumeric
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                value = age.toString(),
                onValueChange = { age = it },
                label = stringResource(R.string.Age),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                inputType = InputType.AlphaNumeric
            )
            Spacer(modifier = Modifier.height(15.dp))
            CustomTextField(
                value = bio,
                onValueChange = { bio = it },
                label = stringResource(R.string.Bio),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                inputType = InputType.AlphaNumeric
            )
            Spacer(modifier = Modifier.height(15.dp))
            if (isError) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier
                .fillMaxHeight()
                .weight(1f))
            AppButton(
                label = stringResource(R.string.register),
                onClickAction = {
                    isLoading = true
                    if (name.isNotBlank() && age.isNotBlank()
                        && bio.isNotBlank() && gender.isNotBlank()
                    ) {
                        isError = false
                        registerApi(
                            viewModel = viewModel,
                            name = name,
                            age = age,
                            bio = bio,
                            gender = gender,
                            navController = navController
                        ) {
                            isLoading = false
                            if (it != null) {
                                if (it.status == 200||it.status==201) {
                                    isError = false
                                    // prefs.saveStringValue("token", it.data.accessToken)
                                    Toast.makeText(
                                        navController.context,
                                        "Profile Created Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //   navController.navigate("forgot_password_screen")
                                } else {
                                    isError = true
                                    errorMessage = "Something Went Wrong!! Please Retry"
                                }
                            } else {
                                isError = true
                                errorMessage = "Registration Failed!!"
                            }
                        }
                    } else {
                        isLoading = false
                        isError = true
                        if (name.isBlank())
                            errorMessage = "Please add Name!!"
                        if (age.isBlank())
                            errorMessage = "Please add Age!!"
                        else if (gender.isBlank())
                            errorMessage = "Please add Your Gender!!"
                        else
                            errorMessage = "Please complete all fields!!"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            ProgressDialog(onDismissRequest = { isLoading = false }, isLoading = isLoading)

        }

    }
}
