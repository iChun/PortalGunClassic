package me.ichun.mods.portalgunclassic.common.packet;

import io.netty.buffer.ByteBuf;
import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwapType implements IMessage
{
    public boolean reset;
    public int type;

    public PacketSwapType()
    {}

    public PacketSwapType(boolean reset, int type)
    {
        this.reset = reset;
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        reset = buf.readBoolean();
        type = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(reset);
        buf.writeInt(type);
    }

    public static class Handler implements IMessageHandler
    {
        @Override
        public IMessage onMessage(IMessage message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if(!((PacketSwapType)message).reset)
            {
                for(EnumHand hand : EnumHand.values())
                {
                    ItemStack is = player.getHeldItem(hand);
                    if(is.getItem() == PortalGunClassic.itemPortalGun)
                    {
                        is.setItemDamage(is.getItemDamage() == 1 ? 0 : 1);
                    }
                }
            }
            else
            {
                PacketSwapType pkt = (PacketSwapType)message;
                if(pkt.type == 0)
                {
                    PortalGunClassic.eventHandlerServer.getSaveData(player.world).kill(player.world, false);
                    PortalGunClassic.eventHandlerServer.getSaveData(player.world).kill(player.world, true);
                }
                else
                {
                    for(EnumHand hand : EnumHand.values())
                    {
                        ItemStack is = player.getHeldItem(hand);
                        if(is.getItem() == PortalGunClassic.itemPortalGun)
                        {
                            PortalGunClassic.eventHandlerServer.getSaveData(player.world).kill(player.world, is.getItemDamage() == 1);
                        }
                    }
                }
            }
            return null;
        }
    }
}
