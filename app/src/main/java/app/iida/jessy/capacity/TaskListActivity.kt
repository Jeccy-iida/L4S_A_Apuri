package app.iida.jessy.capacity

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_task_list.*


class TaskListActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.init(this)
        Realm.getDefaultInstance()
    }

    //記入したデーターを全て読み込んでる。１つだけならval book: Book? = read()
    val taskList = readAll()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        //toolbarをTopbarに設定してる
        setSupportActionBar(toolbar)




        //詳細画面へ画面遷移　データーを渡す
        val adapter = TaskAdapter(this, taskList, object : TaskAdapter.OnItemClickListener {
            override fun onItemClick(item: Task) {
                // クリック時の処理
                //Intentのインスタンスを作成
                //intentは画面遷移の時に使う。
                val intent = Intent(applicationContext, DetailActivity::class.java)

                //intent変数をつなげる(第一引数はキー，第二引数は渡したい変数)
                intent.putExtra("ID", item.id)

                //画面遷移を開始
                startActivity(intent)
            }
        }, true)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val swipeToDismissTouchHelper = getSwipeToDismissTouchHelper(adapter)
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView)


        //画面遷移でAddに飛ぶ
        plusButton.setOnClickListener{
            val toAddActivityIntent = Intent(this, AddActivity::class.java)
            startActivity(toAddActivityIntent)
        }

        //記入したデーターを全て読み込んでる。１つだけならval book: Book? = read()
        val task = readAll()

        // activity_toolbar_sample.xml からToolbar要素を取得
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // アクションバーにツールバーをセット
        setSupportActionBar(toolbar)
        // ツールバーに戻るボタンを設置
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    //カードのスワイプアクションの定義
    private fun getSwipeToDismissTouchHelper(adapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>) =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                //スワイプ時に実行
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val vh = viewHolder as TaskAdapter.TaskViewHolder

                    // プライマリーキーをもとに該当のデータを取得
//                    val id = intent.getStringExtra("ID")

                    // トランザクションして削除
                    realm.executeTransaction {
                        it.where(Task::class.java)
                                .equalTo("id", vh.id)
                                .findAll()
                                .deleteAllFromRealm()
                    }


                    //リストからスワイプしたカードを削除
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                }

                //スワイプした時の背景を設定
                override fun onChildDraw(
                        c: Canvas,
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        dX: Float,
                        dY: Float,
                        actionState: Int,
                        isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                    )
                    val itemView = viewHolder.itemView
                    val background = ColorDrawable()
                    background.color = Color.parseColor("#f44336")
                    if (dX < 0)
                        background.setBounds(
                                itemView.right + dX.toInt(),
                                itemView.top,
                                itemView.right,
                                itemView.bottom
                        )
                    else
                        background.setBounds(
                                itemView.left,
                                itemView.top,
                                itemView.left + dX.toInt(),
                                itemView.bottom
                        )

                    background.draw(c)
                }
            })

    // ツールバーのアイテムを押した時の処理を記述（今回は戻るボタンのみのため、戻るボタンを押した時の処理しか記述していない）
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // android.R.id.home に戻るボタンを押した時のidが取得できる
        if (item.itemId == android.R.id.home) {
            // 今回はActivityを終了させている
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun read(): Task? {
        return realm.where(Task::class.java).findFirst()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun readAll(): RealmResults<Task> {
        return realm.where(Task::class.java).findAll()
    }
}