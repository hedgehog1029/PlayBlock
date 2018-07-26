package com.skcraft.playblock;

import com.sk89q.forge.ResponseTracker;
import com.skcraft.playblock.media.MediaResolver;
import com.skcraft.playblock.projector.BlockProjector;
import com.skcraft.playblock.projector.ItemRemote;
import com.skcraft.playblock.projector.ModItemBlock;
import com.skcraft.playblock.projector.TileEntityProjector;
import com.skcraft.playblock.queue.ExposedQueue;
import com.skcraft.playblock.queue.QueueManager;
import com.skcraft.playblock.queue.QueueSupervisor;
import com.skcraft.playblock.queue.SimpleQueueSupervisor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Initializes everything.
 */
public class SharedRuntime {

    private ResponseTracker tracker = new ResponseTracker();
    private QueueManager queueManager;
    private QueueSupervisor queueSupervisor;
    private MediaResolver mediaResolver;
    private SharedConfiguration config;
    public FMLEventChannel networkWrapper;

    public static Block blockProjector = null;
    public static Item itemRemote = null;
    public static Item itemProjector = null;

    public static FMLEventChannel getNetworkWrapper() {
        return PlayBlock.getRuntime().getSidedNetworkWrapper();
    }

    public FMLEventChannel getSidedNetworkWrapper() {
        return networkWrapper;
    }

    /**
     * Get the response tracker.
     * 
     * @return the tracker
     */
    public ResponseTracker getTracker() {
        return tracker;
    }

    /**
     * Gets the configuration.
     * 
     * @return the configuration
     */
    public SharedConfiguration getConfig() {
        return config;
    }

    /**
     * Called on FML pre-initialization.
     * 
     * @param event
     *            the event
     */
    public void preInit(FMLPreInitializationEvent event) {
        config = new SharedConfiguration("PlayBlock.cfg");

        blockProjector = new BlockProjector();
        itemRemote = new ItemRemote();
        itemProjector = new ModItemBlock(blockProjector);

        MinecraftForge.EVENT_BUS.register(new SharedRegistry());
    }

    /**
     * Called on FML initialization.
     * 
     * @param event
     *            the event
     */
    public void load(FMLInitializationEvent event) {
        queueManager = new QueueManager();
        mediaResolver = new MediaResolver();
        queueSupervisor = new SimpleQueueSupervisor(mediaResolver);
        NetworkRegistry.INSTANCE.registerGuiHandler(PlayBlock.instance, new GuiHandler());

        networkWrapper = NetworkRegistry.INSTANCE.newEventDrivenChannel(PlayBlock.CHANNEL_ID);
        networkWrapper.register(new PacketHandler());

        GameRegistry.registerTileEntity(TileEntityProjector.class, new ResourceLocation(PlayBlock.MOD_ID, "projector"));

        getConfig().save();
    }

    /**
     * Called on server start.
     * 
     * @param event
     *            the event
     */
    public void serverStarted(FMLServerStartedEvent event) {
    }

    /**
     * Called on server stop.
     * 
     * @param event
     *            the event
     */
    public void serverStopping(FMLServerStoppingEvent event) {
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public MediaResolver getMediaResolver() {
        return mediaResolver;
    }

    public QueueSupervisor getQueueSupervisor() {
        return queueSupervisor;
    }

    public void setQueueSupervisor(QueueSupervisor queueSupervisor) {
        this.queueSupervisor = queueSupervisor;
    }

    public void showProjectorGui(EntityPlayer player, TileEntityProjector tileEntity) {
        // Overriden on the client
    }

    public void showRemoteGui(EntityPlayer player, ExposedQueue queuable) {
        // Overriden on the client
    }

}
