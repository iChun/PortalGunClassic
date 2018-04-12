package me.ichun.mods.portalgunclassic.client.core;

import me.ichun.mods.portalgunclassic.client.portal.PortalStatus;
import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import me.ichun.mods.portalgunclassic.common.packet.PacketSwapType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

public class EventHandlerClient
{
    public KeyBinding keySwitch = new KeyBinding("key.portalgunclassic.switch", Keyboard.KEY_G, "key.categories.portalgun");
    public KeyBinding keyReset = new KeyBinding("key.portalgunclassic.reset", Keyboard.KEY_R, "key.categories.portalgun");

    public boolean keySwitchDown = false;
    public boolean keyResetDown = false;

    public PortalStatus status = null;

    @SubscribeEvent
    public void onModelRegistry(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(PortalGunClassic.itemPortalGun, 0, new ModelResourceLocation("portalgunclassic:pg_blue", "inventory"));
        ModelLoader.setCustomModelResourceLocation(PortalGunClassic.itemPortalGun, 1, new ModelResourceLocation("portalgunclassic:pg_orange", "inventory"));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            Minecraft mc = Minecraft.getMinecraft();
            if(mc.player != null && (mc.player.getHeldItemMainhand().getItem() == PortalGunClassic.itemPortalGun || mc.player.getHeldItemOffhand().getItem() == PortalGunClassic.itemPortalGun))
            {
                if(!keySwitchDown && keySwitch.isKeyDown())
                {
                    PortalGunClassic.channel.sendToServer(new PacketSwapType(false, 0));
                }
                if(!keyResetDown && keyReset.isKeyDown())
                {
                    PortalGunClassic.channel.sendToServer(new PacketSwapType(true, GuiScreen.isShiftKeyDown() ? 1 : 0));
                }
                keySwitchDown = keySwitch.isKeyDown();
                keyResetDown = keyReset.isKeyDown();
            }
        }
    }

    @SubscribeEvent
    public void onConnectToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        status = null;
    }
}
