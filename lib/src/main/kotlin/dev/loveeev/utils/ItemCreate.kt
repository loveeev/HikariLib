package dev.loveeev.utils

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Утилита для удобного создания предметов в Minecraft с кастомными параметрами.
 */
class ItemCreate {
    private var item: ItemStack? = null
    private var meta: ItemMeta? = null
    private var name: String? = null
    private var amount = 1 // Количество предметов
    private var damage: Short = 0 // Повреждение предмета
    private val lores: MutableList<String> = mutableListOf()
    private val enchants: MutableMap<Enchantment, Int> = mutableMapOf()
    private var material: Material? = null
    private var unbreakable = false
    private var hideTags = false
    private var durab: Short = -1
    private var modelData: Int? = null

    /**
     * Устанавливает имя предмета с поддержкой цветовых кодов.
     *
     * @param name Название предмета
     * @return Текущий объект `ItemCreate` для цепочки вызовов
     */
    fun name(name: String): ItemCreate = apply {
        this.name = TextUtil.colorize(name)
    }

    /**
     * Устанавливает кастомную модель предмета.
     *
     * @param modelData ID кастомной модели
     * @return Текущий объект `ItemCreate` для цепочки вызовов
     */
    fun setCustomModelData(modelData: Int?): ItemCreate = apply {
        this.modelData = modelData
    }

    /**
     * Добавляет описание (лоры) предмета.
     *
     * @param lores Список строк для описания
     * @return Текущий объект `ItemCreate`
     */
    fun lore(lores: List<String>): ItemCreate = apply {
        this.lores.addAll(lores.map { TextUtil.colorize(it) })
    }

    /**
     * Добавляет одно или несколько описаний.
     *
     * @param lores Текстовые строки для описания
     * @return Текущий объект `ItemCreate`
     */
    fun lore(vararg lores: String): ItemCreate = apply {
        this.lores.addAll(lores.map { TextUtil.colorize(it) })
    }

    /**
     * Устанавливает материал предмета.
     *
     * @param material Материал предмета
     * @return Текущий объект `ItemCreate`
     */
    fun material(material: Material): ItemCreate = apply {
        this.material = material
    }

    /**
     * Устанавливает количество предметов.
     *
     * @param amount Количество предметов
     * @return Текущий объект `ItemCreate`
     */
    fun amount(amount: Int): ItemCreate = apply {
        this.amount = amount
    }

    /**
     * Устанавливает уровень прочности предмета.
     *
     * @param damage Уровень прочности
     * @return Текущий объект `ItemCreate`
     */
    fun damage(damage: Short): ItemCreate = apply {
        this.damage = damage
    }

    /**
     * Устанавливает уровень прочности (альтернативный вариант).
     *
     * @param durability Прочность предмета
     * @return Текущий объект `ItemCreate`
     */
    fun durability(durability: Short): ItemCreate = apply {
        this.durab = durability
    }

    /**
     * Добавляет зачарование к предмету.
     *
     * @param enchantment Тип зачарования
     * @param level Уровень зачарования (по умолчанию 1)
     * @return Текущий объект `ItemCreate`
     */
    fun enchant(enchantment: Enchantment, level: Int = 1): ItemCreate = apply {
        enchants[enchantment] = level
    }

    /**
     * Устанавливает предмет как неразрушаемый.
     *
     * @param unbreakable `true`, если предмет должен быть неразрушаемым
     * @return Текущий объект `ItemCreate`
     */
    fun unbreakable(unbreakable: Boolean): ItemCreate = apply {
        this.unbreakable = unbreakable
    }

    /**
     * Скрывает теги предмета (например, атрибуты и зачарования).
     *
     * @param hideTags `true`, если нужно скрыть теги
     * @return Текущий объект `ItemCreate`
     */
    fun hideTags(hideTags: Boolean): ItemCreate = apply {
        this.hideTags = hideTags
    }

    /**
     * Устанавливает кастомные метаданные предмета.
     *
     * @param meta Объект `ItemMeta`
     * @return Текущий объект `ItemCreate`
     */
    fun meta(meta: ItemMeta): ItemCreate = apply {
        this.meta = meta
    }

    /**
     * Создает предмет с установленными параметрами.
     *
     * @return Готовый `ItemStack`, или `null`, если материал не задан
     */
    fun build(): ItemStack? {
        val mat = material ?: return null
        if (mat.isAir) return null

        item = ItemStack(mat, amount)
        meta = item?.itemMeta ?: return null

        meta?.apply {
            name?.let { setDisplayName(it) }
            if (lores.isNotEmpty()) lore = lores
            isUnbreakable = unbreakable
            modelData?.let { setCustomModelData(it) }
            if (hideTags) addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
        }

        item?.apply {
            itemMeta = meta
            durability = durab
            enchants.forEach { (enchantment, level) -> addUnsafeEnchantment(enchantment, level) }
        }

        return item
    }

    /**
     * Добавляет зачарование в список (без возврата объекта).
     *
     * @param enchantment Тип зачарования
     * @param level Уровень зачарования
     */
    fun addEnchantment(enchantment: Enchantment, level: Int) {
        enchants[enchantment] = level
    }

    companion object {
        /**
         * Создает `ItemCreate` на основе указанного материала.
         *
         * @param material Материал предмета
         * @return Новый объект `ItemCreate`
         */
        fun from(material: Material?): ItemCreate {
            if (material == null) {
                Bukkit.getLogger().severe("ERROR - MATERIAL NULL")
            }
            return ItemCreate().material(material ?: Material.AIR)
        }
    }
}
