package kr.eme.semiMoneyGlobal

import kr.eme.semiMoneyGlobal.commands.MoneyCommand
import kr.eme.semiMoneyGlobal.listeners.ChequeListener
import kr.eme.semiMoneyGlobal.managers.FileManager
import org.bukkit.plugin.java.JavaPlugin

class SemiMoneyGlobal : JavaPlugin() {
    override fun onEnable() {
        main = this
        FileManager.init(dataFolder)
        logger.info("SemiMoneyGlobal Enabled")
    }
    override fun onDisable() {
        logger.info { "SemiMoneyGlobal Disabled" }
    }

    private fun registerCommands() {
        getCommand("money")?.setExecutor(MoneyCommand)
        getCommand("cheque")?.setExecutor(MoneyCommand)
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(ChequeListener, this)
    }
}