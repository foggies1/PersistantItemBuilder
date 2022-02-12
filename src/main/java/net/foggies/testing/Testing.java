package net.foggies.testing;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class Testing extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        ItemStack itemStack = new ItemBuilder(this, "diamond_pickaxe")
                .setAmount(1)
                .setDisplayName("&3&lA Really Cool Pickaxe")
                .setLore(
                        "&7This is a really cool pickaxe that",
                        "&7breaks blocks insanely fast..."
                )
                .addEnchantment(Enchantment.DIG_SPEED, 100)
                .hideFlags(true)
                .putData("level", PersistentDataType.LONG, 1L)
                .makeItem();

        Player player = e.getPlayer();
        player.getInventory().addItem(itemStack);

    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        ItemBuilder itemBuilder = new ItemBuilder(this, itemInHand);

        if (itemBuilder.getData("level", PersistentDataType.LONG) == null) return;
        long level = itemBuilder.getData("level", PersistentDataType.LONG);
        if (level == -1) return;

        itemBuilder.putData("level", PersistentDataType.LONG, level + 1);
        player.getInventory().setItemInMainHand(itemBuilder.makeItem());

        player.sendMessage("You've levelled your item up to " + (level + 1));

    }

}
