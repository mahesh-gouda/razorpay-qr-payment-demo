package com.devappsys.razorpaydemo.model

import com.google.gson.annotations.SerializedName

data class RazorPayQRCode(
    val id: String,
    val entity: String,
    val name: String,
    val usage: String,
    @SerializedName("payment_amount")
    val paymentAmount: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    val status: String,
    val description: String,
    @SerializedName("payments_amount_received")
    val paymentAmountReceived: Int,
    @SerializedName("payments_count_received")
    val paymentCountReceived: Int,
    @SerializedName("customer_id")
    val customerId: String,

)

//
//
//{
//    "id": "qr_KquZv0QFXZ",
//    "entity": "qr_code",
//    "created_at": 1670828271,
//    "name": "Store_1",
//    "usage": "single_use",
//    "type": "upi_qr",
//    "image_url": "https://rzp.io/i/xthjS",
//    "payment_amount": 300,
//    "status": "active",
//    "description": "For Store 1",
//    "fixed_amount": true,
//    "payments_amount_received": 0,
//    "payments_count_received": 0,
//    "notes": {
//    "purpose": "Test UPI QR code notes"
//},
//    "customer_id": "cust_KqszgDxK",
//    "close_by": 1981615838,
//    "tax_invoice": []
//}