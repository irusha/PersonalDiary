package lk.isoft.timetable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private val listener: OnItemClickListener) :
RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }
    val daysofmonth1 = MainActivity.Companion.days
    val daysofmonth = daysofmonth1.map { it.toString() }.toTypedArray()
    val dow = MainActivity.Companion.daysofweek
    val indtoday = MainActivity.Companion.indtoday
    val message = MainActivity.Companion.message
    //val d = arrayListOf<String>("A","B","C")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.day.text = daysofmonth[position]
        holder.dayofweek.text = dow[position]
        holder.itoday.text = indtoday[position]
        holder.message.text = message[position]
    }

    override fun getItemCount(): Int {
        return daysofmonth.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var day: TextView
        var dayofweek: TextView
        var itoday: TextView
        var message: TextView

        init {
            day = itemView.findViewById(R.id.textView8)
            dayofweek = itemView.findViewById(R.id.textView9)
            itoday = itemView.findViewById(R.id.indtoday)
            message = itemView.findViewById(R.id.textView2)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            //
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.OnItemClick(position)
            }

        }

    }
}
