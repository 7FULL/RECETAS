package com.full.recetas.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date


class IsoDateTimeSerializer : JsonDeserializer<Date?>, JsonSerializer<Date?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date {
        val localDateTime = LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_DATE_TIME)
        return Date.from(localDateTime.atZone(ZoneOffset.systemDefault()).toInstant())
    }

    override fun serialize(
        src: Date?,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val localDateTime = LocalDateTime.ofInstant(src?.toInstant() ?: null, ZoneOffset.systemDefault())
        return JsonPrimitive(DateTimeFormatter.ISO_DATE_TIME.format(localDateTime))
    }
}

