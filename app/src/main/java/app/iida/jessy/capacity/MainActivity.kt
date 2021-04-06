package app.iida.jessy.capacity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.stream.Collectors.groupingBy

class MainActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.init(this)
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //画面遷移Main→List
        taskListButton.setOnClickListener{
            val toTaskListActivity = Intent(this,TaskListActivity::class.java)

            // startActivity(toTaskListActivity)
            // 1. AddActivityからTaskのIDをもらうためにstartActivityForResultを使う
            startActivityForResult(toTaskListActivity , 1) // 第二引数の数字はAddActivityと同じものなら何でもいい
        }
    }

//    この画面に来たら更新されるもの
    override fun onResume() {
        super.onResume()

//    合計を入れる変数
        var sum: Int = 0

        //記入したデーターを全て読み込んでる。１つだけならval book: Book? = read()
        val taskList = readAll()

//    forでlevelの取り出し
        taskList.forEach {
            //sum のところでそれぞれのスコアを足す
            sum += it.level
        }

        //TextViewに表示する
        totalTextView.text = sum.toString()
//        radioButton02.setOnClickListener {
//            val checked = radioButton02.isChecked
//        }

//        //TextViewにコメントを表示する
        if (0 <= sum && sum <= 50) {
            // sumが0以上50以下
            mainTextTitle.text = "いい感じ！"
            //     sumが50以上75以下
        } else if (50 <= sum && sum <= 75) {
            mainTextTitle.text = "そろそろ減らしたいね"
            // sumが75以上90以下
        } else if (75 <= sum && sum <= 90) {
            mainTextTitle.text = "そろそろ追加しない方がいいかも"
            // sumが90以上100以下
        } else if (90 <= sum && sum <= 100) {
            mainTextTitle.text = "やばい！何か削れないかな？"
            // sumが100以上
        } else {
            mainTextTitle.text = "キャパオーバー！ 　 絶対何も入れないで"
        }
    }

    //    realmのデータ取得
    fun readAll(): RealmResults<Task> {
        return realm.where(Task::class.java).findAll()
    }

    override fun onActivityResult(level: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(level, resultCode, data)

        if (level != 1) {
            // AddActivity以外の別のActivityから来た場合
            return
        }

        if (level == Activity.RESULT_OK && data != null) {
            // 2. AdActivityで登録したTaskのIDを受け取る
            val id = data.getStringExtra("TaskID")

            // 3. AddActivityで直近に保存したTaskを読み込み操作を行う
            val task = read(id!!)

            totalTextView.text = task?.level.toString()
        }


    }

    fun read(id: String): Task? {
        return realm.where(Task::class.java).equalTo("id" , id).findFirst()
    }
}
