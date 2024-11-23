package com.example.vinilos.ui.views

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos.R
import com.example.vinilos.data.models.Album
import com.example.vinilos.databinding.ActivityCreateAlbumBinding
import com.example.vinilos.ui.viewmodels.AlbumViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateAlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAlbumBinding
    private lateinit var albumViewModel: AlbumViewModel
    private var lastSelectedDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(application)
        )[AlbumViewModel::class.java]

        val etDescription = binding.etAlbumDescription
        val charCounter = binding.charCounterDescription

        val adapterGenre = ArrayAdapter.createFromResource(
            this,
            R.array.genre_options,
            R.layout.score_item
        ).apply {
            setDropDownViewResource(R.layout.score_item_dropdown)
        }

        val adapterRecrdLabel = ArrayAdapter.createFromResource(
            this,
            R.array.recordLabel_options,
            R.layout.score_item
        ).apply {
            setDropDownViewResource(R.layout.score_item_dropdown)
        }

        binding.spinnerGenre.adapter = adapterGenre
        binding.spinnerRecordLabel.adapter = adapterRecrdLabel

        etDescription.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val currentLength = s?.length ?: 0
                val maxLength = 500
                charCounter.text = getString(R.string.char_counter_format, currentLength, maxLength)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val etDate = binding.etAlbumReleaseDate

        etDate.setOnClickListener {
            val locale = Locale("es", "ES")
            Locale.setDefault(locale)
            val calendar = Calendar.getInstance()
            val initialCalendar = lastSelectedDate ?: calendar
            val year = initialCalendar.get(Calendar.YEAR)
            val month = initialCalendar.get(Calendar.MONTH)
            val day = initialCalendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", locale)
                    val formattedDate = dateFormat.format(selectedDate.time)
                    val formattedDateWithCapitalMonth = formattedDate.replaceFirstChar { it.uppercase() }
                    etDate.setText(formattedDateWithCapitalMonth)
                    lastSelectedDate = selectedDate
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        binding.btnCreateAlbumBack.setOnClickListener{
            finish()
        }

        binding.btnSubmitAlbum.setOnClickListener {
            val name = binding.etAlbumName.text.toString().trim()
            val cover = binding.etAlbumCover.text.toString().trim()
            val releaseDate = binding.etAlbumReleaseDate.text.toString().trim()
            val description = binding.etAlbumDescription.text.toString().trim()
            val genre = binding.spinnerGenre.selectedItem.toString()
            val recordLabel = binding.spinnerRecordLabel.selectedItem.toString()

            val inputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale("es", "CO"))
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale("es", "CO"))
            val parsedDate = inputFormat.parse(releaseDate)
            val apiDate = outputFormat.format(parsedDate!!)
            val dateWithTimeZone = apiDate.substring(0, apiDate.length - 2) + ":" + apiDate.substring(apiDate.length - 2)

            if (name.isNotEmpty() && cover.isNotEmpty() && releaseDate.isNotEmpty()
                && description.isNotEmpty() && genre != "Selecciona un género" && recordLabel != "Selecciona una opción"
            ) {
                val newAlbum = Album(
                    id = 0,
                    name = name,
                    cover = cover,
                    releaseDate = dateWithTimeZone,
                    description = description,
                    genre = genre,
                    recordLabel = recordLabel,
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                )
                albumViewModel.createAlbum(newAlbum)
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}