package com.example.razorpaydemo.retrofit

import com.devappsys.razorpaydemo.model.RazorPayCustomer
import com.devappsys.razorpaydemo.model.RazorPayQRCode
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiCall {
    @Headers("Content-Type: application/json")
    @POST()
    fun getRazorPayOrderId(
        @Header("Authorization") credentials: String,
        @Url url: String,
        @Body jsonObject: JsonObject
    ): Call<RazorPayCustomer>


    @Headers("Content-Type: application/json")
    @POST()
    fun createQRCode(
        @Header("Authorization") credentials: String,
        @Url url: String,
        @Body jsonObject: JsonObject
    ): Call<RazorPayQRCode>

    @Headers("Content-Type: application/json")
    @GET()
    fun getQRCode(
        @Header("Authorization") credentials: String,
        @Url url: String,
    ): Call<RazorPayQRCode>
}