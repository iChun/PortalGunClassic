package me.ichun.mods.portalgunclassic.common.world;

import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import me.ichun.mods.portalgunclassic.common.packet.PacketPortalStatus;
import me.ichun.mods.portalgunclassic.common.portal.PortalInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

public class PortalSavedData extends WorldSavedData
{
    public static final String DATA_ID = "PortalGunClassicSaveData";

    public HashMap<Integer, HashMap<String, PortalInfo>> portalInfo = new HashMap<>();

    public PortalSavedData(String identifier)
    {
        super(identifier);
    }

    public void set(World world, boolean orange, BlockPos pos)
    {
        HashMap<String, PortalInfo> map = portalInfo.computeIfAbsent(world.provider.getDimension(), k -> new HashMap<>());
        map.put(orange ? "orange" : "blue", new PortalInfo(orange, pos));
        markDirty();
        PortalGunClassic.channel.sendToDimension(new PacketPortalStatus(map.containsKey("blue"), map.containsKey("orange")), world.provider.getDimension());
    }

    public void kill(World world, boolean orange)
    {
        HashMap<String, PortalInfo> map = portalInfo.get(world.provider.getDimension());
        if(map != null)
        {
            PortalInfo info = map.get(orange ? "orange" : "blue");
            if(info != null)
            {
                info.kill(world);
                map.remove(orange ? "orange" : "blue");
                if(map.isEmpty())
                {
                    portalInfo.remove(world.provider.getDimension());
                }
                markDirty();
            }
            PortalGunClassic.channel.sendToDimension(new PacketPortalStatus(map.containsKey("blue"), map.containsKey("orange")), world.provider.getDimension());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        int count = tag.getInteger("dimCount");
        for(int i = 0; i < count; i++)
        {
            NBTTagCompound dimTag = tag.getCompoundTag("dim" + i);
            HashMap<String, PortalInfo> map = new HashMap<>();
            if(dimTag.hasKey("blue"))
            {
                map.put("blue", PortalInfo.createFromNBT((NBTTagCompound)dimTag.getTag("blue")));
            }
            if(dimTag.hasKey("orange"))
            {
                map.put("orange", PortalInfo.createFromNBT((NBTTagCompound)dimTag.getTag("orange")));
            }
            if(!map.isEmpty())
            {
                portalInfo.put(dimTag.getInteger("dim"), map);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("dimCount", portalInfo.size());
        int i = 0;
        for(Map.Entry<Integer, HashMap<String, PortalInfo>> e : portalInfo.entrySet())
        {
            NBTTagCompound dimTag = new NBTTagCompound();
            dimTag.setInteger("dim", e.getKey());
            if(e.getValue().containsKey("blue"))
            {
                dimTag.setTag("blue", e.getValue().get("blue").toNBT());
            }
            if(e.getValue().containsKey("orange"))
            {
                dimTag.setTag("orange", e.getValue().get("orange").toNBT());
            }

            tag.setTag("dim" + i, dimTag);
            i++;
        }

        return tag;
    }
}
