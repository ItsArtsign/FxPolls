package dev.artsign.fxPolls.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.plugin.java.JavaPlugin

class Database(private val plugin: JavaPlugin) {
    lateinit var pool: HikariDataSource
        private set

    fun connect() {
        val config = HikariConfig().apply{
            jdbcUrl = "jdbc:sqlite:${plugin.dataFolder}/data.db"
            maximumPoolSize = 4
        }

        pool = HikariDataSource(config)

        Schema.apply(pool)
    }

    fun disconnect() {
        if (::pool.isInitialized) pool.close()
    }
}