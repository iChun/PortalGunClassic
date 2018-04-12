package me.ichun.mods.portalgunclassic.common.core;

import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import me.ichun.mods.portalgunclassic.common.block.BlockPortal;
import me.ichun.mods.portalgunclassic.common.item.ItemPortalGun;
import me.ichun.mods.portalgunclassic.common.sounds.SoundRegistry;
import me.ichun.mods.portalgunclassic.common.world.PortalSavedData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;

public class EventHandlerServer
{
    @SubscribeEvent
    public void onRegisterBlock(RegistryEvent.Register<Block> event)
    {
        PortalGunClassic.blockPortalGun = new BlockPortal();
        event.getRegistry().register(PortalGunClassic.blockPortalGun);
    }

    @SubscribeEvent
    public void onRegisterItem(RegistryEvent.Register<Item> event)
    {
        PortalGunClassic.itemPortalGun = new ItemPortalGun();
        event.getRegistry().register(PortalGunClassic.itemPortalGun);
    }

    @SubscribeEvent
    public void onRegisterSound(RegistryEvent.Register<SoundEvent> event)
    {
        SoundRegistry.init(event.getRegistry());
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        updatePlayerDimensionStatus(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        updatePlayerDimensionStatus(event.player);
    }

    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event)
    {
        updatePlayerDimensionStatus(event.player);
    }

    public void updatePlayerDimensionStatus(EntityPlayer player)
    {
        PortalSavedData data = getSaveData(player.getEntityWorld());
        PortalGunClassic.channel
    }

    public PortalSavedData getSaveData(World world)
    {
        PortalSavedData data = (PortalSavedData)world.loadData(PortalSavedData.class, PortalSavedData.DATA_ID);
        if(data == null)
        {
            data = new PortalSavedData();
            world.setData(PortalSavedData.DATA_ID, data);
            data.markDirty();
        }
        return data;
    }
}
