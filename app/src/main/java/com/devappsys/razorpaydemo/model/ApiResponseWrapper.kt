package com.devappsys.razorpaydemo.model

data class ApiResponseWrapper<T> (
    var isSuccess: Boolean = false,
    var message: String? = null,
    var data: T? = null,
    var requestCode: Int = 0
)