package com.joonseo.cheststore_ifdeath;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
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

        Block block1 = chest1loc.getBlock();
        block1.setType(Material.CHEST);

        Block block2 = chest2loc.getBlock();
        block2.setType(Material.CHEST);

        BlockData data1 = block1.getBlockData();
        BlockData data2 = block2.getBlockData();

        if (data1 instanceof org.bukkit.block.data.type.Chest && data2 instanceof org.bukkit.block.data.type.Chest){

            Chest chestdata1 = (Chest) data1;
            Chest chestdata2 = (Chest) data2;

            chestdata1.setFacing(BlockFace.NORTH);
            chestdata1.setType(Chest.Type.LEFT);

            chestdata2.setFacing(BlockFace.NORTH);
            chestdata2.setType(Chest.Type.RIGHT);

            block1.setBlockData(chestdata1,true);
            block2.setBlockData(chestdata2,true);


            Inventory chest_inv = ((org.bukkit.block.Chest)block1.getState()).getInventory();

            for (ItemStack item : e.getDrops()) {
                if(item != null) {
                    chest_inv.addItem(item);
                }
            }

            e.getDrops().clear();


            InventoryHolder temp = chest_inv.getHolder();

            if (temp instanceof org.bukkit.block.DoubleChest){

                org.bukkit.block.DoubleChest chest_holder = (org.bukkit.block.DoubleChest) temp;

                if(chest_holder.getLeftSide() instanceof org.bukkit.block.Chest){
                    org.bukkit.block.Chest left = (org.bukkit.block.Chest) chest_holder.getLeftSide();
                    left.setCustomName(ChatColor.DARK_RED + player.getName() +"의 시체");
                    left.update();
                }

                if(chest_holder.getRightSide() instanceof org.bukkit.block.Chest){
                    org.bukkit.block.Chest right = (org.bukkit.block.Chest) chest_holder.getRightSide();
                    right.setCustomName(ChatColor.DARK_RED + player.getName() +"의 시체");
                    right.update();
                }



            }






        }





    }

}
