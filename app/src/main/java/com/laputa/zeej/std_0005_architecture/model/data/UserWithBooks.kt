package com.laputa.zeej.std_0005_architecture.model.data

data class UserWithBooks(val user: UserData, val books: List<BookData>){
    override fun toString(): String {
        return user.name+"[${user.age}]\n"+
                books.joinToString("\n") {
                    "   "+it.name+"(${it.author})"
                }
    }
}