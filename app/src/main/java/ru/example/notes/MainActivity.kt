package ru.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.navigation.Navigation
import ru.example.notes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mainMenu: Menu? = null
    private var menuShowing = true
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Notes"
        setSupportActionBar(toolbar)
        
//        binding.addNoteBtn.setOnClickListener {
//            Navigation.findNavController(binding.root).navigate(R.id.action_navigation_home_to_navigation_create_note)
//        }

        //setSupportActionBar(findViewById(R.id.toolbar))
//        val navController = Navigation.findNavController(binding.root)
//        NavigationUI.setupWithNavController(toolbar, navController)
        //supportActionBar?.hide()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar_menu, menu)
//        mainMenu = menu
//        return super.onCreateOptionsMenu(menu)
//    }
}