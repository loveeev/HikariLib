package dev.loveeev.advancedgui;

import dev.loveeev.button.Button;
import dev.loveeev.shapes.MenuSlots;
import dev.loveeev.utils.TextUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class AdvancedGui implements Listener {

    @Getter
    private Inventory inventory;
    @Getter
    private Integer size = 9 * 3;
    @Setter
    private Sound sound;
    @Getter
    private String name = "Menu";
    // ГЕТТЕРЫ
    @Getter
    private Player viewer;
    private List<Integer> lockedSlots = new ArrayList<>();
    @Getter
    private Map<Integer, Button> buttons = new HashMap<>();
    @Getter
    @Setter
    private ItemStack wrapper;

    public AdvancedGui(Plugin plugin, Player viewer) {
        this.viewer = viewer;
        this.inventory = Bukkit.createInventory(null,size,name);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setupInventory();
    }

    protected final List<Integer> getLockedSlots() {
        return this.lockedSlots;
    }

    public void display() {
        if (viewer != null && inventory != null) {
            if (!isViewerView()) {
                if (sound != null) {
                    viewer.playSound(viewer.getLocation(), sound, 1.0f, 1.0f);
                }
                viewer.openInventory(inventory);
            }
        }
    }


    public boolean isViewerView() {
        if (viewer == null || inventory == null) {
            return false;
        }

        Inventory openInventory = viewer.getOpenInventory().getTopInventory();
        return openInventory.equals(inventory);
    }

    public void closeMenu() {
        CompletableFuture.runAsync(() -> {
            if (isViewerView()) {
                viewer.closeInventory();
            }
        });
    }

    public abstract void setupInventory();

    public void delItem(int slot) {
        CompletableFuture.runAsync(() -> this.inventory.setItem(slot, null));
    }

    public void setItem(ItemStack itemStack, Integer slot) {
        CompletableFuture.runAsync(() -> inventory.setItem(slot, itemStack));
    }
    protected final void setLockedSlots(MenuSlots.Shape shape) {
        this.setLockedSlots(shape, this.getSize());
    }

    protected final void setLockedSlots(MenuSlots.Shape shape, int size) {
        this.setLockedSlots(MenuSlots.AUTO(shape, size));
    }
    protected final void setLockedSlots(MenuSlots.SizedShape sizedShape) {
        this.setLockedSlots(sizedShape.getSlots());
    }

    public void setSize(Integer size) {
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, name);
    }


    public void setName(String name) {
        String coloredName = new String(TextUtil.INSTANCE.colorize(name).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        if (coloredName.length() > 32) {
            coloredName = coloredName.substring(0, 32);
        }
        this.name = coloredName;
        this.inventory = Bukkit.createInventory(null, size, this.name);
    }

    protected final boolean isButton(int slot) {
        return this.getButtons().containsKey(slot);
    }


    public void setLockedSlots(Integer... slots) {
        CompletableFuture.runAsync(() -> {
            this.lockedSlots.clear();
            for (Integer slot : slots) {
                if (!this.lockedSlots.contains(slot)) {
                    this.lockedSlots.add(slot);
                    setItem(wrapper, slot);
                }
            }
        });
    }

    public void setButton(Button button, int slot) {
        buttons.put(slot, button);
        setItem(button.getItem(), slot);
    }


    public void addButton(Button button) {
        CompletableFuture.runAsync(() -> {
            for (int slot = 0; slot < size; slot++) {
                if (!buttons.containsKey(slot)) {
                    setButton(button, slot);
                    return;
                }
            }
        });
    }

    // Перезагрузка меню
    public void refreshMenu() {
        CompletableFuture.runAsync(() -> {
            this.inventory.clear();
            for (Map.Entry<Integer, Button> entry : buttons.entrySet()) {
                setItem(entry.getValue().getItem(), entry.getKey());
            }
            if (isViewerView()) {
                viewer.closeInventory();
                viewer.openInventory(inventory);
            }
        });
    }

    public void delButton(int slot) {
        CompletableFuture.runAsync(() -> this.buttons.remove(slot));
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < event.getInventory().getSize() && buttons.containsKey(slot)) {
                Button button = buttons.get(slot);
                Player player = (Player) event.getWhoClicked();
                ClickType clickType = event.getClick();
                button.onClickedInMenu(player, this, clickType);
            }
        }
    }

    public Player getPlayer() {
        return this.viewer;
    }

}