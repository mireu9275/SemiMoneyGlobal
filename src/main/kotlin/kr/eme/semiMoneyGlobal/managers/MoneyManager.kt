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

        // 1. 로그를 먼저 저장해야 누적 금액에 반영됩니다.
        MoneyLogManager.log(player, type, amount) //

        val playerObj = Bukkit.getPlayer(player)

        // 플레이어가 오프라인이면 이벤트 처리를 할 필요가 없으므로 리턴
        if (playerObj == null || !playerObj.isOnline) return

        // 2. 로그 매니저를 통해 '현재까지의 총 누적 금액'을 가져옵니다.
        val currentTotalEarned = MoneyLogManager.getPlayerTotalEarned(player)

        // 3. '방금 들어온 금액'을 빼서 '이전 누적 금액'을 역산합니다.
        val previousTotalEarned = currentTotalEarned - amount

        // 4. 반복문으로 미션 달성 여부 체크 (중복 코드 제거)
        for ((threshold, version) in missionMilestones) {
            // "이전에는 목표치 미만이었는데" && "지금 목표치를 넘겼다면" -> 달성
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