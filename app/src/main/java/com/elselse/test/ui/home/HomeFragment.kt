package com.elselse.project1.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elselse.test.MyAdapter
import com.elselse.test.R
import com.elselse.test.TheProject
import com.elselse.test.UserDao
import com.elselse.test.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var items:List<TheProject>


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()
        val recyclerView = binding.recyclerView
        fun setList(items:List<TheProject>){
            recyclerView.adapter = MyAdapter(items);
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        items = userDao.getAll()
        setList(items)

        val fabAddBtn = binding.fabAddBtn
        fabAddBtn.setOnClickListener(View.OnClickListener {
            dialogBuilder = AlertDialog.Builder(requireContext())
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


        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@Database(entities = [TheProject::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}