package me.ichun.mods.portalgunclassic.common.packet;

import io.netty.buffer.ByteBuf;
import me.ichun.mods.portalgunclassic.client.portal.PortalStatus;
import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketEntityLocation implements IMessage
{
    public int id;
    public double ltX;
    public double ltY;
    public double ltZ;
    public double prevX;
    public double prevY;
    public double prevZ;
    public double posX;
    public double posY;
    public double posZ;
    public double mX;
    public double mY;
    public double mZ;
    public float prevYaw;
    public float prevPitch;
    public float yaw;
    public float pitch;

    public PacketEntityLocation()
    {}

    public PacketEntityLocation(Entity ent)
    {
        id = ent.getEntityId();
        ltX = ent.lastTickPosX;
        ltY = ent.lastTickPosY;
        ltZ = ent.lastTickPosZ;
        prevX = ent.prevPosX;
        prevY = ent.prevPosY;
        prevZ = ent.prevPosZ;
        posX = ent.posX;
        posY = ent.posY;
        posZ = ent.posZ;
        mX = ent.motionX;
        mY = ent.motionY;
        mZ = ent.motionZ;
        prevYaw = ent.prevRotationYaw;
        prevPitch = ent.prevRotationPitch;
        yaw = ent.rotationYaw;
        pitch = ent.rotationPitch;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        id = buf.readInt();
        ltX = buf.readDouble();
        ltY = buf.readDouble();
        ltZ = buf.readDouble();
        prevX = buf.readDouble();
        prevY = buf.readDouble();
        prevZ = buf.readDouble();
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();
        mX = buf.readDouble();
        mY = buf.readDouble();
        mZ = buf.readDouble();
        prevYaw = buf.readFloat();
        prevPitch = buf.readFloat();
        yaw = buf.readFloat();
        pitch = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(id);
        buf.writeDouble(ltX);
        buf.writeDouble(ltY);
        buf.writeDouble(ltZ);
        buf.writeDouble(prevX);
        buf.writeDouble(prevY);
        buf.writeDouble(prevZ);
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        buf.writeDouble(mX);
        buf.writeDouble(mY);
        buf.writeDouble(mZ);
        buf.writeFloat(prevYaw);
        buf.writeFloat(prevPitch);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
    }

    public static class Handler implements IMessageHandler<PacketEntityLocation, IMessage>
    {
        @Override
        public IMessage onMessage(PacketEntityLocation message, MessageContext ctx)
        {
            handleClient(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void handleClient(PacketEntityLocation message)
        {
            Entity ent = Minecraft.getMinecraft().world.getEntityByID(message.id);
            if(ent != null)
            {
                ent.lastTickPosX = message.ltX;
                ent.lastTickPosY = message.ltY;
                ent.lastTickPosZ = message.ltZ;
                ent.prevPosX = message.prevX;
                ent.prevPosY = message.prevY;
                ent.prevPosZ = message.prevZ;
                ent.posX = message.posX;
                ent.posY = message.posY;
                ent.posZ = message.posZ;
                ent.motionX = message.mX;
                ent.motionY = message.mY;
                ent.motionZ = message.mZ;
                ent.prevRotationYaw = message.prevYaw;
                ent.prevRotationPitch = message.prevPitch;
                ent.rotationYaw = message.yaw;
                ent.rotationPitch = message.pitch;

                if(ent == Minecraft.getMinecraft().player)
                {
                    PortalGunClassic.eventHandlerClient.justTeleported = true;
                    PortalGunClassic.eventHandlerClient.mX = ent.motionX;
                    PortalGunClassic.eventHandlerClient.mY = ent.motionY;
                    PortalGunClassic.eventHandlerClient.mZ = ent.motionZ;
                }
            }
        }
    }
}
