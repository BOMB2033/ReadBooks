package com.example.readbook.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RepositoryInMemoryImpl {

    private var databaseUsersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var databaseBooksReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("books")
    private val uid:String = Firebase.auth.currentUser!!.uid

    var dataClass = DataClass(
        emptyList(),
        emptyList(),
    )
    private val data = MutableLiveData(dataClass)

    fun getAll() = data

    fun loadUser() {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataClass.users = emptyList()
                dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }.forEach{
                    dataClass.users = dataClass.users.plus(it)
                }
                data.value = dataClass
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseUsersReference.addValueEventListener(listener)
    }

    fun addUser() {
        dataClass.users = dataClass.users.plus(User(uid = uid))
        data.value = dataClass
        databaseUsersReference.child(uid).removeValue()
        databaseUsersReference.child(uid).setValue(User(uid = uid))
    }
    fun addBook(book: Book) {
        val newUid = databaseBooksReference.push().key!!
        dataClass.books = dataClass.books.plus(book.copy(uid = newUid ))
        data.value = dataClass
        databaseBooksReference.child(newUid).setValue(book.copy(uid = newUid ))
    }
    fun getCurrentUser():User{
        dataClass.users.forEach {
            if (it.uid == uid) return it
        }
        return User()
    }

    fun getBook(uidBook: String): Book {
        dataClass.books.forEach {
            if (it.uid == uidBook) return it
        }
        return Book()
    }

    fun loadBooks() {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataClass.books = emptyList()
                dataSnapshot.children.mapNotNull { it.getValue(Book::class.java) }.forEach{
                    dataClass.books = dataClass.books.plus(it)
                }
                data.value = dataClass
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseBooksReference.addValueEventListener(listener)
        /*databaseBooksReference.setValue(
            listOf(
                Book("1", "Воентур 4я","А_З_К","","https://readli.net/chitat-online/?b=1301276&pg="),
                Book("2", "Сын Петра. Том 1. Бесенок","Ланцов Михаил Алексеевич","","https://readli.net/chitat-online/?b=1223737&pg="),
                Book("3", "Хранители времени. Реконструкция истории Вселенной атом за атомом","Хелфанд Дэвид","","https://readli.net/chitat-online/?b=1301269&pg="),
                Book("4", "Под знаком Меркурия","Босин Владимир Георгиевич \"VladimirB\"","","https://readli.net/chitat-online/?b=1161126&pg="),
                Book("5", "Последний герой Исекай db.1","Барка Юрий","","https://readli.net/chitat-online/?b=1301243&pg="),
                Book("6", "Ассистент(ка) Его Темнейшества. Трилогия - Ольга Коротаева","Коротаева Ольга","","https://readli.net/chitat-online/?b=1301242&pg="),
                Book("7", "Дорога в неизвестность","Ерохин Кирилл","","https://readli.net/chitat-online/?b=1301241&pg="),
                Book("8", "Злодейский путь!.. Том 11","Моргот Эл","","https://readli.net/chitat-online/?b=1301238&pg="),
                Book("9", "Злодейский путь!.. Том 10","Моргот Эл","","https://readli.net/chitat-online/?b=1301240&pg="),
            )
        )*/
    }


}


class DataViewModel : ViewModel() {
    private val repository = RepositoryInMemoryImpl()
    val data = repository.getAll()
    val uid:String = Firebase.auth.currentUser!!.uid
    fun getUser() = repository.getCurrentUser()
    fun addUser() = repository.addUser()
    fun loadUsers() = repository.loadUser()
    fun loadBooks() = repository.loadBooks()
    fun addBook(book: Book) = repository.addBook(book)
    fun getBook(uidBook: String) = repository.getBook(uidBook)
}