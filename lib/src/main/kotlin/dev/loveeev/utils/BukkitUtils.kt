package dev.loveeev.utils

import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Cancellable
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object BukkitUtils {

    // Метод для вызова события
    fun fireEvent(event: Event) {
        Bukkit.getPluginManager().callEvent(event)
    }

    // Метод для проверки, было ли событие отменено
    fun isEventCanceled(event: Event): Boolean {
        fireEvent(event)
        return (event is Cancellable && (event as Cancellable).isCancelled)
    }

    // Метод для отправки сообщения всем игрокам на сервере
    fun broadcastMessage(message: String) {
        Bukkit.getOnlinePlayers().forEach { it.sendMessage(TextUtil.colorize(message)) }
    }

    // Метод для получения игрока по UUID
    fun getPlayer(uuid: UUID): Player? = Bukkit.getPlayer(uuid)

    // Метод для телепортации игрока на указанные координаты в указанном мире
    fun teleportPlayer(player: Player, world: World, x: Double, y: Double, z: Double) {
        player.teleport(Location(world, x, y, z))
    }

    // Метод для планирования задачи с задержкой
    fun scheduleTask(plugin: Plugin, task: Runnable, delay: Long): BukkitTask {
        return Bukkit.getScheduler().runTaskLater(plugin, task, delay)
    }

    // Метод для выполнения задачи повторяющейся с задержкой
    fun scheduleRepeatingTask(plugin: Plugin, task: Runnable, delay: Long, period: Long): BukkitTask {
        return Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period)
    }

    // Метод для отмены задачи по ее ID
    fun cancelTask(taskId: Int) {
        Bukkit.getScheduler().cancelTask(taskId)
    }

    // Метод для проверки, онлайн ли игрок
    fun isPlayerOnline(uuid: UUID): Boolean = getPlayer(uuid)?.isOnline == true
    fun isPlayerOnline(player: Player): Boolean = player.isOnline

    // Метод для проверки, есть ли у игрока определенный предмет в инвентаре
    fun hasItem(player: Player, item: ItemStack): Boolean = player.inventory.contains(item)

    // Метод для добавления предмета в инвентарь игрока
    fun giveItem(player: Player, item: ItemStack) {
        player.inventory.addItem(item)
    }

    // Метод для вывода в консоль сообщения с префиксом плагина
    fun logToConsole(message: String) {
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', message))
    }

    // Метод для выполнения действия через указанное время в тиках
    fun runTaskLater(plugin: Plugin, task: (BukkitRunnable) -> Unit, delay: Long) {
        object : BukkitRunnable() {
            override fun run() = task(this)
        }.runTaskLater(plugin, delay)
    }

    // Метод для получения списка всех онлайн игроков
    fun getOnlinePlayers(): List<Player> = Bukkit.getOnlinePlayers().toList()

    // Метод для возрождения игрока
    fun respawnPlayer(player: Player, plugin: JavaPlugin) {
        Bukkit.getScheduler().runTaskLater(plugin, Runnable { player.spigot().respawn() }, 1L)
    }

    // Метод для очистки инвентаря игрока
    fun clearInventory(player: Player) {
        player.inventory.clear()
    }

    // Метод для замены блока в мире
    fun setBlockAt(world: World, x: Int, y: Int, z: Int, material: Material) {
        world.getBlockAt(Location(world, x.toDouble(), y.toDouble(), z.toDouble())).type = material
    }

    // Метод для получения блока в мире по координатам
    fun getBlockAt(world: World, x: Int, y: Int, z: Int): Block {
        return world.getBlockAt(Location(world, x.toDouble(), y.toDouble(), z.toDouble()))
    }

    // Метод для проверки, существует ли мир
    fun worldExists(worldName: String): Boolean = Bukkit.getWorld(worldName) != null

    // Метод для загрузки мира по имени
    fun loadWorld(worldName: String): World? = Bukkit.createWorld(WorldCreator(worldName))

    // Метод для сохранения всех миров
    fun saveAllWorlds() {
        Bukkit.getWorlds().forEach { it.save() }
    }

    // Метод для запуска команды от имени консоли
    fun executeConsoleCommand(command: String) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }

    // Метод для проверки, находится ли игрок в указанном мире
    fun isPlayerInWorld(player: Player, world: World): Boolean = player.world == world
}