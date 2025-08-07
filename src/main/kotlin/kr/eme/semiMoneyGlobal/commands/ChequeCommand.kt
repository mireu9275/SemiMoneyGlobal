package kr.eme.semiMoneyGlobal.commands

import kr.eme.semiMoneyGlobal.main
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ChequeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§c콘솔에서는 사용할 수 없습니다.")
            return true
        }

        if (args.size != 2) {
            sender.sendMessage("§e사용법: /수표 <금액> <수량>")
            return true
        }

        val amount = args[0].toIntOrNull()
        val quantity = args[1].toIntOrNull()

        if (amount == null || quantity == null || amount <= 0 || quantity <= 0) {
            sender.sendMessage("§c올바른 금액과 수량을 입력해주세요.")
            return true
        }

        val cheque = createCheque(amount, quantity)
        sender.inventory.addItem(cheque)
        sender.sendMessage("§a수표를 발급했습니다: ${amount} EP × $quantity")
        return true
    }

    private fun createCheque(amount: Int, quantity: Int): ItemStack {
        val item = ItemStack(Material.PAPER, quantity)
        val meta = item.itemMeta!!
        meta.setDisplayName("§f[수표] §a$amount §fEP")
        meta.lore = listOf("§7우클릭으로 사용 시 EP 지급")

        val container = meta.persistentDataContainer
        val key = NamespacedKey(main, "cheque_amount")
        container.set(key, PersistentDataType.INTEGER, amount)

        meta.setCustomModelData(1001) // 수표 전용 모델 ID (리소스팩 사용 시)
        item.itemMeta = meta
        return item
    }
}
