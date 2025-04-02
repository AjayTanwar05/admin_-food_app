package com.ajaytanwar.adminfoodapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ajaytanwar.adminfoodapp.databinding.ActivityAdditemBinding
import com.ajaytanwar.adminfoodapp.modlle.all_menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Additem_activity : AppCompatActivity() {

    private lateinit var foodname: String
    private lateinit var foodprice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredirnt: String
    private var foodImageUri: Uri? = null

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAdditemBinding by lazy {
        ActivityAdditemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        //initialize Firebase
        auth = FirebaseAuth.getInstance()

        //Initialize Firebase database instance
        database = FirebaseDatabase.getInstance()

        binding.addItem.setOnClickListener {
            foodname = binding.foodName.text.toString().trim()
            foodprice = binding.foodItemPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredirnt = binding.ingredient.text.toString().trim()

            if (!(foodname.isBlank() || foodprice.isBlank() || foodDescription.isBlank() || foodIngredirnt.isBlank())) {
                uploadData()
                Toast.makeText(this, "Item add Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill All the details", Toast.LENGTH_SHORT).show()

            }
        }
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }


        binding.backbutton.setOnClickListener {
            finish()
        }
    }


    private fun uploadData() {

        //get a references to the menu node in the database

        val menuRef = database.getReference("menu")


        //generate a unique key for the main menu item
        val newItemKey = menuRef.push().key


        if (foodImageUri != null) {

            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->

                    //Create a nre item
                    val newItem = all_menu(
                        newItemKey,
                        foodname = foodname,
                        foodprice = foodprice,
                        foodDescription = foodDescription,
                        foodIngredirnt = foodIngredirnt,
                        foodImage = downloadUri.toString(),

                        )
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "data uploaded successfully", Toast.LENGTH_SHORT)
                                .show()

                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "data  uploaded Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }


            }.addOnFailureListener {
                Toast.makeText(this, "data  uploaded Failed", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "please selected an image  ", Toast.LENGTH_SHORT).show()
        }

    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.selectedImage.setImageURI(uri)
                foodImageUri = uri

            }
        }

}
