package kr.eme.prcMoney

import kr.eme.prcMoney.commands.ChequeCommand
import kr.eme.prcMoney.commands.MoneyCommand
import kr.eme.prcMoney.listeners.ChequeListener
import kr.eme.prcMoney.managers.FileManager
import kr.eme.prcMoney.managers.MoneyLogManager
import kr.eme.prcMoney.managers.MoneyManager
import org.bukkit.plugin.java.JavaPlugin

class PRCMoney : JavaPlugin() {
    override fun onEnable() {
        main = this
        FileManager.init(dataFolder)
        MoneyManager.load()
        MoneyLogManager.load()
        registerCommands()
        registerEvents()
        logger.info("PRCMoney Enabled")
    }
    override fun onDisable() {
        MoneyManager.save()
        MoneyLogManager.save()
        logger.info { "PRCMoney Disabled" }
    }

    private fun registerCommands() {
        getCommand("ep")?.setExecutor(MoneyCommand)
        getCommand("token")?.setExecutor(ChequeCommand)
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(ChequeListener, this)
    }
}