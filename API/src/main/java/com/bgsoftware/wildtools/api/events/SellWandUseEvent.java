package com.bgsoftware.wildtools.api.events;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
/**
 * SellWandUseEvent is called when a sell wand is used.
 */
public final class SellWandUseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Chest chest;
    private final Player player;

    private boolean cancelled;
    private String sellMessage;
    private double price, multiplier;

    /**
     * The constructor of the event.
     * @param player The player who used the wand.
     * @param chest The chest its contents were sold.
     * @param price The total price for all the items.
     * @param sellMessage The message that will be sent to the player.
     *
     * @deprecated See HarvesterHoeSellEvent(Player, Chest, Double, Double, String)
     */
    @Deprecated
    public SellWandUseEvent(Player player, Chest chest, double price, String sellMessage){
        this(player, chest, price, 1, sellMessage);
    }

    /**
     * The constructor of the event.
     * @param player The player who used the wand.
     * @param chest The chest its contents were sold.
     * @param price The total price for all the items.
     * @param multiplier The multiplier for the price.
     * @param sellMessage The message that will be sent to the player.
     *
     * @deprecated See HarvesterHoeSellEvent(Player, Chest, Double, Double, String)
     */
    public SellWandUseEvent(Player player, Chest chest, double price, double multiplier, String sellMessage){
        super(true);
        this.player = player;
        this.chest = chest;
        this.sellMessage = sellMessage;
        this.price = price;
        this.multiplier = multiplier;
        this.cancelled = false;
    }

    /**
     * Get the player who used the wand.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the chest its contents were sold.
     */
    public Chest getChest(){
        return chest;
    }

    /**
     * Get the total price for all the items.
     */
    public double getPrice(){
        return price;
    }

    /**
     * Set the total price for all the items.
     * @param price The new total price.
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * Get the multiplier for the price.
     */
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * Set the multiplier for the price.
     * @param multiplier The new multiplier.
     */
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Get the message that will be sent to the player.
     */
    public String getMessage(){
        return sellMessage;
    }

    /**
     * Set a new message to be sent to the player.
     * @param sellMessage The new message.
     */
    public void setMessage(String sellMessage){
        this.sellMessage = sellMessage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
