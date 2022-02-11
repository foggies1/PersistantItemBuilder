package net.foggies.testing;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

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
    private final NamespacedKey key;

    public PDUtils(Plugin plugin, ItemStack item, String key) {
        this.item = item;
        this.key = new NamespacedKey(plugin, key);

        if (item.getItemMeta() != null) {
            this.meta = item.getItemMeta();
            this.container = this.meta.getPersistentDataContainer();
        }
    }

    public PDUtils(Plugin plugin, String key) {
        this.key = new NamespacedKey(plugin, key);
    }

    public <T extends Number, Z extends Number> void subtract(final PersistentDataType<T, Z> dataType, Z value) {
        if (!has(dataType)) {
            set(dataType, value);
            return;
        }

        Z current = get(dataType);
        Validate.notNull(current, "There is no persistent data type on within this container with the key: " + key);

        set(dataType, GenericUtils.subtract(current, value));
    }

    public <T extends Number, Z extends Number> void add(final PersistentDataType<T, Z> dataType, Z value) {
        if (!has(dataType)) {
            set(dataType, value);
            return;
        }

        Z current = get(dataType);
        Validate.notNull(current, "There is no persistent data type on within this container with the key: " + key);

        set(dataType, GenericUtils.add(current, value));
    }

    public <T, Z> Z get(final PersistentDataType<T, Z> dataType) {
        if (!has(dataType)) return null;
        return this.container.get(this.key, dataType);
    }

    public <T, Z> void set(final PersistentDataType<T, Z> dataType, Z value) {
        this.container.set(this.key, dataType, value);
        saveItem();
    }

    public <T, Z> boolean has(final PersistentDataType<T, Z> dataType) {
        return this.container.has(this.key, dataType);
    }

    public void saveItem() {
        if (this.item != null)
            this.item.setItemMeta(meta);
    }

}
