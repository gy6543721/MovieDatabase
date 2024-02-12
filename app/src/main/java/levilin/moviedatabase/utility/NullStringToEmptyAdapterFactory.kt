package levilin.moviedatabase.utility

import com.google.gson.*
import com.google.gson.reflect.TypeToken

class NullStringToEmptyAdapterFactory<T> : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType
        return if (rawType != String::class.java) {
            null
        } else {
            @Suppress("UNCHECKED_CAST")
            StringAdapter() as TypeAdapter<T>
        }
    }
}