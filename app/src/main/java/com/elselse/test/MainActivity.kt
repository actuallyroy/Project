package com.elselse.test

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import com.elselse.test.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var items:List<TheProject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()
        val recyclerView:RecyclerView = findViewById(R.id.recyclerView)
        fun setList(items:List<TheProject>){
            recyclerView.adapter = MyAdapter(items);
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        items = userDao.getAll()
        setList(items)

        val fabAddBtn = findViewById<FloatingActionButton>(R.id.fabAddBtn)
        fabAddBtn.setOnClickListener(View.OnClickListener {
            dialogBuilder = AlertDialog.Builder(this)
            val popupView = layoutInflater.inflate(R.layout.popup, null)
            dialogBuilder.setView(popupView)
            dialog = dialogBuilder.create();
            dialog.show();

            val addBtn = popupView.findViewById<Button>(R.id.add_button)
            val cancelBtn = popupView.findViewById<Button>(R.id.cancel_button)
            cancelBtn.setOnClickListener(View.OnClickListener {
                dialog.cancel();
            })

            addBtn.setOnClickListener(View.OnClickListener {
                val tempID = popupView.findViewById<EditText>(R.id.idText).text.toString()
                var id:Long = 0
                if(!tempID.equals("")){
                    id = tempID.toLong()
                }
                val logic = popupView.findViewById<EditText>(R.id.logicText).text.toString()
                Log.d("ID", id.toString())
                val theProject = TheProject(id, logic)
                userDao.insertAll(theProject)
                items = userDao.getAll()
                setList(items)
                dialog.cancel()
            })
        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}


@Entity(tableName = "yes_thats_me")
data class TheProject(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val code: Long,
    @ColumnInfo(name = "Logic")
    val anything: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM yes_thats_me")
    fun getAll(): List<TheProject>


    @Query("SELECT * FROM yes_thats_me WHERE id LIKE :code")
    fun findById(code: Long): TheProject

    @Insert
    fun insertAll(vararg theProject: TheProject)

    @Delete
    fun delete(theProject: TheProject)

    @Query("DELETE FROM yes_thats_me")
    fun deleteAll()
}

@Database(entities = [TheProject::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}