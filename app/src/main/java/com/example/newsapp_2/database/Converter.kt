package com.example.newsapp_2.database

import androidx.room.TypeConverter
import com.example.newsapp_2.models.Source

class Converter {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }

}