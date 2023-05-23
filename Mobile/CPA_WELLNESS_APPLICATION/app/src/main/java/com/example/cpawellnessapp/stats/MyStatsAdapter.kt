package com.example.cpawellnessapp.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cpawellnessapp.Diary.Diary
import com.example.cpawellnessapp.R

// LW Change to ArrayList
class MyStatsAdapter(private val myDataset: ArrayList<Diary>) :
        RecyclerView.Adapter<MyStatsAdapter.MyViewHolder>() {

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyStatsAdapter.MyViewHolder {
        // LW create a new view, use the layout,   change TextView to View
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.stats_item_layout, parent, false) as View
        // LW set the view's size, margins, paddings and layout parameters
        val lp = view.getLayoutParams()
        lp.height = parent.measuredHeight/6
        view.setLayoutParams(lp)
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        var score = (myDataset[position].smileScore * 100).toInt()
        var entry:String = "Date: " + myDataset[position].dateRecorded.toString() + " - Smile score: " + score.toString() + " - Entry: " + myDataset[position].entryNumber.toString()
        holder.view.findViewById<TextView>(R.id.diaryDate).text = entry

//        myDataset.forEach {
//            var score = (it.smileScore * 100).toInt()
//            var entry:String = "Date: " + it.dateRecorded.toString() + " - Smile score: " + score.toString() + " - Entry: " + it.entryNumber.toString()
//            holder.view.findViewById<TextView>(R.id.diaryDate).text = entry
//        }

        //holder.view.findViewById<TextView>(R.id.textViewRecyclerItem).text = myDataset[position]..toString()
        // LW use the view and drill down to the textview in the layout
//        holder.view.findViewById<TextView>(R.id.textViewRecyclerItem1).text = myDataset[position].name.toString()
//        holder.view.findViewById<TextView>(R.id.textViewRecyclerItem1).text = myDataset[position].name.toString()
//        holder.view.findViewById<TextView>(R.id.textViewRecyclerItem2).text = myDataset[position].pokedex.toString()
//        holder.view.findViewById<TextView>(R.id.textViewRecyclerItem3).text = myDataset[position].type.toString()
//        holder.view.findViewById<TextView>(R.id.textViewRecyclerItem4).text = myDataset[position].catchRate.toString()

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}