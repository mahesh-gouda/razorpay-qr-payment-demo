package com.devappsys.razorpaydemo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devappsys.razorpaydemo.model.ApiResponseWrapper
import com.devappsys.razorpaydemo.model.RazorPayCustomer
import com.devappsys.razorpaydemo.model.RazorPayQRCode
import com.devappsys.razorpaydemo.utils.AppConstants
import com.devappsys.razorpaydemo.utils.AppConstants.RAZORPAY_CUSTOMER
import com.devappsys.razorpaydemo.utils.AppConstants.RAZORPAY_QR
import com.example.razorpaydemo.retrofit.ApiCall
import com.example.razorpaydemo.retrofit.RetrofitClient
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Credentials
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel: ViewModel() {
    var createCustomerResponse: MutableLiveData<RazorPayCustomer>
    var createQRCodeResponse: MutableLiveData<RazorPayQRCode>
    var getQRcodeStatusResponse: MutableLiveData<RazorPayQRCode>


   init {
       createCustomerResponse = MutableLiveData()
       createQRCodeResponse = MutableLiveData()
       getQRcodeStatusResponse = MutableLiveData()
   }



    fun getCreateCustomerResponseObservable() :  MutableLiveData<RazorPayCustomer> {
        return createCustomerResponse;
    }

    fun getCreateQRCodeResponseObservable() :  MutableLiveData<RazorPayQRCode> {
        return createQRCodeResponse;
    }

    fun getQRcodeStatusResponseObservable() :  MutableLiveData<RazorPayQRCode> {
        return getQRcodeStatusResponse;
    }


    fun createNewCustomer(email: String, phone: String ){
        val requestData = JSONObject()
        val extraNote = JSONObject()
        extraNote.put("doc_id","123")  //optional, just if we want to add any data we can add here
        extraNote.put("any_other_filed","Xyz") //optional, just if we want to add any data we can add here


        requestData.put("name","Mahesh")
        requestData.put("email",email)
        requestData.put("contact",phone)
        requestData.put("fail_existing","1")
//        requestData.put("gstin","12ABCDE2356F7GH")
        requestData.put("notes",extraNote)


        val retrofitInstance = RetrofitClient.create()
        val credential: String = Credentials.basic(AppConstants.RAZORPAY_TEST_KEY,AppConstants.RAZORPAY_TEST_SECRET)
        val call = retrofitInstance.getRazorPayOrderId(
            url = RAZORPAY_CUSTOMER, credentials = credential,
            jsonObject = JsonParser().parse(requestData.toString()) as JsonObject
        )

        call.enqueue(object : Callback<RazorPayCustomer> {
            override fun onResponse(call: Call<RazorPayCustomer>, response: Response<RazorPayCustomer>) {
                Log.e("onResponse", response.toString(), )
                createCustomerResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<RazorPayCustomer>, t: Throwable) {
//                createTransactionLiveData.postValue(null)
                Log.e("onFailure", t.toString(), )
            }
        })

    }

    fun createQRCode(customerId: String ){
        val requestData = JSONObject()
        val extraNote = JSONObject()
        extraNote.put("doc_id","123") //optional, just if we want to add any data we can add here
        extraNote.put("any_other_filed","Xyz") //optional
        extraNote.put("purpose","collecting payment") //optional


        requestData.put("type","upi_qr")
        requestData.put("name","Store_1")
        requestData.put("usage","single_use")
        requestData.put("fixed_amount",true)
        requestData.put("payment_amount",300)
        requestData.put("description","For Store 1")
        requestData.put( "customer_id",customerId)
        requestData.put( "close_by",1981615838)
        requestData.put("notes",extraNote)

        val retrofitInstance = RetrofitClient.create()
        val credential: String = Credentials.basic(AppConstants.RAZORPAY_TEST_KEY,AppConstants.RAZORPAY_TEST_SECRET)
        val call = retrofitInstance.createQRCode(
            url = RAZORPAY_QR, credentials = credential,
            jsonObject = JsonParser().parse(requestData.toString()) as JsonObject
        )

        call.enqueue(object : Callback<RazorPayQRCode> {
            override fun onResponse(call: Call<RazorPayQRCode>, response: Response<RazorPayQRCode>) {
                Log.e("onResponse", response.toString(), )
                createQRCodeResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<RazorPayQRCode>, t: Throwable) {
//                createTransactionLiveData.postValue(null)
                Log.e("onFailure", t.toString(), )
            }
        })

    }

    fun getQRCodeStatus(qrCodeId: String ){

        val retrofitInstance = RetrofitClient.create()
        val credential: String = Credentials.basic(AppConstants.RAZORPAY_TEST_KEY,AppConstants.RAZORPAY_TEST_SECRET)
        val url = "$RAZORPAY_QR/$qrCodeId"
        val call = retrofitInstance.getQRCode(
            url = url, credentials = credential,
        )

        call.enqueue(object : Callback<RazorPayQRCode> {
            override fun onResponse(call: Call<RazorPayQRCode>, response: Response<RazorPayQRCode>) {
                Log.e("onResponse", response.toString(), )
                getQRcodeStatusResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<RazorPayQRCode>, t: Throwable) {
//                createTransactionLiveData.postValue(null)
                Log.e("onFailure", t.toString(), )
            }
        })

    }

}