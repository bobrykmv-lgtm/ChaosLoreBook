package com.chaossmp.lorebook;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChaosLoreBook extends JavaPlugin implements Listener, CommandExecutor {

    private static final MiniMessage MINI = MiniMessage.miniMessage();
    private NamespacedKey loreBookKey;

    @Override
    public void onEnable() {
        loreBookKey = new NamespacedKey(this, "is_lore_book");

        if (getCommand("lore") != null) {
            getCommand("lore").setExecutor(this);
        }
        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("ChaosLoreBook успешно активирован!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только для игроков!");
            return true;
        }

        giveLoreBook(player);
        player.sendMessage(ChatColor.DARK_PURPLE + "[Лор] Вы получили Кровные Хроники. Закройте книгу, чтобы она растворилась в воздухе.");
        return true;
    }

    private void giveLoreBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        if (meta != null) {
            meta.setTitle("§5Хроники ChaosSMP");
            meta.setAuthor("Первые Боги");

            meta.addPages(
                    Component.text(
                            "§8Глава I: Создание и Король\n\n" +
                                    "В начале была лишь пустота. Первые Боги соткали мир ChaosSMP из первородной магии. Когда мир ожил, на нем появились первые игроки — души из иных измерений.\n\n" +
                                    "Боги избрали воина по имени §cW1nUser§0 первым и вечным Королем, после чего ушли в тень наблюдателями."
                    ),
                    Component.text(
                            "§8Глава II: Проклятие Хаоса\n\n" +
                                    "Жажда зрелищ толкнула Богов вплести в ткань мира Источник Хаоса.\n\n" +
                                    "Каждая смерть забирает частицу души, а победа над врагом дарует силу Первых Богов. Хаос стал мерой истинной мощи воина."
                    ),
                    Component.text(
                            "§8Глава III: Орбитальные Пушки\n\n" +
                                    "Желая защитить свои земли, Король W1nUser и мастера сервера возвели Орбитальные Пушки — величные орудия, способные обрушить небесный свет на врагов."
                    ),
                    Component.text(
                            "§8Глава IV: Далекие Земли\n\n" +
                                    "На краях света из-за избытка хаоса рушится реальность. Там лежат Далекие Земли — аномальная зона, хранящая тайны и древние алтари."
                    )
            );

            meta.getPersistentDataContainer().set(loreBookKey, PersistentDataType.BYTE, (byte) 1);
            book.setItemMeta(meta);
        }

        player.getInventory().setItemInMainHand(book);
    }

    private boolean isLoreBook(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(loreBookKey, PersistentDataType.BYTE);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        removeLoreBookFromPlayer(player);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (isLoreBook(event.getItemDrop().getItemStack())) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removeLoreBookFromPlayer(event.getPlayer());
    }

    private void removeLoreBookFromPlayer(Player player) {
        if (isLoreBook(player.getInventory().getItemInMainHand())) {
            player.getInventory().setItemInMainHand(null);
        }
        if (isLoreBook(player.getInventory().getItemInOffHand())) {
            player.getInventory().setItemInOffHand(null);
        }
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (isLoreBook(contents[i])) {
                player.getInventory().setItem(i, null);
            }
        }
    }
}
