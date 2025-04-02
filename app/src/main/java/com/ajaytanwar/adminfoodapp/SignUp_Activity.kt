package com.ajaytanwar.adminfoodapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import com.ajaytanwar.adminfoodapp.databinding.ActivitySignUpBinding
import com.ajaytanwar.adminfoodapp.modlle.user_modlle
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.core.Tag
import com.google.firebase.database.database

class SignUp_Activity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var UserName: String
    private lateinit var nameofrestaurant: String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize Firebase Auth
        auth = Firebase.auth

        //initialize Firebase database
        database = Firebase.database.reference

        binding.createButton.setOnClickListener {

            UserName = binding.name.text.toString().trim()
            nameofrestaurant = binding.reasturantname.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (UserName.isBlank() || nameofrestaurant.isBlank() || email.isBlank() || password.isBlank() ) {
                Toast.makeText(this, "Please fill all detail", Toast.LENGTH_SHORT).show()
            } else {
                creataAccount(email, password)

            }

        }
        binding.alreadyhaveAccount.setOnClickListener {
            val intent = Intent(this,Login_Activity::class.java)
            startActivity(intent)

        }

        val locationList = arrayListOf("Jaipur", "Pali", "Ajmer", "Beawar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.ListOfoLocation
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun creataAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, Login_Activity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createaccount: Failure", task.exception)
            }

        }

    }

    private fun saveUserData() {
        UserName = binding.name.text.toString().trim()
        nameofrestaurant = binding.reasturantname.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = user_modlle(UserName,nameofrestaurant,email,password)
        val userId :String= FirebaseAuth.getInstance().currentUser!!.uid
        database.child("AdminUser").child(userId).setValue(user)

    }

}
