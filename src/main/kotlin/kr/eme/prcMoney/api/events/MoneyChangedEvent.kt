package kr.eme.prcMoney.api.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * 잔액이 변경될 때 발생하는 이벤트.
 *
 * @param player 변경을 일으킨 플레이어 (시스템/관리자 변경 시 null)
 * @param delta  변경량 (양수=증가, 음수=감소)
 * @param newBalance 변경 직후 잔액
 * @param type   변경 사유 식별자 (예: "ADD", "SUBTRACT", "SHOP_SELL", "MISSION_REWARD" 등)
 */
class MoneyChangedEvent(
    val player: Player?,
    val delta: Int,
    val newBalance: Int,
    val type: String,
) : Event() {

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}
