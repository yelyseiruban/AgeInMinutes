package com.example.birthdayapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    var tvSelectedDate : TextView? = null
    var tvResultInMinutes : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDatePicker : Button = findViewById(R.id.btnSelectDate)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvResultInMinutes = findViewById(R.id.tvResultInMinutes)
        btnDatePicker.setOnClickListener {
            clickDatePicker()
        }
    }

    private fun clickDatePicker(){
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this,
            R.style.DatePickerDialogStyle,
            DatePickerDialog.OnDateSetListener{
                _, selectedYear, selectedMonth, selectedDay ->
                try{
                    checkDate(selectedYear, selectedMonth, selectedDay)
                    val selectedDate = setSelectedDate(selectedYear, selectedMonth, selectedDay)

                    val passedTimeInMinutes = calculatePassedTimeInMinutes(selectedDate);
                    tvResultInMinutes?.setText(passedTimeInMinutes.toString());
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(this,
                        "${e.message}", Toast.LENGTH_LONG).show();
                }
            },
            year, month, day
            )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis();
        datePickerDialog.show()
    }

    private fun setSelectedDate(year: Int, month: Int, day: Int) : String {
        val selectedDate = "$day/${month+1}/$year"
        tvSelectedDate?.setText(selectedDate)
        return selectedDate;
    }

    private fun checkDate(year: Int, month: Int, day: Int){
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val selectedDate = calendar.time
        val currentDate = Date()

        when {
            selectedDate.after(currentDate) -> {
                throw IllegalArgumentException("You should choose the date before today")
            }
        }

    }

    private fun calculatePassedTimeInMinutes(selectedDate: String): Long{
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val theDate = sdf.parse(selectedDate)
        val selectedDateInMinutes = (theDate?.time ?: 0) / 60000

        val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
        val currentDateInMinutes = (currentDate?.time ?: 0) / 60000

        val passedTimeInMinutes = currentDateInMinutes - selectedDateInMinutes;
        return passedTimeInMinutes;
    }
}