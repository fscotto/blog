package it.plague.blog.domain

import org.hibernate.validator.constraints.NotBlank

open class Article : AbstractDomainEntity() {

	@NotBlank
	open var author: String = ""

	@NotBlank
	open var title: String = ""

	open var content: Array<Byte> = arrayOf()
}
