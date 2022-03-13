package com.example.locationnews.model
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory;
import java.lang.reflect.Type

object NewsUtils {
    private val retrofit by lazy {
        getRetrofit()
    }
    val api: NewsApi by lazy {
        retrofit.create(NewsApi::class.java)
    }
}

fun getRetrofit() : Retrofit {
    // Creating a gson deserializer
    val type: Type = TypeToken.getParameterized(ArrayList::class.java, NewsGet::class.java).type
    val gson = GsonBuilder()
        .registerTypeAdapter(type, GetNewsItemDeserializer())
        .create()

    // Return build
    return Retrofit.Builder()
        .baseUrl(NEWS_API)
        .addConverterFactory(
            GsonConverterFactory.create(gson)
        )
        .build()
}

// Using a custom deserializer: https://www.woolha.com/tutorials/retrofit-2-define-custom-gson-converter-factory
class GetNewsItemDeserializer : JsonDeserializer<ArrayList<NewsGet>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ArrayList<NewsGet> {

        val items = ArrayList<NewsGet>()
        val jsonObject = json!!.asJsonObject
        val success = jsonObject.get("status").asString

        if(success == "ok"){
            val jsonItems = jsonObject.get("articles").asJsonArray

            for (jsonItem in jsonItems) {
                val jsonItemObject = jsonItem.asJsonObject
                val publisher = getJsonNullable(getJsonNullable(jsonItemObject, "source")?.asJsonObject, "name")?.asString
                val title = getJsonNullable(jsonItemObject, "title")?.asString
                val description = getJsonNullable(jsonItemObject, "description")?.asString
                val url = getJsonNullable(jsonItemObject, "url")?.asString
                val publishedAt = getJsonNullable(jsonItemObject, "publishedAt")?.asString
                items.add(NewsGet(
                    (publisher ?: "NaN"),
                    (title ?: "NaN"),
                    (description ?: "NaN"),
                    (url ?: "NaN"),
                    (publishedAt ?: "NaN")))
            }
        }
        return items
    }
}

// Handle null json values
fun getJsonNullable(jsonObject: JsonObject?, tag: String) : JsonElement? {
    val res = jsonObject?.get(tag)
    return if(res == JsonNull.INSTANCE || res == null) null else res
}



