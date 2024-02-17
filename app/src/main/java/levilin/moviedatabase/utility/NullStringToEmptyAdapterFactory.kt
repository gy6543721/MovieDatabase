package levilin.moviedatabase.utility

import com.google.gson.*
import com.google.gson.reflect.TypeToken

@Suppress("UNCHECKED_CAST")
class NullStringToEmptyAdapterFactory<T> : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.rawType
        return if (rawType != String::class.java) {
            null
        } else {
            StringAdapter() as TypeAdapter<T>
        }
    }
}