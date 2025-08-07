package kr.eme.semiMoneyGlobal.commands

import kr.eme.semiMoneyGlobal.managers.MoneyLogManager
import kr.eme.semiMoneyGlobal.managers.MoneyManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object MoneyCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        // 명령어가 /ep add [금액] 형태일 경우
        if (args.size == 2 && args[0].equals("add", ignoreCase = true)) {
            handleAddCommand(sender, args[1])
            return true
        }

        // 아무 인자 없이 /ep 만 입력한 경우
        if (args.isEmpty()) {
            showCurrentMoney(sender)
            return true
        }

        // 잘못된 사용법 안내
        sender.sendMessage("§c사용법: /ep 또는 /ep add [금액]")
        return true
    }

    /**
     * 현재 서버 보유 금액을 출력
     */
    private fun showCurrentMoney(sender: CommandSender) {
        val current = MoneyManager.getMoney()
        sender.sendMessage("§a현재 보유 EP는 §e${current} EP§a 입니다.")
    }

    /**
     * /ep add 명령어 처리
     */
    private fun handleAddCommand(sender: CommandSender, amountArg: String) {
        // 관리자 권한 체크
        if (sender is Player && !sender.isOp) {
            sender.sendMessage("§c해당 명령어는 관리자만 사용할 수 있습니다.")
            return
        }

        val amount = amountArg.toIntOrNull()
        if (amount == null || amount <= 0) {
            sender.sendMessage("§c올바른 금액을 입력해주세요.")
            return
        }

        MoneyManager.addMoney(amount)
        MoneyLogManager.log("SYSTEM", "ADMIN_ADD", amount)
        sender.sendMessage("§e${amount} EP§a가 추가되었습니다.")
    }
}