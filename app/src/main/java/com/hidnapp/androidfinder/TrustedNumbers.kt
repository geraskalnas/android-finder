package com.hidnapp.androidfinder

import android.content.DialogInterface
import android.content.SharedPreferences
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
import androidx.navigation.ui.setupActionBarWithNavController
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
    private var courseRV: RecyclerView? = null

    // variable for our adapter class and array list
    private var adapter: CourseAdapter? = null
    private var courseModalArrayList: ArrayList<CourseModal>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrustedNumbersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        //val navController = findNavController(R.id.nav_host_fragment_content_trusted_numbers)
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)

        // initializing our variables.
        // initializing our variables.
        courseNameEdt = findViewById(R.id.idEdtCourseName)
        //courseDescEdt = findViewById(R.id.idEdtCourseDescription)
        addBtn = findViewById(R.id.idBtnAdd)
        saveBtn = findViewById(R.id.idBtnSave)
        courseRV = findViewById(R.id.idRVCourses)

        // calling method to load data
        // from shared prefs.

        // calling method to load data
        // from shared prefs.
        loadData()

        // calling method to build
        // recycler view.

        // calling method to build
        // recycler view.
        buildRecyclerView()

        // on click listener for adding data to array list.


        // on click listener for adding data to array list.
        addBtn?.setOnClickListener(View.OnClickListener() {
            courseModalArrayList!!.add(
                CourseModal(
                    courseNameEdt?.getText().toString(), "2"
                    //courseDescEdt.getText().toString()
                )
            )
            // notifying adapter when new data added.
            adapter!!.notifyItemInserted(courseModalArrayList!!.size)
        })


        saveBtn?.setOnClickListener(View.OnClickListener {
            saveData()
        })

        binding.fab.setOnClickListener { view -> 2*2
        }
    }

    override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_trusted_numbers)
    return navController.navigateUp(appBarConfiguration)
            || super.onSupportNavigateUp()
    }

    private fun buildRecyclerView() {
        adapter = CourseAdapter(courseModalArrayList, this@TrustedNumbers)

        val manager = LinearLayoutManager(this)
        courseRV!!.setHasFixedSize(true)

        courseRV!!.layoutManager = manager

        courseRV!!.adapter = adapter
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        val gson = Gson()

        val json = sharedPreferences.getString("courses", null)

        val type: Type = object : TypeToken<ArrayList<CourseModal?>?>() {}.getType()

        courseModalArrayList = gson.fromJson(json, type)

        if (courseModalArrayList == null) {
            courseModalArrayList = ArrayList()
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        val gson = Gson()

        val json: String = gson.toJson(courseModalArrayList)

        editor.putString("courses", json)

        editor.apply()

        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show()
    }
}