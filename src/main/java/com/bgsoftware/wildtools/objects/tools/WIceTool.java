package com.bgsoftware.wildtools.objects.tools;

import com.bgsoftware.wildtools.utils.items.ToolTaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import com.bgsoftware.wildtools.api.objects.ToolMode;
import com.bgsoftware.wildtools.api.objects.tools.IceTool;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class WIceTool extends WTool implements IceTool {

    private int radius;

    public WIceTool(Material type, String name, int radius){
        super(type, name, ToolMode.ICE);
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

        List<Block> toBeReplaced = new ArrayList<>();
        boolean reduceDurability = false;

        UUID taskId = ToolTaskManager.generateTaskId(usedItem, player.getInventory());

        outerLoop:
        for(int x = min.getBlockX(); x <= max.getBlockX(); x++){
            for(int z = min.getBlockZ(); z <= max.getBlockZ(); z++){
                for(int y = max.getBlockY(); y >= min.getBlockY(); y--){
                    Block targetBlock = block.getWorld().getBlockAt(x, y, z);
                    if(targetBlock.getType() == Material.ICE && plugin.getProviders().canBreak(player, targetBlock, this)){
                        toBeReplaced.add(targetBlock);
                        //Tool is using durability, reduces every block
                        if (isUsingDurability())
                            reduceDurablility(player, taskId);
                        if(plugin.getNMSAdapter().getItemInHand(player).getType() == Material.AIR)
                            break outerLoop;
                        reduceDurability = true;
                    }
                }
            }
        }

        //Tool is using durability, reduces every block
        if (reduceDurability && !isUsingDurability())
            reduceDurablility(player, taskId);

        //Setting all the blocks sync
        Bukkit.getScheduler().runTask(plugin, () -> {
           for(Block _block : toBeReplaced)
               _block.setType(Material.AIR);
        });

        ToolTaskManager.removeTask(taskId);

        return true;
    }

}
