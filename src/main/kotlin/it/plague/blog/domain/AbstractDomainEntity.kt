package it.plague.blog.domain

import java.time.LocalDateTime

abstract class AbstractDomainEntity {

	var id: Long? = null

	var version: Int = 0

	var createdBy: String = ""

	var created: LocalDateTime = LocalDateTime.now()

	var modifiedBy: String? = ""

	var modified: LocalDateTime? = null

	protected fun onUpdate() {
		// FIXME setting modifiedBy property too
		this.modified = LocalDateTime.now()
	}
}
