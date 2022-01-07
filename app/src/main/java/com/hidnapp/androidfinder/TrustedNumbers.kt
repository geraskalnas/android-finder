package com.hidnapp.androidfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.hidnapp.androidfinder.databinding.ActivityTrustedNumbersBinding
//import androidx.recyclerview.widget.RecyclerView
//import android.R

class TrustedNumbers : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTrustedNumbersBinding

    private var courseNameEdt: EditText? = null  // creating variables for our ui components.
    //private var courseDescEdt: EditText? = null
    private var addBtn: Button? = null
    private  var saveBtn: Button? = null
    private var numberRV: RecyclerView? = null

    // variable for our adapter class and array list
    private var adapter: RVItemAdapter? = null
    private var numberModalArrayList: ArrayList<RVItemModal>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrustedNumbersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //val navController = findNavController(R.id.nav_host_fragment_content_trusted_numbers)
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)

        courseNameEdt = findViewById(R.id.idEdtCourseName)
        //courseDescEdt = findViewById(R.id.idEdtCourseDescription)
        addBtn = findViewById(R.id.idBtnAdd)
        saveBtn = findViewById(R.id.idBtnSave)
        numberRV = findViewById(R.id.idRVCourses)

        loadData()

        buildRecyclerView()

        addBtn?.setOnClickListener(View.OnClickListener() {
            numberModalArrayList!!.add(
                RVItemModal(
                    courseNameEdt?.getText().toString(), ""
                    //courseDescEdt.getText().toString()
                )
            )
            // notifying adapter when new data added.
            adapter!!.notifyItemInserted(numberModalArrayList!!.size)
        })


        saveBtn?.setOnClickListener(View.OnClickListener {
            saveData()
        })

        //binding.fab.setOnClickListener { view -> 2*2
        //}
    }

    override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_trusted_numbers)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }

    private fun buildRecyclerView() {
        adapter = RVItemAdapter(numberModalArrayList, this@TrustedNumbers)

        val manager = LinearLayoutManager(this)
        numberRV!!.setHasFixedSize(true)

        numberRV!!.layoutManager = manager

        numberRV!!.adapter = adapter
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        val gson = Gson()

        val json = sharedPreferences.getString("tn", null)

        val type: Type = object : TypeToken<ArrayList<RVItemModal?>?>() {}.getType()

        numberModalArrayList = gson.fromJson(json, type)

        if (numberModalArrayList == null) {
            numberModalArrayList = ArrayList()
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        val gson = Gson()

        val json: String = gson.toJson(numberModalArrayList)

        editor.putString("tn", json)

        editor.apply()

        Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show()
    }
}