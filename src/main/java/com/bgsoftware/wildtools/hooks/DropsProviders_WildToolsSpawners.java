package com.bgsoftware.wildtools.hooks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;
import java.util.List;

public final class DropsProviders_WildToolsSpawners implements DropsProvider {

    @Override
    public List<ItemStack> getBlockDrops(Block block) {
        List<ItemStack> drops = new ArrayList<>();

        if(!(block.getState() instanceof CreatureSpawner))
            return drops;

        drops.add(getSpawnerItem((CreatureSpawner) block.getState()));

        return drops;
    }

    @Override
    public boolean isSpawnersOnly() {
        return true;
    }

    public static ItemStack getSpawnerItem(CreatureSpawner creatureSpawner){
        ItemStack itemStack;

        try{
            itemStack = new ItemStack(Material.MOB_SPAWNER);
        }catch(Throwable ex){
            itemStack = new ItemStack(Material.matchMaterial("SPAWNER"));
        }

        try {
            BlockStateMeta blockStateMeta = (BlockStateMeta) itemStack.getItemMeta();
            blockStateMeta.setBlockState(creatureSpawner);
            itemStack.setItemMeta(blockStateMeta);
        }catch(Throwable ex){
            //noinspection deprecation
            itemStack.setDurability(creatureSpawner.getSpawnedType().getTypeId());
        }

        return itemStack;
    }

}