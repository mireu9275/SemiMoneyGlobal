package kr.eme.semiMoneyGlobal

import kr.eme.semiMoneyGlobal.commands.MoneyCommand
import kr.eme.semiMoneyGlobal.listeners.ChequeListener
import kr.eme.semiMoneyGlobal.managers.FileManager
import kr.eme.semiMoneyGlobal.managers.MoneyLogManager
import kr.eme.semiMoneyGlobal.managers.MoneyManager
import org.bukkit.plugin.java.JavaPlugin

class SemiMoneyGlobal : JavaPlugin() {
    override fun onEnable() {
        main = this
        FileManager.init(dataFolder)
        MoneyManager.load()
        MoneyLogManager.load()
        registerCommands()
        registerEvents()
        logger.info("SemiMoneyGlobal Enabled")
    }
    override fun onDisable() {
        MoneyManager.save()
        MoneyLogManager.save()
        logger.info { "SemiMoneyGlobal Disabled" }
    }

    private fun registerCommands() {
        getCommand("ep")?.setExecutor(MoneyCommand)
        getCommand("cheque")?.setExecutor(MoneyCommand)
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(ChequeListener, this)
    }
}