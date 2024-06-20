package com.example.readbook.repository
data class DataClass(
    var users: List<User>,
    var books: List<Book>,
)

data class User(
    var uid: String = "",
    var isAdmin:Boolean = false,
    var uidBooks: List<Book> = emptyList(),
)

data class Book(
    var uid:String = "",
    var name:String = "",
    var author:String = "",
    var about:String = "",
    var path:String = "",
)