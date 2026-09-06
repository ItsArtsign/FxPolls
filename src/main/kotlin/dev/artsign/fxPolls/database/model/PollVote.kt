package dev.artsign.fxPolls.database.model

import java.util.UUID

data class PollVote(
    val pollId: Int,
    val optionId: Int,
    val playerUuid: UUID
)