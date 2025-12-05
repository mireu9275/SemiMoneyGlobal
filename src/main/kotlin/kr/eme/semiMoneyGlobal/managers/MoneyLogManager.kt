package kr.eme.semiMoneyGlobal.managers

import org.bukkit.configuration.file.YamlConfiguration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MoneyLogManager {

    private val logList = mutableListOf<LogEntry>()

    data class LogEntry(
        val player: String,
        val type: String,
        val amount: Int,
        val time: String // yyyy-MM-dd HH:mm:ss
    )

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    /**
     * 로그 추가
     */
    fun log(player: String, type: String, amount: Int) {
        val now = LocalDateTime.now().format(formatter)
        logList.add(LogEntry(player, type, amount, now))
    }

    /**
     * 로그 로드
     */
    fun load() {
        val config = FileManager.loadLogConfig()
        logList.clear()

        val section = config.getMapList("logs")
        section.forEach { map ->
            val player = map["player"] as? String ?: return@forEach
            val type = map["type"] as? String ?: return@forEach
            val amount = (map["amount"] as? Int) ?: return@forEach
            val time = map["time"] as? String ?: return@forEach
            logList.add(LogEntry(player, type, amount, time))
        }
    }

    /**
     * 로그 저장
     */
    fun save() {
        val config = YamlConfiguration()
        val logData = logList.map { entry ->
            mapOf(
                "player" to entry.player,
                "type" to entry.type,
                "amount" to entry.amount,
                "time" to entry.time
            )
        }
        config.set("logs", logData)
        FileManager.saveLogConfig(config)
    }

    /**
     * 총 수익 반환
     */
    fun getTotalEarned(): Int {
        return logList.sumOf { it.amount }
    }

    /**
     * 특정 플레이어의 누적 획득 EP 계산 (양수 금액만 합산)
     */
    fun getPlayerTotalEarned(playerName: String): Int {
        return logList
            .filter { it.player == playerName && it.amount > 0 } // 해당 플레이어의 '획득' 기록만 필터링
            .sumOf { it.amount }
    }
}