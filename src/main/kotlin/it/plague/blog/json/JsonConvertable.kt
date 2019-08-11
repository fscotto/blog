package it.plague.blog.json

import io.vertx.core.json.JsonObject

interface JsonConvertable<T> {
	fun fromJson(jsonObject: JsonObject): T
	fun toJson(): JsonObject
}