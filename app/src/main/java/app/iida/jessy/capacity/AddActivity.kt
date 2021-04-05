package app.iida.jessy.capacity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_add.*


class AddActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()

    var level: Int = 0
    var levelname:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        //        if (memo != null){
//            titleEditText.setText(memo.title)
//            nameEditText.setText(memo.name)
//            moneyEditText.setText(memo.money)
//            descriptionEditText.setText(memo.description)
//        }

        //追加ボタンを押した時に入力されたテキストを取得しsave()メソッドに値を渡す
        addButton.setOnClickListener {
            val title: String = titleEditText.text.toString()

            val descriptor: String = descriptionEditText.text.toString()
            save(title,level,levelname,descriptor)
        }

        radioButton01.setOnClickListener{
            val checked = radioButton01.isChecked

            if (checked) {
                // 1つ目のRadioButtonが選択された時の処理
                radioButton01.isChecked = true
                //他の3つをOff(=false)にする
                radioButton02.isChecked = false
                radioButton03.isChecked = false
                radioButton04.isChecked = false
                //01で5加算する
                level += 5
                //levelnameに簡単/たのしいを入れる
                levelname = "簡単/たのしい"

            }
        }

        radioButton02.setOnClickListener{
            val checked = radioButton02.isChecked

            if (checked) {
                // 1つ目のRadioButtonが選択された時の処理
                radioButton02.isChecked = true
                //他の3つをOff(=false)にする
                radioButton01.isChecked = false
                radioButton03.isChecked = false
                radioButton04.isChecked = false
                //02で10加算する
                level += 10
                //levelnameに普通を入れる
                levelname = "普通"
            }
        }

        radioButton03.setOnClickListener{
            val checked = radioButton03.isChecked
            if (checked) {
                // 1つ目のRadioButtonが選択された時の処理
                radioButton03.isChecked = true
                //他の3つをOff(=false)にする
                radioButton01.isChecked = false
                radioButton02.isChecked = false
                radioButton04.isChecked = false
                //03で15加算する
                level += 15
                //levelnameに憂鬱/しんどい
                levelname = "憂鬱/しんどい"
            }
        }

        radioButton04.setOnClickListener{
            val checked = radioButton04.isChecked
            if (checked) {
                // 1つ目のRadioButtonが選択された時の処理
                radioButton04.isChecked = true
                //他の3つをOff(=false)にする
                radioButton01.isChecked = false
                radioButton02.isChecked = false
                radioButton03.isChecked = false
                //04で25加算する
                level += 25
                //levelnameに苦痛/やりたくない
                levelname = "苦痛/やりたくない"
            }
        }

//        fun calculation(op : String?) :Int {
//            return  (op == "+") {
//                0 + level.text.toString().toInt()
//            }
//        }

        //戻るのボタン設定

        // activity_toolbar_sample.xml からToolbar要素を取得
        val toolbar2 = findViewById<Toolbar>(R.id.toolBar2)
        // アクションバーにツールバーをセット
        setSupportActionBar(toolbar2)
        // ツールバーに戻るボタンを設置
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    // ツールバーのアイテムを押した時の処理を記述（今回は戻るボタンのみのため、戻るボタンを押した時の処理しか記述していない）
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // android.R.id.home に戻るボタンを押した時のidが取得できる
        if (item.itemId == android.R.id.home) {
            // 今回はActivityを終了させている
            finish()
        }
        return super.onOptionsItemSelected(item)

    }

    //realm関係
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    //受け取った情報の保存
    fun save(title: String, level: Int, levelname: String, description: String) {

        realm.executeTransaction {
            //メモの新規作成
            val newTask: Task = it.createObject(Task::class.java, java.util.UUID.randomUUID().toString())
            newTask.title = title
            newTask.level = level
            newTask.levelname = levelname
            newTask.description = description


        // 1. 宣言的に AddActivityを終了させる
        val result = Intent()
        // 2. newTaskのIDを渡す(
        result.putExtra("TaskID", newTask.id)
        setResult(Activity.RESULT_OK, result)
        finish()

        // 色々あってAddActivityではスナックバーを呼び出せないのでMainActivityでスナックバーを表示するのがよいかも
        // Snackbar.make(container, "保存しました!!", Snackbar.LENGTH_SHORT).show()
    }
    }
}
