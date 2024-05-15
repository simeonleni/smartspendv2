package com.app.smartspend

import com.nikolovlazar.smartspend.models.Category
import com.nikolovlazar.smartspend.models.Expense
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val config = RealmConfiguration.create(schema = setOf(Expense::class, Category::class))
val db: Realm = Realm.open(config)