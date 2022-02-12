# PersistantItemBuilder
A ItemBuilder that supports adding persistent data to items.

# Example 

Giving the player and item via the ItemBuilder class.
```java
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
```

Altering the player's item in their hand by adding one to their persistent level
each time using the ItemBuilder class.
```java
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
```

