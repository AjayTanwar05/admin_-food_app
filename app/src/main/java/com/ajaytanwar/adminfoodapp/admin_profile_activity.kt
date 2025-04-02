package com.ajaytanwar.adminfoodapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ajaytanwar.adminfoodapp.databinding.ActivityAdminProfileBinding
import com.ajaytanwar.adminfoodapp.modlle.user_modlle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class admin_profile_activity : AppCompatActivity() {

    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth :FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var AdminReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        AdminReference = database.reference.child("AdminUser")

        binding.backbutton.setOnClickListener {
            finish()
        }

        binding.saveInfoButton.setOnClickListener {
            updateUserData()
        }


        setContentView(binding.root)
        binding.Name.isEnabled = false
        binding.Address.isEnabled = false
        binding.Email.isEnabled = false
        binding.password.isEnabled = false
        binding.phone.isEnabled = false
        binding.saveInfoButton.isEnabled = false

        var isEnable = false
        binding.editbutton.setOnClickListener {
            isEnable = !isEnable
            binding.Name.isEnabled = isEnable
            binding.Address.isEnabled = isEnable
            binding.Email.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.saveInfoButton.isEnabled = isEnable
        }



        retriveUserData()
    }



    private fun retriveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid !=null){
            val userReference = AdminReference.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var  ownerName = snapshot.child("name").getValue()
                        var  email = snapshot.child("email").getValue()
                        var  password = snapshot.child("password").getValue()
                        var  address = snapshot.child("address").getValue()
                        var phone = snapshot.child("phone").getValue()

                        setDataToTextView(ownerName,email,password,address,phone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

    }

    private fun setDataToTextView(
        ownerName: Any?,
        email: Any?,
        password: Any?,
        address: Any?,
        phone: Any?
    ) {
        binding.Name.setText(ownerName.toString())
        binding.Email.setText(email.toString())
        binding.Address.setText(address.toString())
        binding.password.setText(password.toString())
        binding.phone.setText(phone.toString())
    }
    private fun updateUserData() {
        var updateName = binding.Name.text.toString()
       var updateEmail =binding.Email.text.toString()
       var updateAddress =binding.Address.text.toString()
       var updatePassword =binding.password.text.toString()
       var updatePhone =binding.phone.text.toString()
        var  currentUserUid = auth.currentUser?.uid
        if (currentUserUid !=null) {


            val userReference = AdminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("address").setValue(updateAddress)
            userReference.child("password").setValue(updatePassword)



            Toast.makeText(this, "Profile Update Seccefull", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)

        }else {
            Toast.makeText(this,"Profile Update Failed",Toast.LENGTH_SHORT).show()
        }
    }
}