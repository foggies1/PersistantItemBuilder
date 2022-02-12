package net.foggies.testing;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ItemBuilder {

    private final Plugin plugin;
    private ItemStack itemStack;
    private Material material;
    private int amount;
    private String displayName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private Map<String, PDValue<?, ?>> data; // key -> datatype and value
    private boolean hideFlags = false;

    public ItemBuilder(Plugin plugin, ItemStack itemStack) {
        this.plugin = plugin;
        this.itemStack = itemStack;
    }

    public ItemBuilder(Plugin plugin, Material material) {
        this.plugin = plugin;
        this.material = material;
    }

    public ItemBuilder(Plugin plugin, String material) {
        this.plugin = plugin;
        this.material = Material.valueOf(material.toUpperCase());
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (this.enchantments == null) this.enchantments = new ConcurrentHashMap<>();
        if (this.enchantments.containsKey(enchantment))
            this.enchantments.replace(enchantment, level);
        else
            this.enchantments.put(enchantment, level);
        return this;
    }

    public int getEnchantmentLevel(Enchantment enchantment) {
        if (this.enchantments == null) this.enchantments = new ConcurrentHashMap<>();
        if (this.itemStack == null) return -1;
        return this.enchantments.get(enchantment);
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.displayName = name;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder hideFlags(boolean value) {
        this.hideFlags = value;
        return this;
    }

    public <T, Z> ItemBuilder putData(String key, PersistentDataType<T, Z> dataType, Z value) {
        if (this.data == null) this.data = new ConcurrentHashMap<>();
        if (this.data.containsKey(key))
            this.data.replace(key, new PDValue<>(dataType, value));
        else
            this.data.put(key, new PDValue<>(dataType, value));
        return this;
    }

    public <T, Z> Z getData(String key, PersistentDataType<T, Z> dataType) {
        if (this.data == null) this.data = new ConcurrentHashMap<>();
        if (this.itemStack == null) return null;
        return new PDUtils(plugin, itemStack).get(key, dataType);
    }

    public ItemStack makeItem() {
        if (this.material == null) this.material = Material.STONE;
        if (this.displayName == null) this.displayName = this.material.name();
        if (this.amount == -1) this.amount = 1;
        if (this.lore == null) this.lore = new ArrayList<>();
        if (this.data == null) this.data = new ConcurrentHashMap<>();
        if (this.enchantments == null) this.enchantments = new ConcurrentHashMap<>();

        ItemStack itemStack = new ItemStack(this.material, this.amount);
        if (this.itemStack != null) itemStack = this.itemStack;

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setLore(colorLore(this.lore));
        itemMeta.setDisplayName(color(this.displayName));

        if (!this.enchantments.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> enchants : enchantments.entrySet()) {
                itemMeta.addEnchant(enchants.getKey(), enchants.getValue(), true);
            }
        }

        if (hideFlags) itemMeta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(itemMeta);

        // Sets the persistent data after we set the item stack's meta.
        if (!this.data.isEmpty()) {
            for (Map.Entry<String, PDValue<?, ?>> values : data.entrySet()) {
                new PDUtils(this.plugin, itemStack)
                        .set(values.getKey(), (PersistentDataType<Object, Object>) values.getValue().getDataType(), values.getValue().getValue());
            }
        }

        return itemStack;
    }

    private String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private List<String> colorLore(List<String> lore) {
        List<String> output = new ArrayList<>();
        lore.forEach(line -> output.add(color(line)));
        return output;
    }


}
