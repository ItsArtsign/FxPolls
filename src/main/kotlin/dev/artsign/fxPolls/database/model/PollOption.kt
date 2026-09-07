package dev.artsign.fxPolls.database.model

data class PollOption(
    private var id: Int,
    private var pollId: Int,
    private var label: String
) {
    fun getId(): Int = id

    fun setId(id: Int) {
        this.id = id
    }

    fun getPollId(): Int = pollId

    fun setPollId(pollId: Int) {
        this.pollId = pollId
    }

    fun getLabel(): String = label

    fun setLabel(label: String) {
        this.label = label
    }
}
