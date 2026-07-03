package com.example.network

import android.util.Log
import com.example.model.Category
import com.example.model.Product
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Url
import retrofit2.Response

private const val TAG = "CloudSyncService"
private const val BASE_URL = "https://placeholder.invalid/"

interface CloudSyncApi {
    @GET
    suspend fun getProducts(@Url url: String): Response<List<Product>>

    @PUT
    suspend fun updateProducts(@Url url: String, @Body products: List<Product>): Response<ResponseBody>

    @GET
    suspend fun getCategories(@Url url: String): Response<List<Category>>

    @PUT
    suspend fun updateCategories(@Url url: String, @Body categories: List<Category>): Response<ResponseBody>
}

object CloudSyncService {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: CloudSyncApi = retrofit.create(CloudSyncApi::class.java)

    // Helper to serialize list of products manually if needed
    fun serializeProducts(products: List<Product>): String {
        val type = Types.newParameterizedType(List::class.java, Product::class.java)
        return moshi.adapter<List<Product>>(type).toJson(products)
    }

    // Helper to serialize list of categories manually if needed
    fun serializeCategories(categories: List<Category>): String {
        val type = Types.newParameterizedType(List::class.java, Category::class.java)
        return moshi.adapter<List<Category>>(type).toJson(categories)
    }
}
