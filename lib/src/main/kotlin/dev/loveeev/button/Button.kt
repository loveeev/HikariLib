package dev.loveeev.button


import dev.loveeev.advancedgui.AdvancedGui
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

abstract class Button {
    //Установка предмета
    abstract val item: ItemStack?

    //При нажатие на меню
    abstract fun onClickedInMenu(player: Player?, menu: AdvancedGui?, clickType: ClickType?)
}