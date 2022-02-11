package net.foggies.testing;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*
    A utility class for easily editing the persistent
    data of players and items.
    
    Created by foggies 11/02/2022
    https://github.com/foggies1
 */
public class PDUtils {

    private ItemStack item;
    private ItemMeta meta;
    private Player player;
    private PersistentDataContainer container;
    private NamespacedKey key;

    public PDUtils(ItemStack item, String key) {
        this.item = item;
        this.key = new NamespacedKey(Testing.getPlugin(Testing.class), key);

        if (item.getItemMeta() != null) {
            this.meta = item.getItemMeta();
            this.container = item.getItemMeta().getPersistentDataContainer();
        }
    }

    public PDUtils(Player player, String key) {
        this.player = player;
        this.key = new NamespacedKey(Testing.getPlugin(Testing.class), key);
    }

    public <T extends Number, Z extends Number> void subtract(PersistentDataType<T, Z> dataType, Z value) {
        if (!has(dataType)) {
            set(dataType, value);
            return;
        }

        set(dataType, GenericUtils.subtract(get(dataType), value));
    }

    public <T extends Number, Z extends Number> void add(PersistentDataType<T, Z> dataType, Z value) {
        if (!has(dataType)) {
            set(dataType, value);
            return;
        }

        set(dataType, GenericUtils.add(get(dataType), value));
    }

    public <T, Z> Z get(PersistentDataType<T, Z> dataType) {
        return this.container.get(this.key, dataType);
    }

    public <T, Z> void set(PersistentDataType<T, Z> dataType, Z value) {
        this.container.set(this.key, dataType, value);
        saveItem();
    }

    public <T, Z> boolean has(PersistentDataType<T, Z> dataType) {
        return this.container.has(this.key, dataType);
    }

    public void saveItem() {
        if (this.item != null)
            this.item.setItemMeta(meta);
    }

}
