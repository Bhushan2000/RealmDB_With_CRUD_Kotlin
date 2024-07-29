package com.example.realmdb_with_crud_kotlin.data.Repository.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Todo : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var id: Int = 0
    var todo: String = ""
    var completed: Boolean = false
    var userId: Int = 0
}