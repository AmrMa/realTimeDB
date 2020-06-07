package com.example.realtimedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import com.google.firebase.database.ValueEventListener as ValueEventListener

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val database = Firebase.database
    val myRef = database.getReference("message")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SendvalueBTN.setOnClickListener(this)
        sendData.setOnClickListener(this)
        reciveData.setOnClickListener(this)
        AddListnerToSingleValue()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.SendvalueBTN ->{
                myRef.child("myApp").child("sms").setValue(valueRT.text.toString())
            }
            R.id.sendData ->{
                var user = User(username.text.toString(),
                email.text.toString(),marrige.isChecked,salary.text.toString().toDouble()
                    )
                var userID = id.text.toString()
                myRef.child("users").child(userID).setValue(user)
            }
            R.id.reciveData->{
                var userID = id.text.toString()
                myRef.child("users").child(userID).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        try {
                            if(dataSnapshot !=null){
                                var usee = dataSnapshot.getValue(User::class.java)
                                username.setText(usee!!.username)
                                email.setText(usee!!.email)
                                salary.setText(usee!!.Salary.toString())
                                marrige.isChecked=usee!!.marriage!!

                            }
                        }catch (ex:Exception){
                            Toast.makeText(this@MainActivity,ex.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }
    fun AddListnerToSingleValue(){
        myRef.child("myApp").child("sms").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if(dataSnapshot !=null){
                        NameTxt.text = dataSnapshot.value as String
                    }
                }catch (ex:Exception){
                    Toast.makeText(this@MainActivity,ex.message,Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
@IgnoreExtraProperties
data class User(
    var username:String="",
    var email:String="",
    var marriage:Boolean?=false,
    var Salary:Double=0.0
)