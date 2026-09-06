package dev.artsign.fxPolls.event

import dev.artsign.fxPolls.gui.PollsGUI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClickListener: Listener {
    @EventHandler
    fun onPlayerClick(e: InventoryClickEvent){
        if (e.inventory.holder is PollsGUI){
            e.isCancelled = true
            e.whoClicked.sendMessage("u clicked")
        }
    }
}