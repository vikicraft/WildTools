package com.bgsoftware.wildtools.nms;

import com.bgsoftware.wildtools.recipes.AdvancedShapedRecipe;
import org.apache.commons.lang.Validate;
import org.bukkit.Chunk;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface NMSAdapter {

    String getVersion();

    default boolean isLegacy(){
        return true;
    }

    List<ItemStack> getBlockDrops(Player pl, Block bl, boolean silkTouch);

    List<ItemStack> getCropDrops(Player pl, Block bl);

    int getExpFromBlock(Block block, Player player);

    void dropExp(Location location, int exp);

    int getTag(ItemStack is, String key, int def);

    ItemStack setTag(ItemStack is, String key, int value);

    String getTag(ItemStack is, String key, String def);

    ItemStack setTag(ItemStack is, String key, String value);

    ItemStack getItemInHand(Player player);

    ItemStack getItemInHand(Player player, Event e);

    List<UUID> getTasks(ItemStack itemStack);

    ItemStack addTask(ItemStack itemStack, UUID taskId);

    ItemStack removeTask(ItemStack itemStack, UUID taskId);

    ItemStack clearTasks(ItemStack itemStack);

    void setItemInHand(Player player, ItemStack itemStack);

    void setItemInHand(Player player, ItemStack itemStack, Event event);

    boolean isFullyGrown(Block block);

    void setCropState(Block block, CropState cropState);

    Collection<Player> getOnlinePlayers();

    void setBlockFast(Location location, int combinedId);

    void refreshChunk(Chunk chunk, Set<Location> blocksList);

    int getCombinedId(Location location);

    int getFarmlandId();

    void setCombinedId(Location location, int combinedId);

    Enchantment getGlowEnchant();

    boolean isOutsideWorldborder(Location location);

    Object getBlockData(Material type, byte data);

    BlockPlaceEvent getFakePlaceEvent(Player player, Location location, Block copyBlock);

    void playPickupAnimation(LivingEntity livingEntity, Item item);

    boolean isAxeType(Material material);

    boolean isShovelType(Material material);

    default Collection<Entity> getNearbyEntities(Location location, double range){
        return new ArrayList<>();
    }

    default ItemStack[] parseChoice(Recipe recipe, ItemStack itemStack){
        return new ItemStack[] {itemStack};
    }

    default void setExpCost(InventoryView inventoryView, int expCost){

    }

    default int getExpCost(InventoryView inventoryView){
        return 0;
    }

    default String getRenameText(InventoryView inventoryView){
        return "";
    }

    default AdvancedShapedRecipe createRecipe(String toolName, ItemStack result){
        return new AdvancedRecipeClassImpl(result);
    }

    class AdvancedRecipeClassImpl extends ShapedRecipe implements AdvancedShapedRecipe {

        private static Field ingredientsField;

        static {
            try{
                ingredientsField = ShapedRecipe.class.getDeclaredField("ingredients");
                ingredientsField.setAccessible(true);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        private Map<Character, ItemStack> ingredients;

        public AdvancedRecipeClassImpl(org.bukkit.inventory.ItemStack result){
            super(result);
            updateIngredients();
        }

        @Override
        public AdvancedRecipeClassImpl shape(String... shape) {
            super.shape(shape);
            updateIngredients();
            return this;
        }

        @Override
        public AdvancedRecipeClassImpl setIngredient(char key, org.bukkit.inventory.ItemStack itemStack) {
            Validate.isTrue(this.ingredients.containsKey(key), "Symbol does not appear in the shape: ", key);
            this.ingredients.put(key, itemStack);
            return this;
        }

        @Override
        public ShapedRecipe toRecipe() {
            return this;
        }

        private void updateIngredients(){
            try{
                //noinspection unchecked
                ingredients = (Map<Character, org.bukkit.inventory.ItemStack>) ingredientsField.get(this);
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }

    }

}
