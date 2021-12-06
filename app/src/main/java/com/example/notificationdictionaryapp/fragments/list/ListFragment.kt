package com.example.notificationdictionaryapp.fragments.list

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notificationdictionaryapp.R
import com.example.notificationdictionaryapp.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*
import android.app.AlarmManager

import android.app.PendingIntent

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.SystemClock
import com.example.notificationdictionaryapp.NotificationReceiver
import com.example.notificationdictionaryapp.messageExtra
import com.example.notificationdictionaryapp.titleExtra
import java.util.*
import android.R.string.no





class ListFragment : Fragment() {
    private val CHANNEL_ID = "channel_id_notify_01"
    private  val notificationId = 42
    private  lateinit var mWordViewModel: WordViewModel
    val adapter = ListAdapter()
    var notifyText = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)
        createNotificationChannel()
        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mWordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        mWordViewModel.readAllData.observe(viewLifecycleOwner, Observer{ word ->
            adapter.setData(word)
        })
        notifyText = mWordViewModel.readAllData.value?.get(0)?.englishWord + " = " + mWordViewModel.readAllData.value?.get(0)?.turkishWord
        view.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        view.button.setOnClickListener {scheduleNotification() }
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
    private  fun sendNotification(){
        val builder = NotificationCompat.Builder(requireContext(),CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Notify Title")
            .setContentText(notifyText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(requireContext())){
            notify(notificationId,builder.build())
        }

    }
    fun scheduleNotification() {
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        intent.putExtra(titleExtra,"title")
        intent.putExtra(messageExtra, mWordViewModel.readAllData.value?.get(0)?.englishWord + " = " + mWordViewModel.readAllData.value?.get(0)?.turkishWord)
        val pending =
            PendingIntent.getBroadcast(requireContext(), notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Schdedule notification
        val manager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), 60000, pending);
    }
    private  fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    fun cancelNotification() {
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        intent.putExtra("title", "title")
        intent.putExtra("text", notifyText)
        val pending =
            PendingIntent.getBroadcast(requireContext(), notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Cancel notification
        val manager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.cancel(pending)
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