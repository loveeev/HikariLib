package dev.loveeev.utils

import net.md_5.bungee.api.ChatColor

/**
 * Утилитарный класс для работы с цветным текстом в Minecraft.
 */
object TextUtil {

    /**
     * Преобразует строку, заменяя `&`-коды на цветовые коды Minecraft.
     *
     * @param message Строка с цветовыми кодами `&` и '&#"
     * @return Строка с раскрашенным текстом
     */
    fun colorize(message: String): String = translateColorCodes(message)

    /**
     * Преобразует список строк, заменяя `&`-коды на цветовые коды Minecraft.
     *
     * @param messages Список строк с цветовыми кодами `&`и '&#"
     * @return Список раскрашенных строк
     */
    fun colorize(messages: List<String>?): List<String> =
        messages.orEmpty().map { translateColorCodes(it) }

    /**
     * Удаляет все цветовые коды из текста, оставляя только обычный текст.
     *
     * @param message Строка с цветными кодами
     * @return Очищенная строка без цветовых кодов
     */
    fun stripColor(message: String): String = ChatColor.stripColor(message) ?: ""

    /**
     * Делает первую букву каждого слова заглавной.
     *
     * @param text Исходная строка
     * @return Строка с заглавными буквами в каждом слове
     */
    fun capitalizeWords(text: String): String =
        text.split(" ").joinToString(" ") { it.lowercase().replaceFirstChar { c -> c.uppercase() } }

    /**
     * Центрирует текст, добавляя пробелы по бокам. Полезно для табличек и чатов.
     *
     * @param text Исходный текст
     * @param width Общая ширина строки (обычно 40 для чата)
     * @return Центрированный текст
     */
    fun centerText(text: String, width: Int = 40): String {
        val length = stripColor(text).length
        val padding = (width - length) / 2
        return " ".repeat(maxOf(0, padding)) + text
    }

    /**
     * Вспомогательный метод для преобразования `&`-кодировки в формат `ChatColor`.
     *
     * @param text Исходная строка с `&`-кодами
     * @return Раскрашенная строка
     */
    private fun translateColorCodes(text: String): String {
        val parts = text.split("&").mapIndexed { index, part ->
            when {
                index == 0 -> part
                part.startsWith("#") && part.length >= 7 -> "${ChatColor.of(part.substring(0, 7))}${part.substring(7)}"
                else -> ChatColor.translateAlternateColorCodes('&', "&$part")
            }
        }
        return parts.joinToString("")
    }
}
