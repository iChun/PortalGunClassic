package me.ichun.mods.portalgunclassic.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemPortalCore extends Item
{
    public ItemPortalCore()
    {
        setRegistryName(new ResourceLocation("portalgunclassic", "portal_core"));
        setUnlocalizedName("portalgunclassic.item.portal_core");
        setCreativeTab(CreativeTabs.MISC);
    }
}
