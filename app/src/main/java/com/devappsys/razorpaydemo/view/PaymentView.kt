package com.devappsys.razorpaydemo.view

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.*
import androidx.lifecycle.*
import com.devappsys.razorpaydemo.R
import com.devappsys.razorpaydemo.model.RazorPayCustomer
import com.devappsys.razorpaydemo.model.RazorPayQRCode
import com.devappsys.razorpaydemo.viewmodel.PaymentViewModel
import com.squareup.picasso.Picasso

class PaymentView(context: Context?) : LinearLayout(context) {
    private lateinit var viewModel: PaymentViewModel
    private lateinit var viewModelStoreOwner: ViewModelStoreOwner
    private var dialog : Dialog? = null;
    private var qrDialog : Dialog? = null;
    private lateinit var generateQR : Button
    private lateinit var etEmail : EditText
    private lateinit var etPhone : EditText

    private lateinit var layoutShowQr : LinearLayout
    private lateinit var layoutPaymentSuccess : RelativeLayout
    private lateinit var layoutPaymentFailed : RelativeLayout
    private lateinit var layoutPaymentPending : RelativeLayout
    private lateinit var btnShowQR : Button
    private lateinit var btnCheckStatus : Button

    //data model
    private lateinit var razorPayQRCode : RazorPayQRCode
    private lateinit var razorPayCustomer: RazorPayCustomer

    //    val e_name: String = ""
//    var e_id: Int = 0
    constructor(context: Context?,viewModelStoreOwner: ViewModelStoreOwner) : this(context) {
        this.viewModelStoreOwner = viewModelStoreOwner;
        initUi()
    }
//
//    constructor(id: Int, context: Context?) : this(context) {
//        //code
//    }

    init {
//        initUi()
    }

    private fun initUi(){
        val view = LayoutInflater.from(context).inflate(R.layout.payment_view, null)

         generateQR  = view.findViewById(R.id.generateQR)

         etEmail  = view.findViewById(R.id.et_email)
        etEmail.background.clearColorFilter()
        etEmail.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)

