package com.example.notificationdictionaryapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notificationdictionaryapp.R
import com.example.notificationdictionaryapp.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment() {

    private  lateinit var mWordViewModel: WordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)

        val adapter = ListAdapter()
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mWordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        mWordViewModel.readAllData.observe(viewLifecycleOwner, Observer{ word ->
            adapter.setData(word)
        })

        view.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteAllWords()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllWords() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            mWordViewModel.deleteAllWords()
            Toast.makeText(requireContext(), "Successfully removed everything", Toast.LENGTH_SHORT).show()

        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete everything")
        builder.setMessage("Are you sure you want to delete everything")
        builder.create().show()
    }
}