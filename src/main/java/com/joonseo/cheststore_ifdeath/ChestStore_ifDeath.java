package com.joonseo.cheststore_ifdeath;

import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ChestStore_ifDeath extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic

        Bukkit.getPluginManager().registerEvents(this,this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){

        Player player = e.getEntity();
        Location loc = player.getLocation();
        World world = loc.getWorld();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time_message = now.format(formatter);

        if (world == null) return;

        String message = ChatColor.YELLOW + "X: " + ChatColor.GREEN + loc.getBlockX() +
                ChatColor.YELLOW + " Y: " + ChatColor.GREEN + loc.getBlockY() +
                ChatColor.YELLOW + " Z: " + ChatColor.GREEN + loc.getBlockZ();

        player.sendMessage(ChatColor.AQUA+"당신은 "+ message +ChatColor.AQUA + "에서 " + ChatColor.GREEN + time_message
        + ChatColor.AQUA + " 에 죽었습니다.");


        Location chest1loc = loc.getBlock().getLocation();
        Location chest2loc = chest1loc.clone().add(1,0,0);

        chest1loc.getBlock().setType(Material.CHEST);
        chest2loc.getBlock().setType(Material.CHEST);

        BlockState state1 = chest1loc.getBlock().getState();
        BlockState state2 = chest2loc.getBlock().getState();

        if (state1 instanceof Chest && state2 instanceof Chest){

            Chest chest1 = (Chest) state1;
            Chest chest2 = (Chest) state2;

            Inventory chest_inv = chest1.getInventory();

            for (ItemStack item : e.getDrops()) {
                if(item != null) {
                    chest_inv.addItem(item);
                }
            }

            e.getDrops().clear();


            InventoryHolder temp = chest1.getInventory().getHolder();
            if(temp instanceof Chest){

                Chest chest_holder = (Chest) temp;
                chest_holder.setCustomName(ChatColor.DARK_RED + player.getName() + "의 시체");
                chest_holder.update();

            }






        }





    }

}
