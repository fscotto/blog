package it.plague.blog.dao

import java.io.Serializable

interface GenericDao<T, ID : Serializable> {
	fun findAll(): List<T>
	fun findOne(id: ID): T
	fun save(o: T)
	fun merge(o: T)
	fun delete(id: ID)
}