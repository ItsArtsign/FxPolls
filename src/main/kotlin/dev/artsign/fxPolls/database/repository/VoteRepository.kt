package dev.artsign.fxPolls.database.repository

import dev.artsign.fxPolls.database.Database
import java.util.UUID

class VoteRepository(private val database: Database) {

    fun castVote(pollId: Int, optionId: Int, playerUuid: UUID){
        database.pool.connection.use { conn ->
            conn.prepareStatement("""
                INSERT INTO poll_votes (poll_id, option_id, player_uuid)
                VALUES (?, ?, ?)
                ON CONFLICT(poll_id, player_uuid) DO UPDATE SET option_id = excluded.option_id
            """.trimIndent()).use { stmt ->
                stmt.setInt(1, pollId)
                stmt.setInt(2, optionId)
                stmt.setString(3, playerUuid.toString())
                stmt.executeUpdate()
            }
        }
    }

    fun getStandings(pollId: Int): Map<Int, Int> {
        val standings = mutableMapOf<Int, Int>()
        database.pool.connection.use { conn ->
            conn.prepareStatement(
                "SELECT option_id, COUNT(*) c FROM poll_votes WHERE poll_id = ? GROUP BY option_id"
            ).use { stmt ->
                stmt.setInt(1, pollId)
                stmt.executeQuery().use { rs ->
                    while (rs.next()) standings[rs.getInt("option_id")] = rs.getInt("c")
                }
            }
        }
        return standings
    }

}