         etPhone  = view.findViewById(R.id.et_phone)
        etPhone.background.clearColorFilter()
        etPhone.background.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)

         layoutShowQr  = view.findViewById(R.id.layoutShowQR)
         layoutPaymentSuccess  = view.findViewById(R.id.paymentSuccessLayout)
         layoutPaymentFailed  = view.findViewById(R.id.paymentFailedLayout)
         layoutPaymentPending  = view.findViewById(R.id.paymentPendingLayout)

         btnShowQR  = view.findViewById(R.id.veiwQR)
         btnCheckStatus  = view.findViewById(R.id.checkPaymentStatus)

        initViewModel()
        generateQR.setOnClickListener {
            if(etEmail.text.toString().isBlank() || etEmail.text.toString().isEmpty() ){
                etEmail.error = "Enter email";
            }else if(android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()){
                if(etPhone.text.toString().isBlank() || etPhone.text.toString().isEmpty() ){
                    etPhone.error = "Enter Phone number";
                }else if(android.util.Patterns.PHONE.matcher(etPhone.text.toString()).matches()) {
                    val email : String = etEmail.text.toString()
                    val phone : String = etPhone.text.toString()
                    showDialog("Processing, Please wait!")
                    viewModel.createNewCustomer(email,phone)
                }else{
                    etPhone.error = "Enter valid Phone number";
                }
            }else{
                etEmail.error = "Enter valid email";
            }
        }

        btnShowQR.setOnClickListener {
            showQRCode(razorPayQRCode.imageUrl)
        }

        btnCheckStatus.setOnClickListener {
            refreshQrStatus()
        }

        createCustomerResponseObservable()
        createQRCodeResponseObservable()
        createQRCodeStatusObservable()



        addView(view)

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(viewModelStoreOwner)[PaymentViewModel::class.java]
    }


    private fun createCustomerResponseObservable(){
        viewModel.getCreateCustomerResponseObservable()
            .observe(context.lifecycleOwner()!!, Observer<RazorPayCustomer> {
                if (it != null) {
                    Log.e("createCustomerResponseObservable: ", it.id)
                    razorPayCustomer = it
                    viewModel.createQRCode(it.id)
                }else{
                    Toast.makeText(context,"Unable create customer / Customer details might already exists",Toast.LENGTH_SHORT).show()
                    hideDialog()
                }
            })
    }


    private fun createQRCodeResponseObservable(){
        viewModel.getCreateQRCodeResponseObservable()
            .observe(context.lifecycleOwner()!!, Observer<RazorPayQRCode> {
                hideDialog()
                if (it != null) {
                    Log.e("qrcode: ", it.imageUrl)
                    razorPayQRCode = it

                    etEmail.visibility = GONE
                    etPhone.visibility = GONE
                    generateQR.visibility = GONE

                    layoutShowQr.visibility = VISIBLE

                    layoutPaymentFailed.visibility = GONE
                    layoutPaymentSuccess.visibility = GONE


                    showQRCode(it.imageUrl)
                }else{
                    Toast.makeText(context,"Unable create QrCode",Toast.LENGTH_SHORT).show()

                }
            })
    }

    private fun createQRCodeStatusObservable(){
        viewModel.getQRcodeStatusResponseObservable()
            .observe(context.lifecycleOwner()!!, Observer<RazorPayQRCode> {
                hideDialog()
                if (it != null) {
                    Log.e("Status: ", it.paymentAmountReceived.toString())
                    razorPayQRCode = it

                    etEmail.visibility = GONE
                    etPhone.visibility = GONE
                    generateQR.visibility = GONE

                    layoutShowQr.visibility = VISIBLE

                    if(razorPayQRCode.paymentAmountReceived == razorPayQRCode.paymentAmount && razorPayQRCode.paymentCountReceived== 1){
                        layoutPaymentSuccess.visibility = VISIBLE
                        layoutPaymentPending.visibility = GONE
                        layoutPaymentFailed.visibility = GONE

                        layoutShowQr.visibility = GONE

//                        etEmail.visibility = VISIBLE
//                        etPhone.visibility = VISIBLE
//                        generateQR.visibility = VISIBLE


                    }else if(razorPayQRCode.status != "active"){
                        layoutPaymentFailed.visibility = VISIBLE
                        layoutPaymentSuccess.visibility = GONE
                        layoutPaymentPending.visibility = GONE
                    }else{
                        layoutPaymentPending.visibility = VISIBLE
                        layoutPaymentFailed.visibility = GONE
                        layoutPaymentSuccess.visibility = GONE
                    }
                }else{
                    Toast.makeText(context,"Unable create QrCode",Toast.LENGTH_SHORT).show()

                }
            })
    }


    private fun refreshQrStatus(){
        showDialog("Getting Payment Status!")
        viewModel.getQRCodeStatus(razorPayQRCode.id)
    }

    fun Context.lifecycleOwner(): LifecycleOwner? {
        var curContext = this
        var maxDepth = 20
        while (maxDepth-- > 0 && curContext !is LifecycleOwner) {
            curContext = (curContext as ContextWrapper).baseContext
        }
        return if (curContext is LifecycleOwner) {
            curContext as LifecycleOwner
        } else {
            null
        }
    }


    private fun showDialog(title: String) {
        dialog = Dialog( context)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.loader_layout)
        val tvMessage = dialog!!.findViewById(R.id.tv_loader_message) as TextView
        tvMessage.text = title
        dialog!!.show()
    }

    private fun showQRCode(imgUrl: String) {
        qrDialog = Dialog( context)
        qrDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        qrDialog!!.setCancelable(true)
        qrDialog!!.setContentView(R.layout.qr_code_dialog)
//        val window: Window? = qrDialog!!.window
//        window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val ivQrView = qrDialog!!.findViewById(R.id.iv_qr_code) as ImageView
        val btnclose = qrDialog!!.findViewById(R.id.btn_close_qr) as Button

        Picasso.get().load(imgUrl).placeholder(R.drawable.progress_animation).into(ivQrView)

        btnclose.setOnClickListener {
            hideQrDialog()
        }

        qrDialog!!.show()
    }


    private fun hideDialog(){
        dialog?.hide()
    }

    private fun hideQrDialog(){
        qrDialog?.hide()
        refreshQrStatus()
    }

}