package me.ichun.mods.portalgunclassic.common.world;

import me.ichun.mods.portalgunclassic.common.portal.PortalInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;

public class PortalSavedData extends WorldSavedData
{
    public static final String DATA_ID = "PORTAL_GUN_CLASSIC_SAVE_DATA";

    public HashMap<String, PortalInfo> portalInfo = new HashMap<>();

    public PortalSavedData()
    {
        super("PortalGunClassic");
    }

    public void set(boolean orange, BlockPos pos)
    {
        portalInfo.put(orange ? "orange" : "blue", new PortalInfo(orange, pos));
        markDirty();
    }

    public void kill(World world, boolean orange)
    {
        PortalInfo info = portalInfo.get(orange ? "orange" : "blue");
        if(info != null)
        {
            info.kill(world);
            portalInfo.remove(orange ? "orange" : "blue");
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        if(tag.hasKey("blue"))
        {
            portalInfo.put("blue", PortalInfo.createFromNBT((NBTTagCompound)tag.getTag("blue")));
        }
        if(tag.hasKey("orange"))
        {
            portalInfo.put("orange", PortalInfo.createFromNBT((NBTTagCompound)tag.getTag("orange")));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        if(portalInfo.containsKey("blue"))
        {
            tag.setTag("blue", portalInfo.get("blue").toNBT());
        }
        if(portalInfo.containsKey("orange"))
        {
            tag.setTag("orange", portalInfo.get("orange").toNBT());
        }
        return tag;
    }
}
