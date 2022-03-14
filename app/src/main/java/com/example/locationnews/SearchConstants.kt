package com.example.locationnews

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap


class SearchConstants {
    companion object {
        val sortByOptions = hashMapOf(
            "Keyword Relevancy" to "relevancy",
            "Popularity" to "popularity",
            "Recency" to "publishedAt"
        )

        val categoryOptions = hashMapOf(
            "Business" to "business",
            "Entertainment" to "entertainment",
            "General" to "general",
            "Science" to "science",
            "Sports" to "sports",
            "Technology" to "technology"
        )

        var countryCodes: SortedMap<String, String> = sortedMapOf()
        var languageCodes: SortedMap<String, String> = sortedMapOf()

        val codesType: Type = object : TypeToken<HashMap<String, String>>() {}.type

        fun getCountryCodeGson(): Gson {
            return GsonBuilder().registerTypeAdapter(codesType, CountryCodeDeserializer()).create()
        }

        fun getLanguageCodeGson(): Gson {
            val type = object : TypeToken<HashMap<String, String>>() {}.type
            return GsonBuilder().registerTypeAdapter(codesType, LanguageCodeDeserializer()).create()
        }
    }
}

class CountryCodeDeserializer : JsonDeserializer<HashMap<String, String>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HashMap<String, String> {
        val result = HashMap<String, String>()
        if(json != null) {
            val jsonItems = json.asJsonArray

            for(jsonItem in jsonItems) {
                result[jsonItem.
                    asJsonObject.
                    get("name").
                    asString] = jsonItem.
                    asJsonObject.
                    get("alpha-2").
                    asString
            }
        }
        return result
    }
}

class LanguageCodeDeserializer : JsonDeserializer<HashMap<String, String>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HashMap<String, String> {
        val result = HashMap<String, String>()
        if(json != null) {
            val jsonItems = json.asJsonArray

            for(jsonItem in jsonItems) {
                result[jsonItem.
                asJsonObject.
                get("name").
                asString] = jsonItem.
                asJsonObject.
                get("code").
                asString
            }
        }
        return result
    }
}