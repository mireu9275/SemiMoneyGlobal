package kr.eme.prcMoney.listeners

import kr.eme.prcMoney.api.CheckAPI
import kr.eme.prcMoney.managers.MoneyManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object ChequeListener : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return

        val item = event.item ?: return
        val amount = CheckAPI.getChequeAmount(item) ?: return

        event.isCancelled = true

        val player = event.player
        MoneyManager.addMoney(amount, "CHEQUE_USE", player.name)
        player.sendMessage("§a수표를 사용하여 §e${amount} EP§a를 획득했습니다.")

        item.amount -= 1
    }
}
