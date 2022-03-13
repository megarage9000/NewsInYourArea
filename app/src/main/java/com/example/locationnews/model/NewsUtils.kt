package com.example.locationnews.model
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import retrofit2.Converter
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
    val type: Type = TypeToken.getParameterized(ArrayList::class.java, NewsGet::class.java).type
    val gson = GsonBuilder()
        .registerTypeAdapter(type, GetNewsItemDeserializer())
        .create()
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
        val jsonItems = jsonObject.get("articles").asJsonArray

        if(success == "ok"){
            for (jsonItem in jsonItems) {
                val jsonItemObject = jsonItem.asJsonObject
                val publisher = jsonItemObject.get("source").asJsonObject.get("name").asString
                val title = jsonItemObject.get("title").asString
                val description = jsonItemObject.get("description").asString
                val url = jsonItemObject.get("url").asString
                val publishedAt = jsonItemObject.get("publishedAt").asString
                items.add(NewsGet(publisher, title, description, url, publishedAt))
            }
        }
        return items
    }
}




