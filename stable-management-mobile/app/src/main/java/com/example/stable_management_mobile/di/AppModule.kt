package com.example.stable_management_mobile.di

import com.example.stable_management_mobile.data.remote.StableApi
import com.example.stable_management_mobile.data.repository.StableRepositoryImpl
import com.example.stable_management_mobile.domain.repository.StableRepository
import com.example.stable_management_mobile.ui.screens.stables.StablesViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder().addInterceptor(logging).build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StableApi::class.java)
    }

    single<StableRepository> {
        StableRepositoryImpl(get())
    }

    viewModel{
        StablesViewModel(get())
    }
}