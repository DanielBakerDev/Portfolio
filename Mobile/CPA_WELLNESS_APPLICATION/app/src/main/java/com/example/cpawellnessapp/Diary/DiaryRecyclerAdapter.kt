package com.example.cpawellnessapp.Diary

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.cpawellnessapp.BuildConfig
import com.example.cpawellnessapp.DiaryList
import com.example.cpawellnessapp.R
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File

class DiaryRecyclerAdapter(private val diaries: ArrayList<Diary>) :
    RecyclerView.Adapter<DiaryRecyclerAdapter.MyViewHolder>() {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        // LW Renamed textview to view
        class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): DiaryRecyclerAdapter.MyViewHolder {
            // create a new view
            // LW changed textview to view and set the layout to the xml created
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.diary_menu_item_layout, parent, false) as View
            // set the view's size, margins, paddings and layout parameters
            val lp = view.layoutParams
            lp.height = DiaryList.width / 4
            view.layoutParams = lp
            return MyViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            // LW set the item to bind through view.textview
            val retriever = FFmpegMediaMetadataRetriever()



            try {
                val score = (diaries[position].smileScore * 100).toInt()
                holder.view.findViewById<TextView>(R.id.diaryDate).text = "Date Recorded: " + diaries[position].dateRecorded.toString()
                holder.view.findViewById<TextView>(R.id.diaryId).text = "Diary #" + diaries[position].Id.toString()
                holder.view.findViewById<TextView>(R.id.diaryScore).text = "Happiness Score: " + score.toString()
                var path = File(DiaryList.mediaDir?.get(0)?.path.toString()+"/videoDiaries");
                val videoFile = File(path, diaries[position].fileURI)
                retriever.setDataSource(videoFile.path);
                val duration = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)?.toDouble()
                //Get the frame at the halfway point
                var bitmap: Bitmap? = (duration?.div(2))?.toLong()?.let {
                    retriever.getFrameAtTime(
                        it,
                        MediaMetadataRetriever.OPTION_CLOSEST
                    )
                }
                holder.view.findViewById<ImageView>(R.id.diaryThumbnail).background = BitmapDrawable(bitmap);

                holder.view.findViewById<ImageButton>(R.id.playButton).setOnClickListener(View.OnClickListener() {

                        val intent = Intent().apply {
                            action = Intent.ACTION_VIEW
                            type = MimeTypeMap.getSingleton()
                                .getMimeTypeFromExtension(videoFile.extension)
                            val authority = "${BuildConfig.APPLICATION_ID}.provider"
                            data = FileProvider.getUriForFile(it.context, authority, videoFile)
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                    it.context.startActivity(intent);
                });
            }
            catch( e: Exception) {

            }
            finally {
                retriever.release()
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = diaries.size
}