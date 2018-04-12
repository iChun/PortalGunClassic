package me.ichun.mods.portalgunclassic.common.packet;

import io.netty.buffer.ByteBuf;
import me.ichun.mods.portalgunclassic.client.portal.PortalStatus;
import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPortalStatus implements IMessage
{
    public boolean blue;
    public boolean orange;

    public PacketPortalStatus()
    {}

    public PacketPortalStatus(boolean blue, boolean orange)
    {
        this.blue = blue;
        this.orange = orange;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        blue = buf.readBoolean();
        orange = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(blue);
        buf.writeBoolean(orange);
    }

    public static class Handler implements IMessageHandler
    {
        @Override
        public IMessage onMessage(IMessage message, MessageContext ctx)
        {
            PortalGunClassic.eventHandlerClient.status = new PortalStatus(((PacketPortalStatus)message).blue, ((PacketPortalStatus)message).orange);
            return null;
        }
    }
}
