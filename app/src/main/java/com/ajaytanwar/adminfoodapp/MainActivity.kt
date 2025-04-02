package com.ajaytanwar.adminfoodapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ajaytanwar.adminfoodapp.databinding.ActivityMainBinding
import com.ajaytanwar.adminfoodapp.modlle.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database : FirebaseDatabase
    private lateinit var auth  :FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()



        binding.addMenu.setOnClickListener {
            val intent = Intent(this, Additem_activity::class.java)
            startActivity(intent)
        }

        binding.AllItemMenu.setOnClickListener {
            val intent = Intent(this, all_item_activity::class.java)
            startActivity(intent)
        }


        binding.profileMain.setOnClickListener {
            val intent = Intent(this, admin_profile_activity::class.java)
            startActivity(intent)
        }

        binding.newUser.setOnClickListener {
            val intent = Intent(this, New_account_activity::class.java)
            startActivity(intent)

        }
        binding.outForDeliverybutton.setOnClickListener {
            val intent = Intent(this, out_for_delivery::class.java)
            startActivity(intent)
        }

        binding.pandingOrder.setOnClickListener {
            val intent = Intent(this, panding_order_activity::class.java)
            startActivity(intent)
        }

        binding.logOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Login_Activity::class.java))
            finish()
        }


        pendingOrders()

        completedOrder()

        wholeTimeErning()
    }

    private fun wholeTimeErning() {
        var  listOfTotalPay = mutableListOf<Int>()
        completedOrderReference =FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (orderSnapshot in snapshot.children){
                    var completeOrder =orderSnapshot.getValue(OrderDetails::class.java)

                    completeOrder?.totalamount?.replace("₹","")?.toIntOrNull()
                        ?.let { i ->
                            listOfTotalPay.add(i)
                        }
                }
                binding.wholeTimeEarning.text = listOfTotalPay.sum().toString() + "₹"

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun completedOrder() {
        val  completedOrderReference = database.reference.child("CompletedOrder")
        var completedOrderCount = 0
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                completedOrderCount =snapshot.childrenCount.toInt()
                binding.CompletedOrderCount.text = completedOrderCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        val  pandingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderCount = 0
        pandingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                pendingOrderCount =snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingOrderCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}