package com.devappsys.razorpaydemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.devappsys.razorpaydemo.view.PaymentView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var container: LinearLayout = findViewById(R.id.container)

        container.addView(PaymentView(this@MainActivity,this))
//        button.setOnClickListener {
//            createNewPaymentOrder()
//        }
    }
    
    


}