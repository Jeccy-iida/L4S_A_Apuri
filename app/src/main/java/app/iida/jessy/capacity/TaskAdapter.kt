package app.iida.jessy.capacity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.taskitem.view.*
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter (
    private val context: Context,
    private var taskList: OrderedRealmCollection<Task>?,
    private var listener: OnItemClickListener,
    private val autoUpdate: Boolean
) :
    RealmRecyclerViewAdapter<Task, TaskAdapter.TaskViewHolder>(taskList, autoUpdate) {

    override fun getItemCount(): Int = taskList?.size ?: 0

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task: Task = taskList?.get(position) ?: return

        //ListItemの情報
        holder.date.setOnClickListener {
            listener.onItemClick(task)
        }

        //holder.imageView.setImageResource(task.imageId)
        holder.titleTextView.text = task.title
        holder.taskLevelTextView.text = task.levelname
//        holder.dateTextView.text =
//                //○分前表示にしたい
//                SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPANESE).format(task.time)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TaskViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.taskitem, viewGroup, false)
        return TaskViewHolder(v)
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: LinearLayout = view.dateItem
        val titleTextView: TextView = view.taskTitleTextView
//        val dateTextView: TextView = view.dateTxetView
        val taskLevelTextView: TextView = view.taskLevelTextView
    }

    interface OnItemClickListener {
        fun onItemClick(item: Task)
    }
}
