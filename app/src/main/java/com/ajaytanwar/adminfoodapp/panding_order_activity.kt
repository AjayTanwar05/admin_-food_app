package com.ajaytanwar.adminfoodapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.adminfoodapp.adapter.pandingOrder_adapter
import com.ajaytanwar.adminfoodapp.databinding.ActivityPandingOrderBinding
import com.ajaytanwar.adminfoodapp.modlle.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class panding_order_activity : AppCompatActivity() , pandingOrder_adapter.OnItemClicked {
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalprice: MutableList<String> = mutableListOf()
    private var listOfImageFirstfoodOrder: MutableList<String> = mutableListOf()
    private var listOfIOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference


    private lateinit var binding: ActivityPandingOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //initialization of database
        database = FirebaseDatabase.getInstance()

        //initialization of databaseReference

        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()



        binding = ActivityPandingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbutton.setOnClickListener {
            finish()
        }

    }

    private fun getOrderDetails() {

        //retrive
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfIOrderItem.add(it)
                    }
                }
                addDataToListForReyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForReyclerView() {
        for (oderItem in listOfIOrderItem) {

            //add data to respective

            oderItem.userName?.let { listOfName.add(it) }
            oderItem.totalamount?.let { listOfTotalprice.add(it) }
            oderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstfoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pandingRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = pandingOrder_adapter(
            this,
            listOfName,
            listOfTotalprice,
            listOfImageFirstfoodOrder,
            this
        )
        binding.pandingRecyclerView.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent = Intent(this, order_details_activity::class.java)
        val userOrderDetails = listOfIOrderItem[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcptedClickListener(position: Int) {
        // handle item accepted and update database

        val childeItemPushKey = listOfIOrderItem[position].itemPushKey
        val clickItemOrderReference = childeItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAccepetStatus(position)
    }

    override fun onItemDispatchClickListener(position: Int) {
        // handle item dispatch and update database
        val dispatchItemPushKey = listOfIOrderItem[position].itemPushKey
        val dispatchItemOrderReference = database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfIOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderItemReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderItemReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is Dispatch", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order is Not Dispatch", Toast.LENGTH_SHORT).show()
            }
    }




    private fun updateOrderAccepetStatus(position: Int) {
        //update order accepted in user history
        val userIdOfClickedItem= listOfIOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfIOrderItem[position].itemPushKey
        val buyHistoryReference = database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }
}