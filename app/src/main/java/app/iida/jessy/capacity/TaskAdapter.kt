package app.iida.jessy.capacity

import android.content.Context
import android.util.Log
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

// RealmMemoApplication <Task, TaskAdapter.TaskViewHolder>(taskList, autoUpdate)
    androidx.recyclerview.widget.RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun getItemCount(): Int = taskList?.size ?: 0

//    override fun getItemCount(): Int {
//        Log.d("Life Cycle", "getItemCount")
//        return list.size
//    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task: Task = taskList?.get(position) ?: return
        //ListItemの情報
        holder.date.setOnClickListener {
            listener.onItemClick(task)
        }

        holder.titleTextView.text = task.title
        holder.taskLevelTextView.text = task.levelname
        holder.id = task.id
    }


override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
    Log.d("Life Cycle", "onCreateViewHolder")
    val rowView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.taskitem, parent, false)
    return TaskViewHolder(rowView)
}

//    interface ListListener {
//        fun onClickRow(tappedView: View, rowModel: RowModel)
//    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        チェックする定番の方法　中身が入ってないかすぐ確認できる
        var id: String = ""
        val date: LinearLayout = view.dateItem
        val titleTextView: TextView = view.taskTitleTextView
        val taskLevelTextView: TextView = view.taskLevelTextView
    }

    interface OnItemClickListener {
        fun onItemClick(item: Task)
    }
}
