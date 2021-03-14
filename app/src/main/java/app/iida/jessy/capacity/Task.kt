package app.iida.jessy.capacity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Task (
    //ID
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var title: String = "",
    open var level:  Int = 0,
    open var levelname: String = "",
    open var description: String = "",
    open var time: Date = Date(System.currentTimeMillis())
) : RealmObject()