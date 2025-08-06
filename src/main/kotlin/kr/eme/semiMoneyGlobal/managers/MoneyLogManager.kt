package kr.eme.semiMoneyGlobal.managers

object MoneyLogManager {
    private val logs = mutableListOf<String>()
    fun log(message: String) {
        logs.add("[${now()}] $message")
    }
    fun loadFromFile(lines: List<String>) {
        logs.clear()
        logs.addAll(lines)
    }
    fun saveToFile(lines: List<String>) {
        FileManager.saveLogLines(lines)
    }
    fun getLogs(): List<String> = logs.toList()
    private fun now(): String = java.time.LocalDateTime.now().toString()
}