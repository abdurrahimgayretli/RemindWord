package com.negotium.remindword.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jwang123.flagkit.FlagKit
import com.negotium.remindword.R
import com.negotium.remindword.model.Word
import kotlinx.android.synthetic.main.custom_row.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private lateinit var currentItem: Word
    private var wordList = emptyList<Word>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row,parent,false))

    }

    override fun getItemCount(): Int {
        return  wordList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = wordList[position]
        holder.itemView.englishWord_txt.text = currentItem.fromWord
        holder.itemView.turkishWord_txt.text = currentItem.toWord
        if(currentItem.fromFlag == "en"){
            holder.itemView.fromImageList.setImageDrawable(FlagKit.drawableWithFlag(holder.itemView.getContext(), "gb"))
        }else{
            holder.itemView.fromImageList.setImageDrawable(FlagKit.drawableWithFlag(holder.itemView.getContext(), currentItem.fromFlag))
        }
        if(currentItem.toFlag == "en"){
            holder.itemView.toImageList.setImageDrawable(FlagKit.drawableWithFlag(holder.itemView.getContext(), "gb"))
        }else{
            holder.itemView.toImageList.setImageDrawable(FlagKit.drawableWithFlag(holder.itemView.getContext(), currentItem.toFlag))
        }
        holder.itemView.rowLayout.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }


    fun setData(word: List<Word>){
        this.wordList=word
        notifyDataSetChanged()
    }
}