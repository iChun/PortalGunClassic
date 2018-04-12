package me.ichun.mods.portalgunclassic.common.core;

import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import me.ichun.mods.portalgunclassic.common.entity.EntityPortalProjectile;
import me.ichun.mods.portalgunclassic.common.packet.PacketEntityLocation;
import me.ichun.mods.portalgunclassic.common.packet.PacketPortalStatus;
import me.ichun.mods.portalgunclassic.common.packet.PacketRequestTeleport;
import me.ichun.mods.portalgunclassic.common.packet.PacketSwapType;
import me.ichun.mods.portalgunclassic.common.tileentity.TileEntityPortal;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class ProxyCommon
{
    public void preInit()
    {
        GameRegistry.registerTileEntity(TileEntityPortal.class, "portalgunclassic:tile_portal");

        EntityRegistry.registerModEntity(new ResourceLocation("portalgunclassic", "portal_projectile"), EntityPortalProjectile.class, "portalgunclassic_portal_projectile", 0, PortalGunClassic.instance, 256, 1, true);

        PortalGunClassic.eventHandlerServer = new EventHandlerServer();
        MinecraftForge.EVENT_BUS.register(PortalGunClassic.eventHandlerServer);

        PortalGunClassic.channel = new SimpleNetworkWrapper(PortalGunClassic.MOD_NAME);
        PortalGunClassic.channel.registerMessage(new PacketSwapType.Handler(), PacketSwapType.class, 0, Side.SERVER);
        PortalGunClassic.channel.registerMessage(new PacketPortalStatus.Handler(), PacketPortalStatus.class, 1, Side.CLIENT);
        PortalGunClassic.channel.registerMessage(new PacketRequestTeleport.Handler(), PacketRequestTeleport.class, 2, Side.SERVER);
        PortalGunClassic.channel.registerMessage(new PacketEntityLocation.Handler(), PacketEntityLocation.class, 3, Side.CLIENT);


    }
}
