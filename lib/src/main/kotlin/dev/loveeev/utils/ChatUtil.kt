package dev.loveeev.utils

import org.bukkit.entity.Player


class ChatUtil(val prefix: String) {

    fun s(player: Player, message: String?) {
        player.sendMessage(TextUtil.colorize(prefix + message))
    }
}