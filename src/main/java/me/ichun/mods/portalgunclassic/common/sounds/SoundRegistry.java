package me.ichun.mods.portalgunclassic.common.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SoundRegistry
{
    public static SoundEvent enter;
    public static SoundEvent exit;
    public static SoundEvent fizzle;
    public static SoundEvent invalid;
    public static SoundEvent openblue;
    public static SoundEvent openred;
    public static SoundEvent fireblue;
    public static SoundEvent firered;
    public static SoundEvent reset;
    public static SoundEvent active;

    public static boolean init = false;

    public static void init(IForgeRegistry<SoundEvent> registry)
    {
        if(!init)
        {
            init = true;

            enter = register(registry, "enter");
            exit = register(registry, "exit");
            fizzle = register(registry, "fizzle");
            invalid = register(registry, "invalid");
            openblue = register(registry, "openblue");
            openred = register(registry, "openred");
            fireblue = register(registry, "fireblue");
            firered = register(registry, "firered");
            reset = register(registry, "reset");
            active = register(registry, "active");
        }
    }

    private static SoundEvent register(IForgeRegistry<SoundEvent> registry, String name)
    {
        ResourceLocation rs = new ResourceLocation("portalgunclassic", name);
        SoundEvent event = new SoundEvent(rs).setRegistryName(rs);
        registry.register(event);
        return event;
    }
}
