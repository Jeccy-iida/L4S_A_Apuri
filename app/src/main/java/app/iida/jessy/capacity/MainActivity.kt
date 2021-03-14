package app.iida.jessy.capacity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.init(this)
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //受け取った変数を入れる (IDはお役御免
        val id = intent.getStringExtra("ID")

        //bookの中身は25行目のid検索の結果出てきた１つのBookが入ってる
        val task = read(id!!)

        //取り出した情報を各入れ物に入れる
        totalTextView.text = task?.level.toString()


        //画面遷移Main→List
        taskListButton.setOnClickListener{
            val toTaskListActivity = Intent(this,TaskListActivity::class.java)
            startActivity(toTaskListActivity)

        }
    }

    fun read(searchId:String): Task?{

        return realm.where(Task::class.java).equalTo("id" , searchId).findFirst()

    }
}