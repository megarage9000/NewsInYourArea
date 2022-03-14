package com.example.locationnews

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type



class SearchConstants {
    companion object {
        val sortByOptions = arrayOf(
            "relevancy",
            "popularity",
            "publishedAt"
        )

        val categoryOptions = arrayOf(
            "business",
            "entertainment",
            "general",
            "health",
            "science",
            "sports",
            "technology"
        )

        var countryCodes: HashMap<String, String> = hashMapOf()
        var languageCodes: HashMap<String, String> = hashMapOf()

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