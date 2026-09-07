package dev.artsign.fxPolls.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.plugin.java.JavaPlugin

class Database(plugin: JavaPlugin) {
    private var pluginValue = plugin
    private lateinit var poolValue: HikariDataSource

    fun getPlugin(): JavaPlugin = pluginValue

    fun setPlugin(plugin: JavaPlugin) {
        pluginValue = plugin
    }

    var pool: HikariDataSource
        get() {
            check(::poolValue.isInitialized) { "Database is not connected" }
            return poolValue
        }
        set(value) {
            if (::poolValue.isInitialized && poolValue !== value) {
                poolValue.close()
            }
            poolValue = value
        }

    fun getPool(): HikariDataSource {
        return pool
    }

    fun setPool(pool: HikariDataSource) {
        this.pool = pool
    }

    fun connect() {
        val config = HikariConfig().apply{
            jdbcUrl = "jdbc:sqlite:${pluginValue.dataFolder}/data.db"
            maximumPoolSize = 4
        }

        setPool(HikariDataSource(config))

        Schema.apply(getPool())
    }

    fun disconnect() {
        if (::poolValue.isInitialized) poolValue.close()
    }
}