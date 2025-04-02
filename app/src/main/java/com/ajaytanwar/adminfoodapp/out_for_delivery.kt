package com.ajaytanwar.adminfoodapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.adminfoodapp.adapter.DeliveryAdapter
import com.ajaytanwar.adminfoodapp.databinding.ActivityOutForDeliveryBinding
import com.ajaytanwar.adminfoodapp.modlle.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class out_for_delivery : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database : FirebaseDatabase
    private var listOfCompletedOrder : ArrayList<OrderDetails> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backbutton.setOnClickListener {
            finish()
        }


        // reteive and d
        retriveCompletedOrderDetails()

    }

    private fun retriveCompletedOrderDetails() {

        // firebase database
        database = FirebaseDatabase.getInstance()
        val completedOrderReference = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completedOrderReference.addListenerForSingleValueEvent(object  :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //clear the list data populating it with new data
                listOfCompletedOrder.clear()

                for (orderSnapshot in snapshot.children){

                    val  competeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    competeOrder?.let {
                        listOfCompletedOrder.add(it)
                    }
                }

                listOfCompletedOrder.reverse()

                setDataIntoRecycler()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setDataIntoRecycler() {

        //
        val customerName = mutableListOf<String>()
        val MoneyStauts = mutableListOf<Boolean>()

        for (order in listOfCompletedOrder){
            order.userName?.let {
                customerName.add(it)
            }
            MoneyStauts.add(order.paymentReceived)
        }
        val adapter = DeliveryAdapter(customerName,MoneyStauts)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this )

    }
}