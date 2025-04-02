package com.ajaytanwar.adminfoodapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ajaytanwar.adminfoodapp.databinding.ActivityLoginBinding
import com.ajaytanwar.adminfoodapp.modlle.user_modlle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class Login_Activity : AppCompatActivity() {
    private var UserName: String? = null
    private var nameofrestaurant: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()


        // initialize Firebase Auth
        auth = Firebase.auth

        //initialize firebase database
        database = Firebase.database.reference

        //google Sign in
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.loginbutton.setOnClickListener {
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "please Fill all Detail", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount()
            }
        }
        binding.googlebutton.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
        binding.DontHaveAccount.setOnClickListener {
            val intent = Intent(this, SignUp_Activity::class.java)
            startActivity(intent)
        }

    }


    private fun createUserAccount() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                UpdateUi(user)

            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            Toast.makeText(
                                this,
                                "Create User and Login Successfully ",
                                Toast.LENGTH_SHORT
                            ).show()
                            saveUserData()
                            UpdateUi(user)
                        } else {
                            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                            Log.d(
                                "Account",
                                "createUserAccount: Authentication failed",
                                task.exception
                            )
                        }
                    }
            }
        }


    }

    private fun saveUserData() {
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = user_modlle(UserName, nameofrestaurant, email, password)
        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("AdminUser").child(userId).setValue(user)
        }

    }


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // Successfully sign in with google
                            Toast.makeText(
                                this, "Successfully sign in with google", Toast.LENGTH_SHORT).show()
                            UpdateUi(authTask.result?.user)
                            finish()

                        } else {
                            Toast.makeText(this, " Google Sign-in failed  ", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, " Google Sign-in failed  ", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onStart() {
        super.onStart()
        val currentUser  =  auth.currentUser
        if (currentUser!=null)  {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }
    private fun UpdateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

}
