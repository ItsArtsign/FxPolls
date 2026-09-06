package dev.artsign.fxPolls.command

import dev.artsign.fxPolls.gui.PollsGUI
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FxPollsCommand : CommandExecutor {

    private final val MM = MiniMessage.miniMessage()

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if (sender !is Player){
            sender.sendMessage(MM.deserialize("<red>Only players can execute this command!"))
            return true
        }

        sender.openInventory(PollsGUI().inventory)

        return true
    }
}