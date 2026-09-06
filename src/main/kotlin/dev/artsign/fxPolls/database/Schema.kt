package dev.artsign.fxPolls.database

import javax.sql.DataSource

object Schema {
    fun apply(dataSource: DataSource){
        dataSource.connection.use { conn ->
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS polls (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                question TEXT NOT NULL,
                active INTEGER NOT NULL DEFAULT 1
                )
            """.trimIndent())

            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS poll_votes (
                poll_id INTEGER NOT NULL,
                option_id INTEGER NOT NULL,
                player_uuid TEXT NOT NULL,
                PRIMARY KEY (poll_id, player_uuid)
                )
            """.trimIndent())

        }
    }
}