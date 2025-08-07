package kr.eme.semiMoneyGlobal.managers

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object FileManager {
    private lateinit var dataFolder: File

    fun init(folder: File) {
        dataFolder = folder
        if (!dataFolder.exists()) dataFolder.mkdirs()
    }

    // money.yml 파일
    fun getMoneyFile(): File = File(dataFolder, "money.yml")
    fun loadMoneyConfig(): YamlConfiguration = YamlConfiguration.loadConfiguration(getMoneyFile())
    fun saveMoneyConfig(config: YamlConfiguration) = config.save(getMoneyFile())

    // money_log.yml 파일
    fun getLogFile(): File = File(dataFolder, "money_log.yml")
    fun loadLogConfig(): YamlConfiguration = YamlConfiguration.loadConfiguration(getLogFile())
    fun saveLogConfig(config: YamlConfiguration) = config.save(getLogFile())
}