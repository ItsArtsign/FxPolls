package dev.artsign.fxPolls.database.model

import java.util.UUID

data class PollVote(
    private var pollIdValue: Int,
    private var optionIdValue: Int,
    private var playerUuidValue: UUID
) {
    fun getPollId(): Int = pollIdValue

    fun setPollId(pollId: Int) {
        pollIdValue = pollId
    }

    fun getOptionId(): Int = optionIdValue

    fun setOptionId(optionId: Int) {
        optionIdValue = optionId
    }

    fun getPlayerUuid(): UUID = playerUuidValue

    fun setPlayerUuid(playerUuid: UUID) {
        playerUuidValue = playerUuid
    }
}