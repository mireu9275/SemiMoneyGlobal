package kr.eme.prcMoney.api

import kr.eme.prcMoney.main
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object CheckAPI {

    private val CHEQUE_MATERIAL = Material.SADDLE
    private const val CHEQUE_CMD = 38
    private val CHEQUE_KEY get() = NamespacedKey(main, "cheque_amount")

    /**
     * 수표 아이템을 생성합니다.
     * @param amount 수표 1장당 EP 금액
     * @param quantity 수표 수량
     */
    fun createCheque(amount: Int, quantity: Int = 1): ItemStack {
        val item = ItemStack(CHEQUE_MATERIAL, quantity)
        val meta = item.itemMeta!!
        meta.setDisplayName("§f[토큰] §a${amount} §fEP")
        meta.lore = listOf("§7우클릭으로 사용 시 EP로 환전")
        meta.setCustomModelData(CHEQUE_CMD)
        meta.persistentDataContainer.set(CHEQUE_KEY, PersistentDataType.INTEGER, amount)
        item.itemMeta = meta
        return item
    }

    /**
     * 해당 아이템이 수표인지 확인합니다.
     */
    fun isCheque(item: ItemStack?): Boolean {
        if (item == null) return false
        if (item.type != CHEQUE_MATERIAL) return false
        val meta = item.itemMeta ?: return false
        if (!meta.hasCustomModelData() || meta.customModelData != CHEQUE_CMD) return false
        return meta.persistentDataContainer.has(CHEQUE_KEY, PersistentDataType.INTEGER)
    }

    /**
     * 수표의 EP 금액을 반환합니다. 수표가 아니면 null.
     */
    fun getChequeAmount(item: ItemStack?): Int? {
        if (item == null) return null
        if (item.type != CHEQUE_MATERIAL) return null
        val meta = item.itemMeta ?: return null
        if (!meta.hasCustomModelData() || meta.customModelData != CHEQUE_CMD) return null
        return meta.persistentDataContainer.get(CHEQUE_KEY, PersistentDataType.INTEGER)
    }
}
