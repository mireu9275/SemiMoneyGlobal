package kr.eme.prcMoney.managers

import kr.eme.prcMission.api.events.MissionEvent
import kr.eme.prcMission.enums.MissionVersion
import kr.eme.prcMission.objects.const.MissionTargets
import kr.eme.prcMission.objects.const.MissionTypes
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player

object MoneyManager {
    private var money: Int = 0

    private val missionMilestones = listOf(
        25_000 to MissionVersion.V1,
        40_000 to MissionVersion.V2,
        60_000 to MissionVersion.V2
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
    /**
     * 금액 차감 (부족할 경우 false 반환)
     * 0원이 될 경우 특정 발전 과제를 부여합니다.
     */
    fun subtractMoney(amount: Int, player: Player? = null): Boolean {
        if (money < amount) return false
        money -= amount

        // 잔액이 0원이 되었을 때 발전 과제 실행
        if (money == 0 && player != null) {
            doneAdvancement(player, "module/normal/not_enough_minerals")
        }

        return true
    }

    /**
     * 특정 발전 과제를 즉시 완료 처리합니다.
     */
    fun doneAdvancement(player: Player, adv: String) {
        val key = NamespacedKey("pcadv", adv)
        val advancement = Bukkit.getAdvancement(key) ?: return
        val progress = player.getAdvancementProgress(advancement)

        for (criteria in progress.remainingCriteria) {
            progress.awardCriteria(criteria)
        }
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