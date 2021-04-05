package app.iida.jessy.capacity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.read
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.init(this)
        Realm.getDefaultInstance()
    }


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_detail)

            //受け取った変数を入れる (IDはお役御免
            //intent.getStringExtrは画面遷移の際に使う
            val id = intent.getStringExtra("ID")

            //bookの中身は25行目のid検索の結果出てきた１つのBookが入ってる
            val task = read(id!!)

            //取り出した情報を各入れ物に入れる
            titleTextView.text = task?.title
            levelText.text = task?.levelname
            descriptionTextView.text = task?.description



            // activity_toolbar_sample.xml からToolbar要素を取得
            val toolbar3 = findViewById<Toolbar>(R.id.toolbar3)
            // アクションバーにツールバーをセット
            setSupportActionBar(toolbar3)
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

        fun read(searchId:String): Task?{

            return realm.where(Task::class.java).equalTo("id" , searchId).findFirst()

        }
    }
