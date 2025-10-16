package com.example.todolistandroid

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {

    private lateinit var newTask: EditText
    private lateinit var btnAdd: Button
    private lateinit var prioText: TextView
    private lateinit var prioBar: SeekBar
    private lateinit var listView: ListView

    val listOfTasks = mutableListOf<Task>(
        Task(
            "Test task",
            "Priority: Normal"
        )
    )

    val listOfDoneTasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        newTask = findViewById(R.id.newTask)
        btnAdd = findViewById(R.id.btnAdd)
        prioText = findViewById(R.id.prioText)
        prioBar = findViewById(R.id.prioBar)
        listView = findViewById(R.id.listView)
        var priorityNow = "Priority: Normal"
        var priorityAfterBtn: String = "Priority Normal"

        val adapter = object: ArrayAdapter<Task>(
            this,
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            listOfTasks
        ){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val text1 = view.findViewById<TextView>(android.R.id.text1)
                val text2 = view.findViewById<TextView>(android.R.id.text2)

                val task = listOfTasks[position]

                text1.text = task.name
                text2.text = task.priority

                text1.setTextColor(Color.parseColor("#C9D1D9"))
                text2.setTextColor(Color.parseColor("#FFC107"))
                val typeface = ResourcesCompat.getFont(context, R.font.andika)
                text1.typeface = typeface
                text2.typeface = typeface
                text1.textSize = 23f
                text2.textSize = 15f
                return view
            }
        }
        listView.adapter = adapter

        listView.setOnItemClickListener{
            parent, view, position, id ->
            Toast.makeText(this, "task: \"${listOfTasks[position].name}\" is done now, Hold to delete.", Toast.LENGTH_SHORT).show()
            val text1 = view.findViewById<TextView>(android.R.id.text1)
            val text2 = view.findViewById<TextView>(android.R.id.text2)

            val isStriked = text1.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG != 0 //!=0 ustawione, ==0 nieustawione

            if(isStriked){
                text1.paintFlags = text1.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                text2.paintFlags = text1.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                listOfDoneTasks.add(text1.text.toString())



            }
            else{
                text1.paintFlags = text1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text2.paintFlags = text1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            }

        }

        listView.setOnItemLongClickListener{
            parent, view, position, id ->
                Toast.makeText(this, "deleted task: ${listOfTasks[position].name}.", Toast.LENGTH_SHORT).show()
            listOfTasks.removeAt(position)
            adapter.notifyDataSetChanged()
            true
        }


        btnAdd.setOnClickListener {
            if(newTask.text.toString().isEmpty()){
                Toast.makeText(this, "Enter not empty value", Toast.LENGTH_SHORT).show()
            }
            else{
                priorityAfterBtn = priorityNow
                Toast.makeText(this, "Added new task! - ${newTask.text.toString()}", Toast.LENGTH_SHORT).show()
                val newTaskObj = Task(newTask.text.toString(), priorityNow)
                listOfTasks.add(newTaskObj)
                adapter.notifyDataSetChanged()
                newTask.text = null



            }
        }

        prioBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val priorityText = when(progress){
                    0->"Priority: Normal"
                    1->"Priority: Important"
                    else->"Priority: VERY IMPORTANT"
                }
                prioText.text = priorityText
                priorityNow = priorityText


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

// TODO:
    //taski maja sie zapisywac do pamieci telefonu zeby nie znikaly
    //dodac date zeby pod albo nad albo obok nazwy aplikacji bylo day: ...
    //przycisk menu w ktorym mozna wybrac date z ktorego dnia robimy/czytamy todoliste
    //przycisk ktory przelacza widok miedzy zrobionymi, a niezrobionymi taskami:
    // 1 widok zrobione i niezrobione,
    // 2 widok zrobione
    // 3 widok niezrobione
    // klikniecie przelacza po prostu







    }
}


