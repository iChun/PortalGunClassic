package me.ichun.mods.portalgunclassic.common;

import me.ichun.mods.portalgunclassic.client.core.EventHandlerClient;
import me.ichun.mods.portalgunclassic.common.core.EventHandlerServer;
import me.ichun.mods.portalgunclassic.common.core.ProxyCommon;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = PortalGunClassic.MOD_ID, name = PortalGunClassic.MOD_NAME,
        version = PortalGunClassic.VERSION,
        dependencies = "required-after:forge@[14.23.2.2624,)",
        acceptedMinecraftVersions = "[1.12,1.13)"
)
public class PortalGunClassic
{
    public static final String MOD_ID = "portalgunclassic";
    public static final String MOD_NAME = "PortalGunClassic";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MOD_ID)
    public static PortalGunClassic instance;

    @SidedProxy(clientSide = "me.ichun.mods.portalgunclassic.client.core.ProxyClient", serverSide = "me.ichun.mods.portalgunclassic.common.core.ProxyCommon")
    public static ProxyCommon proxy;

    public static EventHandlerClient eventHandlerClient;
    public static EventHandlerServer eventHandlerServer;

    public static Item itemPortalGun;
    public static Item itemPortalCore;

    public static Block blockPortalGun;

    public static SimpleNetworkWrapper channel;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
    }
}
