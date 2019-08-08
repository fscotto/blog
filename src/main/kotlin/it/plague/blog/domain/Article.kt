package it.plague.blog.domain

import java.time.LocalDateTime

data class Article(var id: Long? = 0,
									 var createdBy: User,
									 var created: LocalDateTime = LocalDateTime.now(),
									 var modifiedBy: User? = null,
									 var modified: LocalDateTime? = null,
									 var author: Author,
									 var title: String,
									 var content: Array<Byte>? = null) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Article

		if (id != other.id) return false
		if (createdBy != other.createdBy) return false
		if (created != other.created) return false
		if (modifiedBy != other.modifiedBy) return false
		if (modified != other.modified) return false
		if (author != other.author) return false
		if (title != other.title) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id?.hashCode() ?: 0
		result = 31 * result + createdBy.hashCode()
		result = 31 * result + created.hashCode()
		result = 31 * result + (modifiedBy?.hashCode() ?: 0)
		result = 31 * result + (modified?.hashCode() ?: 0)
		result = 31 * result + author.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + (content?.contentHashCode() ?: 0)
		return result
	}
}
