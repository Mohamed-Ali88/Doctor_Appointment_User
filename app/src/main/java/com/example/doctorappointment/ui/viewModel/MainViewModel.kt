package com.example.doctorappointment.ui.viewModel


import androidx.lifecycle.ViewModel
import com.example.doctorappointment.domen.CategoryModel
import com.example.doctorappointment.domen.DoctorsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MainViewModel : ViewModel() {

    fun fetchAllCategories(): Flow<List<CategoryModel>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Category")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = ArrayList<CategoryModel>()
                for (product in snapshot.children) {
                    val cat = product.getValue(CategoryModel::class.java)
                    categories.add(cat!!)
                }
                trySend(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        db.addValueEventListener(eventListener)
        awaitClose { db.removeEventListener(eventListener) }
    }

    fun fetchCategoryDoctors(categoryName : String): Flow<List<DoctorsModel>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Doctors")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val doctors = ArrayList<DoctorsModel>()
                for (product in snapshot.children) {
                    val doc = product.getValue(DoctorsModel::class.java)
                    if (doc?.Special == categoryName) {
                        doctors.add(doc!!)
                    }
                }
              trySend(doctors)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        db.addValueEventListener(eventListener)
        awaitClose { db.removeEventListener(eventListener) }
    }

    fun fetchAllDoctors(topDoctor: Boolean): Flow<List<DoctorsModel>> = callbackFlow {
        val db = FirebaseDatabase.getInstance().getReference("Doctors")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val doctors = ArrayList<DoctorsModel>()
                for (product in snapshot.children) {
                    val cat = product.getValue(DoctorsModel::class.java)
                    if (cat?.TopDoctor == topDoctor) {
                        doctors.add(cat!!)
                    } else if (!topDoctor) {
                        doctors.add(cat!!)
                    }
                }
                trySend(doctors)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        db.addValueEventListener(eventListener)
        awaitClose { db.removeEventListener(eventListener) }
    }

}