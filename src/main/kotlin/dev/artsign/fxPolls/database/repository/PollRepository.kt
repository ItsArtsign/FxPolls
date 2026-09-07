package dev.artsign.fxPolls.database.repository

import dev.artsign.fxPolls.database.Database
import dev.artsign.fxPolls.database.model.Poll
import dev.artsign.fxPolls.database.model.PollOption

class PollRepository(database: Database) {
    private var databaseValue = database

    fun getDatabase(): Database = databaseValue

    fun setDatabase(database: Database) {
        databaseValue = database
    }

    fun createPoll(question: String, options: List<String>): Poll {
        require(question.isNotBlank()) { "Poll question cannot be blank" }
        require(options.size >= 2) { "A poll must have at least two options" }
        require(options.all { it.isNotBlank() }) { "Poll options cannot be blank" }

        databaseValue.pool.connection.use { connection ->
            connection.autoCommit = false
            try {
                val pollId = connection.prepareStatement(
                    "INSERT INTO polls (question) VALUES (?)",
                    arrayOf("id")
                ).use { statement ->
                    statement.setString(1, question)
                    statement.executeUpdate()
                    statement.generatedKeys.use { keys ->
                        check(keys.next()) { "Failed to create poll" }
                        keys.getInt(1)
                    }
                }

                connection.prepareStatement(
                    "INSERT INTO poll_options (poll_id, label) VALUES (?, ?)"
                ).use { statement ->
                    options.forEach { option ->
                        statement.setInt(1, pollId)
                        statement.setString(2, option)
                        statement.addBatch()
                    }
                    statement.executeBatch()
                }
                connection.commit()
                return Poll(pollId, question, true)
            } catch (error: Exception) {
                connection.rollback()
                throw error
            } finally {
                connection.autoCommit = true
            }
        }
    }

    fun getPoll(pollId: Int): Poll? {
        databaseValue.pool.connection.use { connection ->
            connection.prepareStatement(
                "SELECT id, question, active FROM polls WHERE id = ?"
            ).use { statement ->
                statement.setInt(1, pollId)
                statement.executeQuery().use { result ->
                    return if (result.next()) {
                        Poll(
                            result.getInt("id"),
                            result.getString("question"),
                            result.getInt("active") == 1
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun getOptions(pollId: Int): List<PollOption> {
        val options = mutableListOf<PollOption>()
        databaseValue.pool.connection.use { connection ->
            connection.prepareStatement(
                "SELECT id, poll_id, label FROM poll_options WHERE poll_id = ? ORDER BY id"
            ).use { statement ->
                statement.setInt(1, pollId)
                statement.executeQuery().use { result ->
                    while (result.next()) {
                        options += PollOption(
                            result.getInt("id"),
                            result.getInt("poll_id"),
                            result.getString("label")
                        )
                    }
                }
            }
        }
        return options
    }

    fun setActive(pollId: Int, active: Boolean): Boolean {
        databaseValue.pool.connection.use { connection ->
            connection.prepareStatement(
                "UPDATE polls SET active = ? WHERE id = ?"
            ).use { statement ->
                statement.setInt(1, if (active) 1 else 0)
                statement.setInt(2, pollId)
                return statement.executeUpdate() == 1
            }
        }
    }

    fun deletePoll(pollId: Int): Boolean {
        databaseValue.pool.connection.use { connection ->
            connection.prepareStatement("DELETE FROM polls WHERE id = ?").use { statement ->
                statement.setInt(1, pollId)
                return statement.executeUpdate() == 1
            }
        }
    }
}
