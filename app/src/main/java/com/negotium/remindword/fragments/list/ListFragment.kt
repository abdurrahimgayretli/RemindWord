package com.negotium.remindword.fragments.list

import android.app.*
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.negotium.remindword.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

import android.content.Intent
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.negotium.remindword.*
import kotlinx.android.synthetic.main.fragment_list.*
import com.negotium.remindword.NotificationReceiver
import com.negotium.remindword.data.SettingDataBase
import com.negotium.remindword.model.Setting
import com.negotium.remindword.viewmodel.SettingViewModel


class ListFragment : Fragment()  {
    private val CHANNEL_ID = "channel_id_notify_01"
    private  val notificationId = 42
    private  lateinit var mWordViewModel: WordViewModel
    private  lateinit var mSettingViewModel: SettingViewModel
    var interval = "15"
    val adapter = ListAdapter()
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

        mSettingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        mWordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        mWordViewModel.readAllData.observe(viewLifecycleOwner, Observer{ word ->
            adapter.setData(word)
        })

        view.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        val settingDb= SettingDataBase.getInstance(requireContext())
        val settingDao = settingDb.settingDao()
        var data = settingDao.findByKey("Notification")
        if(data.equals(null)){
            val Setting = Setting(0,"Notification","False")
            mSettingViewModel.addSetting(Setting)
            data = settingDao.findByKey("Notification")
        }
        if(data.value == "True"){
            view.floatingAlarmButton.setImageResource(R.drawable.ic_baseline_alarm_off_24)
        }

        view.floatingAlarmButton.setOnClickListener {
            if(data.value == "False" && mWordViewModel.readAllData.value?.isEmpty() == false){
                intervalOkButton.isVisible = true
                intervalText.isVisible = true
                intervalCloseButton.isVisible = true
            }
            else if (data.value == "True"){
                settingDao.updateByKey(data.key,"False")
                floatingAlarmButton.setImageResource(R.drawable.ic_baseline_alarm_on_24)
                cancelNotification()
                Toast.makeText(requireContext(), "Successfully removed notification!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Please added word!!", Toast.LENGTH_SHORT).show()
            }
            data = settingDao.findByKey("Notification")
        }
        view.intervalOkButton.setOnClickListener{
            if(!intervalText.text.isEmpty()){
                settingDao.updateByKey(data.key,"True")
                intervalText.isVisible = false
                intervalCloseButton.isVisible = false
                intervalOkButton.isVisible = false
                interval = intervalText.text.toString()
                floatingAlarmButton.setImageResource(R.drawable.ic_baseline_alarm_off_24)
                Schdedulenotification()
                val imm: InputMethodManager =
                    requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                var view2 = requireActivity().currentFocus
                if (view2 == null) {
                    view2 = View(activity)
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                data = settingDao.findByKey("Notification")
                Toast.makeText(requireContext(), "Successfully created notification!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Please fill out field.", Toast.LENGTH_SHORT).show()
            }
        }
        view.intervalCloseButton.setOnClickListener{
            intervalText.isVisible = false
            intervalCloseButton.isVisible = false
            intervalOkButton.isVisible = false
            val imm: InputMethodManager =
                requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view1 = requireActivity().currentFocus
            if (view1 == null) {
                view1 = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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
    fun Schdedulenotification(){
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        intent.putExtra(titleExtra,"Dictionary")
        val pending = PendingIntent.getBroadcast(requireContext(), notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Schdedule notification
        val manager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setInexactRepeating(AlarmManager.RTC,System.currentTimeMillis(),
            (60000 * interval.toInt()).toLong(), pending)

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

    fun cancelNotification(){
        val intent = Intent(context, NotificationReceiver::class.java)
        val pending =
            PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val manager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
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