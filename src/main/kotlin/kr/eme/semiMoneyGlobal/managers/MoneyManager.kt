package kr.eme.semiMoneyGlobal.managers

import kr.eme.semiMission.api.events.MissionEvent
import kr.eme.semiMission.enums.MissionVersion
import kr.eme.semiMission.objects.const.MissionTargets
import kr.eme.semiMission.objects.const.MissionTypes
import org.bukkit.Bukkit

object MoneyManager {
    private var money: Int = 0

    private val missionMilestones = listOf(
        300_000 to MissionVersion.V1,
        500_000 to MissionVersion.V1,
        2_000_000 to MissionVersion.V2,
        5_000_000 to MissionVersion.V2,
        10_000_000 to MissionVersion.V2
    )

    /**
     * 현재 보유 금액 반환
     */
    fun getMoney(): Int = money

    /**
     * 금액 추가
     */
    fun addMoney(amount: Int) {
        money += amount
    }

    /**
     * 금액 차감 (부족할 경우 false 반환)
     */
    fun subtractMoney(amount: Int): Boolean {
        if (money < amount) return false
        money -= amount
        return true
    }

    /**
     * money.yml 로부터 금액 로드
     */
    fun load() {
        val file = FileManager.getMoneyFile()
        if (!file.exists()) {
            file.writeText("money: 0")
        }
        val config = FileManager.loadMoneyConfig()
        money = config.getInt("money", 0)
    }

    /**
     * money.yml 에 금액 저장
     */
    fun save() {
        val config = FileManager.loadMoneyConfig()
        config.set("money", money)
        FileManager.saveMoneyConfig(config)
    }

    /**
     * 다른 프로젝트에서 사용할 돈 추가 함수...
     */
    fun addMoney(amount: Int, type: String, player: String = "SYSTEM") {
        money += amount

        MoneyLogManager.log(player, type, amount) //

        val playerObj = Bukkit.getPlayer(player)

        if (playerObj == null || !playerObj.isOnline) return

        val currentTotalEarned = MoneyLogManager.getPlayerTotalEarned(player)

        val previousTotalEarned = currentTotalEarned - amount

        for ((threshold, version) in missionMilestones) {
            if (previousTotalEarned < threshold && currentTotalEarned >= threshold) {
                Bukkit.getPluginManager().callEvent(
                    MissionEvent(
                        playerObj,
                        version,
                        MissionTypes.PLAYER_EP,
                        MissionTargets.EP,
                        1
                    )
                )
            }
        }
    }
}