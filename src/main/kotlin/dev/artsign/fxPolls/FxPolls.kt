package dev.artsign.fxPolls

import dev.artsign.fxPolls.command.FxPollsCommand
import dev.artsign.fxPolls.database.Database
import dev.artsign.fxPolls.database.repository.VoteRepository
import dev.artsign.fxPolls.event.InventoryClickListener
import org.bukkit.plugin.java.JavaPlugin

class FxPolls : JavaPlugin() {
    lateinit var database: Database
        private set
    lateinit var voteRepository: VoteRepository
        private set

    override fun onEnable() {
        saveResource("config.yml", true)

        database = Database(this)
        database.connect()
        voteRepository = VoteRepository(database)


        getCommand("fxpolls")!!.setExecutor(FxPollsCommand())

        server.pluginManager.registerEvents(InventoryClickListener(), this)
    }

    override fun onDisable() {
        database.disconnect()
    }
}
