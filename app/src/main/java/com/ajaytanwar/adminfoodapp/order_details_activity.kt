package com.ajaytanwar.adminfoodapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.adminfoodapp.adapter.OrderDetailsAdapter
import com.ajaytanwar.adminfoodapp.databinding.ActivityOrderDetailsBinding
import com.ajaytanwar.adminfoodapp.modlle.OrderDetails

class order_details_activity : AppCompatActivity() {

    private val binding : ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName :String? = null
    private var addreses :String? = null
    private var phoneNumber :String? = null
    private var totalPrice :String? = null
    private  var foodNames : ArrayList<String> = arrayListOf()
    private  var foodImages : ArrayList<String> = arrayListOf()
    private  var foodQuantity : ArrayList<Int> = arrayListOf()
    private  var foodPrices : ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()

    }

    private fun getDataFromIntent() {

        val recevedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        recevedOrderDetails?.let { orderDetails ->
                userName = recevedOrderDetails.userName
                foodNames = recevedOrderDetails.foodName as ArrayList<String>
                foodImages= recevedOrderDetails.foodImages as ArrayList<String>
                foodQuantity = recevedOrderDetails.foodQuantity as ArrayList<Int>
                addreses = recevedOrderDetails.address
                phoneNumber = recevedOrderDetails.phoneNumber
                foodPrices = recevedOrderDetails.foodprice as ArrayList<String>
                totalPrice = recevedOrderDetails.totalamount

                setUserDetails()
                setAdapter()



        }

    }

    private fun setAdapter() {
        binding.OrderdetailsRecycler.layoutManager = LinearLayoutManager(this)
        val adapter =OrderDetailsAdapter(this,foodNames,foodImages,foodQuantity,foodPrices)
        binding.OrderdetailsRecycler.adapter =adapter
    }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = addreses
        binding.totalPrice.text = totalPrice
        binding.phone.text = phoneNumber


    }
}