package com.devappsys.razorpaydemo.model

import com.google.gson.annotations.SerializedName

data class RazorPayCustomer (
    val id  : String,
    val entity : String,
    val name : String,
    val email : String,
    val contact : String,
    )

//{
//    "id":"cust_KoDxb3RK0V8q",
//    "entity":"customer",
//    "name":"Mahesh",
//    "email":"mahxxxxxxxx@gmail.com",
//    "contact":"99999999999",
//    "gstin":null,
//    "notes":{
//    "doc_id":"123",
//    "any_other_filed":"Xyz"
//},
//    "created_at":1670242302
//}