package app.iida.jessy.capacity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.RealmResults
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

    //この画面に来たら更新されるもの
    /*
override fun onResume() {

    super.onResume()
    //受け取った変数を入れる (IDはお役御免
    val id = ("ID")

    //taskの中身はid検索の結果出てきた全てのデータが入ってる
    val task = readAll(id)
    //各タスクのレベルを足し合わせて合計値を計算したい

    //取り出した情報を入れ物に入れる

}
    */

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
            val task = readAll(id!!)

            totalTextView.text = task?.level.toString()
        }


    }

    fun readAll(id: String): Task? {
        return realm.where(Task::class.java).equalTo("id" , id).findFirst()
    }
}
