package dev.artsign.fxPolls.database.repository

import dev.artsign.fxPolls.database.Database
import dev.artsign.fxPolls.database.model.PollVote
import java.util.UUID

class VoteRepository(database: Database) {
    private var databaseValue = database

    fun getDatabase(): Database = databaseValue

    fun setDatabase(database: Database) {
        databaseValue = database
    }

    fun castVote(pollId: Int, optionId: Int, playerUuid: UUID): PollVote {
        databaseValue.pool.connection.use { conn ->
            conn.prepareStatement(
                "SELECT 1 FROM polls p JOIN poll_options o ON o.poll_id = p.id " +
                        "WHERE p.id = ? AND o.id = ? AND p.active = 1"
            ).use { validation ->
                validation.setInt(1, pollId)
                validation.setInt(2, optionId)
                validation.executeQuery().use { result ->
                    check(result.next()) { "Poll is inactive or the option does not belong to it" }
                }
            }
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
        return PollVote(pollId, optionId, playerUuid)
    }

    fun getStandings(pollId: Int): Map<Int, Int> {
        val standings = mutableMapOf<Int, Int>()
        databaseValue.pool.connection.use { conn ->
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