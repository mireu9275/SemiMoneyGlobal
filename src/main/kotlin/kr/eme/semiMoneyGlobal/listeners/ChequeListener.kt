package kr.eme.semiMoneyGlobal.listeners

import kr.eme.semiMoneyGlobal.main
import kr.eme.semiMoneyGlobal.managers.MoneyManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ChequeListener : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return

        val item: ItemStack = event.item ?: return
        if (item.type != Material.PAPER) return

        val meta = item.itemMeta ?: return
        if (meta.customModelData != 1001) return

        val container = meta.persistentDataContainer
        val key = NamespacedKey(main, "cheque_amount")
        val amount = container.get(key, PersistentDataType.INTEGER) ?: return

        val player = event.player
        MoneyManager.addMoney(amount, "CHEQUE_USE", player.name)
        player.sendMessage("§a수표를 사용하여 §e$amount EP§a를 획득했습니다.")

        item.amount = item.amount - 1
        if (item.amount <= 0) {
            player.inventory.remove(item)
        }
    }
}