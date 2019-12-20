package com.bgsoftware.wildtools.objects.tools;

import com.bgsoftware.wildtools.api.objects.ToolMode;
import com.bgsoftware.wildtools.api.objects.tools.DrainTool;
import com.bgsoftware.wildtools.utils.items.ToolTaskManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class WDrainTool extends WTool implements DrainTool {

    private int radius;

    public WDrainTool(Material type, String name, int radius){
        super(type, name, ToolMode.DRAIN);
        this.radius = radius;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public boolean onBlockInteract(PlayerInteractEvent e) {
        return handleUse(e.getPlayer(), e.getItem(), e.getPlayer().getLocation().getBlock());
    }

    @Override
    public boolean onAirInteract(PlayerInteractEvent e) {
        return handleUse(e.getPlayer(), e.getItem(), e.getPlayer().getLocation().getBlock());
    }

    private boolean handleUse(Player player, ItemStack usedItem, Block block){
        Location max = block.getLocation().clone().add(radius, radius, radius),
                min = block.getLocation().clone().subtract(radius, radius, radius);

        UUID taskId = ToolTaskManager.generateTaskId(usedItem, player.getInventory());

        boolean reduceDurability = false;

        outerLoop:
        for(int x = min.getBlockX(); x <= max.getBlockX(); x++){
            for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++){
                for(int y = max.getBlockY(); y >= min.getBlockY(); y--){
                    Block targetBlock = block.getWorld().getBlockAt(x, y, z);
                    if(targetBlock.getType() == Material.ICE && plugin.getProviders().canBreak(player, targetBlock, this)){
                        targetBlock.setType(Material.AIR);
                        if(isUsingDurability())
                            reduceDurablility(player, taskId);
                        if(plugin.getNMSAdapter().getItemInHand(player).getType() == Material.AIR)
                            break outerLoop;
                        reduceDurability = true;
                    }
                }
            }
        }

        if(reduceDurability && !isUsingDurability())
            reduceDurablility(player, taskId);

        ToolTaskManager.removeTask(taskId);

        return true;
    }

}
