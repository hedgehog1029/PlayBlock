package com.skcraft.playblock;

import com.skcraft.playblock.client.ClientRuntime;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@Mod(modid = PlayBlock.MOD_ID, name = PlayBlock.MOD_NAME, dependencies = PlayBlock.DEPENDENCIES)
public class PlayBlock {

    public static final String MOD_ID = "playblock";
    public static final String MOD_NAME = "PlayBlock";
    public static final String DEPENDENCIES = "after:opencomputers@[1.7.0,)";
    public static final String CHANNEL_ID = "PlayBlock";

    public static Logger log;

    @Instance
    public static PlayBlock instance;

    @SidedProxy(serverSide = "com.skcraft.playblock.SharedRuntime", clientSide = "com.skcraft.playblock.client.ClientRuntime")
    public static SharedRuntime runtime;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
        runtime.preInit(event);
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        runtime.load(event);
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        runtime.serverStarted(event);
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        runtime.serverStopping(event);
    }

    /**
     * Get the runtime, which may be either a {@link ClientRuntime} or a
     * {@link SharedRuntime}.
     * 
     * @return the runtime
     */
    public static SharedRuntime getRuntime() {
        return runtime;
    }

    /**
     * Get the client runtime.
     * 
     * @return the client runtime
     */
    @SideOnly(Side.CLIENT)
    public static ClientRuntime getClientRuntime() {
        return (ClientRuntime) runtime;
    }

    public static void log(Level level, String message) {
        log.log(level, message);
    }

    public static void log(Level level, String message, Throwable t) {
        log.log(level, message);
        t.printStackTrace();
    }

    public static void logf(Level level, String format, Object... data) {
        String message = String.format(format, data);
        log(level, message);
    }

}
