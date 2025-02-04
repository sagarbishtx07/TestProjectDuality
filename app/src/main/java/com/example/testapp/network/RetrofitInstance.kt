package com.example.testapp.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class RetrofitInstance(private var token: String? = null) {
    companion object {
        val base_url = "https://ayooba-duality-project.vercel.app/api/v1/"
    }

    private var retrofit: Retrofit? = null

    private val logging by lazy {
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    private fun createRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)

        token?.let {
            Timber.tag("RetrofitInstance").d("Token added : $token")

            val authInterceptor = AuthenticationInterceptor(token = it)
            client.addInterceptor(authInterceptor)
        }

        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }
    fun updateToken(newToken: String) {
        token = newToken
        retrofit = null
    }

    val api: Api
        get() {
            val retrofit = createRetrofit()
            return retrofit.create(Api::class.java)
        }

    class AuthenticationInterceptor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            Timber.tag("RetrofitInstance").d("using update token $token")
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }
    }
}