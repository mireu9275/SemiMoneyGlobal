package kr.eme.prcMoney.commands

import kr.eme.prcMoney.api.CheckAPI
import kr.eme.prcMoney.managers.MoneyManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ChequeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§c콘솔에서는 사용할 수 없습니다.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§e사용법: /token <금액> [수량]")
            return true
        }

        val amount = args[0].toIntOrNull()
        val quantity = if (args.size >= 2) args[1].toIntOrNull() else 1

        if (amount == null || amount <= 0) {
            sender.sendMessage("§c올바른 금액을 입력해주세요.")
            return true
        }

        if (quantity == null || quantity <= 0) {
            sender.sendMessage("§c올바른 수량을 입력해주세요.")
            return true
        }

        val totalCost = amount * quantity

        // EP 잔액 확인
        if (MoneyManager.getMoney() < totalCost) {
            sender.sendMessage("§cEP가 부족합니다. (필요: ${totalCost} EP, 보유: ${MoneyManager.getMoney()} EP)")
            return true
        }

        // 인벤토리 빈 칸 확인
        if (sender.inventory.firstEmpty() == -1) {
            sender.sendMessage("§c인벤토리에 빈 칸이 없습니다. 공간을 확보한 후 다시 시도해주세요.")
            return true
        }

        // EP 차감
        MoneyManager.subtractMoney(totalCost, sender)

        // 수표 생성 및 지급
        val cheque = CheckAPI.createCheque(amount, quantity)
        sender.inventory.addItem(cheque)
        sender.sendMessage("§a수표를 발급했습니다: §e${amount} EP §f× §e${quantity}장 §7(총 ${totalCost} EP 차감)")
        return true
    }
}
