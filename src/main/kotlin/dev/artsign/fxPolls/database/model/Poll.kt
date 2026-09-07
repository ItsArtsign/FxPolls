package dev.artsign.fxPolls.database.model

data class Poll(
    private var id: Int,
    private var question: String,
    private var active: Boolean
) {
    fun getId(): Int = id

    fun setId(id: Int) {
        this.id = id
    }

    fun getQuestion(): String = question

    fun setQuestion(question: String) {
        this.question = question
    }

    fun isActive(): Boolean = active

    fun setActive(active: Boolean) {
        this.active = active
    }
}
