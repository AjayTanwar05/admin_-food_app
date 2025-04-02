package com.ajaytanwar.adminfoodapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajaytanwar.adminfoodapp.adapter.menu_item_adapter
import com.ajaytanwar.adminfoodapp.databinding.ActivityAllItemBinding
import com.ajaytanwar.adminfoodapp.modlle.all_menu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class all_item_activity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var menuItems: ArrayList<all_menu> = ArrayList()
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()

        binding.backbutton.setOnClickListener {
            finish()

        }
    }

        private fun retrieveMenuItem() {
           database = FirebaseDatabase.getInstance()
            val foodRef: DatabaseReference = database.reference.child("menu")

            //fetch data from data base
            foodRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    //Clear exacting data before populating
                    menuItems.clear()

                    //loop for though each food item
                    for (foodSnapshot in snapshot.children) {
                        val menuItem = foodSnapshot.getValue(all_menu::class.java)
                        menuItem?.let {
                            menuItems.add(it)
                        }
                    }
                    setAdapter()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("databaseError","Error: ${error.message}")
                }

            })
        }

        private fun setAdapter() {
            val adapter = menu_item_adapter(this@all_item_activity, menuItems, databaseReference){position ->
                deleteMenuItems(position)
            }
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.menuRecyclerView.adapter = adapter
        }

    private fun deleteMenuItems(position: Int) {
        val  menuItemtoDelete = menuItems[position]
        val menuItemKey =menuItemtoDelete.Key
        val foodMenuReference =database.reference.child("menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener{ task ->
            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.menuRecyclerView.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this,"Item Not Delete",Toast.LENGTH_SHORT).show()
            }
        }
    }
}







