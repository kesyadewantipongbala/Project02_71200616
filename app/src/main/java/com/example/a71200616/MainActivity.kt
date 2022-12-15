package com.example.a71200616

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
   val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtJudul = findViewById<EditText>(R.id.edtJudul)
        val edtTanggal = findViewById<EditText>(R.id.edtTanggal)
        val edtIsi = findViewById<EditText>(R.id.edtIsi)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnHapus = findViewById<Button>(R.id.btnHapus)
        val btnCari = findViewById<Button>(R.id.btnCari)


        btnSimpan.setOnClickListener{
            val catatan = Catatan(edtJudul.text.toString(), edtTanggal.text.toString(), edtIsi.text.toString())
            db.collection("catatan").document(edtJudul.text.toString()).set(catatan)
            edtJudul.setText("")
            edtTanggal.setText("")
            edtIsi.setText("")
            refreshData()
        }

        btnHapus.setOnClickListener{
            db.collection("catatan").document(edtJudul.text.toString()).delete()
            refreshData()
        }

        btnCari.setOnClickListener{
            cari(edtJudul.text.toString())
        }

    }

    fun refreshData() {
        db.collection("catatan")
            .get()
            .addOnSuccessListener { result ->
                val txvHasil = findViewById<TextView>(R.id.txvHasil)
                var hasil = ""
                for (doc in result){
                    hasil += "${doc.get("judul")} - ${doc.get("tanggal")}\n${doc.get("isi")}\n"
                }
                txvHasil.setText(hasil)
            }
    }

    fun cari(judul : String){
        db.collection("catatan")
            .document(judul)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.exists()){
                val doc = snapshot.data
                val txvHasil = findViewById<TextView>(R.id.txvHasil)
                txvHasil.setText("${doc!!.get("judul")} - ${doc!!.get("tanggal")}\n${doc!!.get("isi")}\n")
            }
    }
    }
}