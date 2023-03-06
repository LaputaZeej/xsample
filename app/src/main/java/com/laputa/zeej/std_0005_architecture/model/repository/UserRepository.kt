package com.laputa.zeej.std_0005_architecture.model.repository

import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.model.data.BookData
import com.laputa.zeej.std_0005_architecture.model.data.UserData

interface UserRepository {

    suspend fun loadUser(userId:String):UserData?
    suspend fun loadBooks(userId:String):List<BookData>
    suspend fun loadAddress(userId: String):AddressData?
}