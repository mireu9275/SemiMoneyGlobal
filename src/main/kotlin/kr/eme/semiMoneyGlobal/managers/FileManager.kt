package kr.eme.semiMoneyGlobal.managers

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object FileManager {
    private lateinit var dataFolder: File
    fun init(folder: File) {
        dataFolder = folder
        if (!dataFolder.exists()) dataFolder.mkdirs()
    }
    fun getMoneyFile(): File = File(dataFolder, "money.yml")
    fun loadMoneyConfig(): YamlConfiguration = YamlConfiguration.loadConfiguration(getMoneyFile())
    fun saveMoneyConfig(config: YamlConfiguration) {
        config.save(getMoneyFile())
    }
    fun getLogFile(): File = File(dataFolder, "money_log.txt")
    fun loadLogLines(): List<String> = if (getLogFile().exists()) getLogFile().readLines() else emptyList()
    fun saveLogLines(lines: List<String>) {
        getLogFile().printWriter().use { writer ->
            lines.forEach(writer::println)
        }
    }
}