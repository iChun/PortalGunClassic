package me.ichun.mods.portalgunclassic.client.core;

import me.ichun.mods.portalgunclassic.client.render.RenderPortalProjectile;
import me.ichun.mods.portalgunclassic.client.render.TileRendererPortal;
import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import me.ichun.mods.portalgunclassic.common.core.ProxyCommon;
import me.ichun.mods.portalgunclassic.common.entity.EntityPortalProjectile;
import me.ichun.mods.portalgunclassic.common.tileentity.TileEntityPortal;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void preInit()
    {
        super.preInit();

        PortalGunClassic.eventHandlerClient = new EventHandlerClient();
        MinecraftForge.EVENT_BUS.register(PortalGunClassic.eventHandlerClient);

        ClientRegistry.registerKeyBinding(PortalGunClassic.eventHandlerClient.keySwitch);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPortal.class, new TileRendererPortal());

        RenderingRegistry.registerEntityRenderingHandler(EntityPortalProjectile.class, new RenderPortalProjectile.Factory());
    }
}
