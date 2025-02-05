package com.example.testapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.data.request.ForgotPasswordReq
import com.example.testapp.data.request.ForgotVerifyOtpReq
import com.example.testapp.data.request.LoginApiReq
import com.example.testapp.data.request.RegistrationApiReq
import com.example.testapp.data.request.SignUpReq
import com.example.testapp.data.response.ForgotPasswordRes
import com.example.testapp.data.response.ForgotVerifyOtpRes
import com.example.testapp.data.response.LoginApiRes
import com.example.testapp.data.response.RegistrationApiRes
import com.example.testapp.data.response.SignUpApiRes
import com.example.testapp.repo.MainRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    val repo: MainRepo
) : ViewModel() {
    private fun <T> makeApiCall(apiCall: suspend () -> Response<T>): MutableLiveData<T?> {
        val liveData = MutableLiveData<T?>()
        viewModelScope.launch {
            val response = apiCall()
            if (response.isSuccessful)
                liveData.postValue(response.body())
            else
                liveData.postValue(null)
        }
        return liveData
    }


    fun loginApi(request: LoginApiReq): MutableLiveData<LoginApiRes?> {
        return makeApiCall { repo.login(request) }
    }

    fun signUpApi(request: SignUpReq): MutableLiveData<SignUpApiRes?> {
        return makeApiCall { repo.signUp(request) }
    }

    fun registerApi(request: RegistrationApiReq): MutableLiveData<RegistrationApiRes?> {
        return makeApiCall { repo.registrationApi(request) }
    }

    fun forgotPassword(request: ForgotPasswordReq): MutableLiveData<ForgotPasswordRes?> {
        return makeApiCall { repo.forgotPassword(request) }
    }

    fun verifyOTP(request: ForgotVerifyOtpReq): MutableLiveData<ForgotVerifyOtpRes?> {
        return makeApiCall { repo.verifyOTP(request) }
    }


}