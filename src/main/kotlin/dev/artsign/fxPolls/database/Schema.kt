package dev.artsign.fxPolls.database

import javax.sql.DataSource

object Schema {
    fun apply(dataSource: DataSource){
        dataSource.connection.use { conn ->
            conn.createStatement().execute("PRAGMA foreign_keys = ON")
            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS polls (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                question TEXT NOT NULL,
                active INTEGER NOT NULL DEFAULT 1
                )
            """.trimIndent())

            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS poll_options (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                poll_id INTEGER NOT NULL,
                label TEXT NOT NULL,
                FOREIGN KEY (poll_id) REFERENCES polls(id) ON DELETE CASCADE
                )
            """.trimIndent())

            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS poll_votes (
                poll_id INTEGER NOT NULL,
                option_id INTEGER NOT NULL,
                player_uuid TEXT NOT NULL,
                PRIMARY KEY (poll_id, player_uuid),
                FOREIGN KEY (poll_id) REFERENCES polls(id) ON DELETE CASCADE,
                FOREIGN KEY (option_id) REFERENCES poll_options(id) ON DELETE CASCADE
                )
            """.trimIndent())

            conn.createStatement().execute(
                "CREATE INDEX IF NOT EXISTS idx_poll_options_poll_id ON poll_options(poll_id)"
            )
            conn.createStatement().execute(
                "CREATE INDEX IF NOT EXISTS idx_poll_votes_poll_id ON poll_votes(poll_id)"
            )
        }
    }
}