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

    private final Plugin plugin;
    private ItemStack item;
    private ItemMeta meta;
    private Player player;
    private PersistentDataContainer container;
    private NamespacedKey key;

    public PDUtils(Plugin plugin, ItemStack item) {
        this.plugin = plugin;
        this.item = item;

        if (item.getItemMeta() != null) {
            this.meta = item.getItemMeta();
            this.container = this.meta.getPersistentDataContainer();
        }
    }

    public PDUtils(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.container = player.getPersistentDataContainer();
    }

    public <T extends Number, Z extends Number> PDUtils subtract(final String key, final PersistentDataType<T, Z> dataType, Z value) {
        setKey(key);

        if (!has(key, dataType)) {
            return set(key, dataType, value);
        }

        Z current = get(key, dataType);
        Validate.notNull(current, "There is no persistent data type on within this container with the key: " + key);

        return set(key, dataType, GenericUtils.subtract(current, value));
    }

    public <T extends Number, Z extends Number> PDUtils add(final String key, final PersistentDataType<T, Z> dataType, Z value) {
        setKey(key);

        if (!has(key, dataType)) {
            return set(key, dataType, value);
        }

        Z current = get(key, dataType);
        Validate.notNull(current, "There is no persistent data type on within this container with the key: " + key);

        return set(key, dataType, GenericUtils.add(current, value));
    }

    public <T, Z> Z get(final String key, final PersistentDataType<T, Z> dataType) {
        setKey(key);
        if (!has(key, dataType)) return null;
        return this.container.get(this.key, dataType);
    }

    public <T, Z> PDUtils set(final String key, final PersistentDataType<T, Z> dataType, Z value) {
        setKey(key);
        this.container.set(this.key, dataType, value);
        saveItem();
        return this;
    }

    public <T, Z> boolean has(final String key, final PersistentDataType<T, Z> dataType) {
        setKey(key);
        return this.container.has(this.key, dataType);
    }

    private void saveItem() {
        if (this.item != null)
            this.item.setItemMeta(meta);
    }

    private void setKey(final String key) {
        this.key = new NamespacedKey(this.plugin, key);
    }

}
