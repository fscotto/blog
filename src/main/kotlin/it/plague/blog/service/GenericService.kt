package it.plague.blog.service

import java.io.Serializable

interface GenericService<T, ID : Serializable> {
	fun getAll(): List<T>
	fun getOne(id: ID): T
	fun save(o: T)
	fun merge(o: T)
	fun delete(id: ID)
}