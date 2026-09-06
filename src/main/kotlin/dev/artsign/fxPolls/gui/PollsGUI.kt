package dev.artsign.fxPolls.gui

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class PollsGUI : InventoryHolder{

    fun getHolder(): InventoryHolder{
        return this
    }

    override fun getInventory(): Inventory {
        val inv: Inventory = Bukkit.createInventory(this, 27, Component.text("Polls"))

        /*

        TODO: IMPL. METHODS
        - Get Polls
        - If none, show none item
        - Show navbar down bottom w/ Previous polls, info item, & nav pages
        - Show polls
        - Show filler items

        - Make Configurable 😭

         */

        return inv
    }


}