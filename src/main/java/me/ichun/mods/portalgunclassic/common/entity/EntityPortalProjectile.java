package me.ichun.mods.portalgunclassic.common.entity;

import me.ichun.mods.portalgunclassic.common.PortalGunClassic;
import me.ichun.mods.portalgunclassic.common.block.BlockPortal;
import me.ichun.mods.portalgunclassic.common.sounds.SoundRegistry;
import me.ichun.mods.portalgunclassic.common.tileentity.TileEntityPortal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPortalProjectile extends Entity
{
    private static final DataParameter<Boolean> ORANGE = EntityDataManager.createKey(EntityPortalProjectile.class, DataSerializers.BOOLEAN);
    public int age = 0;

    public EntityPortalProjectile(World worldIn)
    {
        super(worldIn);
        setSize(0.3F, 0.3F);
        setEntityInvulnerable(true);
    }

    public EntityPortalProjectile(World world, Entity entity, boolean isOrange)
    {
        this(world);
        this.dataManager.set(ORANGE, isOrange);
        shoot(entity, 4.999F);
        setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight() - (width / 2F), entity.posZ, entity.rotationYaw, entity.rotationPitch);
    }

    public void setOrange(boolean flag)
    {
        dataManager.set(ORANGE, flag);
    }

    public boolean isOrange()
    {
        return dataManager.get(ORANGE);
    }

    public void shoot(Entity entity, float velocity)
    {
        float f = -MathHelper.sin(entity.rotationYaw * 0.017453292F) * MathHelper.cos(entity.rotationPitch * 0.017453292F);
        float f1 = -MathHelper.sin((entity.rotationPitch) * 0.017453292F);
        float f2 = MathHelper.cos(entity.rotationYaw * 0.017453292F) * MathHelper.cos(entity.rotationPitch * 0.017453292F);
        this.shoot((double)f, (double)f1, (double)f2, velocity);
        this.motionX += entity.motionX;
        this.motionZ += entity.motionZ;

        if (!entity.onGround)
        {
            this.motionY += entity.motionY;
        }
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void shoot(double x, double y, double z, float velocity)
    {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double)f;
        y = y / (double)f;
        z = z / (double)f;
        x = x * (double)velocity;
        y = y * (double)velocity;
        z = z * (double)velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(y, (double)f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    @Override
    public void entityInit()
    {
        this.dataManager.register(ORANGE, false);
    }

    @Override
    public void onUpdate()
    {
        if(posY > world.getHeight() * 2 || posY < -world.getHeight() * 2 || age > 1200) //a minute
        {
            setDead();
            return;
        }

        age++;

        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;

        super.onUpdate();

        Vec3d vec31 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec32 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int i = MathHelper.floor(vec32.x);
                int j = MathHelper.floor(vec32.y);
                int k = MathHelper.floor(vec32.z);
                int l = MathHelper.floor(vec31.x);
                int i1 = MathHelper.floor(vec31.y);
                int j1 = MathHelper.floor(vec31.z);
                BlockPos blockpos;

                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z) || (l == i && i1 == j && j1 == k))
                    {
                        break;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    l = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = world.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if (iblockstate1.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB)
                    {
                        if (block1.canCollideCheck(iblockstate1, true))
                        {
                            RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(world, blockpos, vec31, vec32);
                            if (raytraceresult1 != null)
                            {
                                if(block1 == Blocks.IRON_BARS)
                                {
                                    vec31 = new Vec3d(raytraceresult1.hitVec.x + (motionX / 5000D), raytraceresult1.hitVec.y + (motionY / 5000D), raytraceresult1.hitVec.z + (motionZ / 5000D));
                                }
                                else
                                {
                                    createPortal(raytraceresult1);
                                    setDead();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

        for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void createPortal(RayTraceResult rayTraceResult)
    {
        if(!world.isRemote)
        {
            BlockPos pos = rayTraceResult.getBlockPos().offset(rayTraceResult.sideHit);
            if(BlockPortal.canPlace(world, pos, rayTraceResult.sideHit, isOrange()))
            {
                PortalGunClassic.eventHandlerServer.getSaveData(world).kill(world, isOrange());

                world.setBlockState(pos, PortalGunClassic.blockPortalGun.getDefaultState());
                TileEntity te = world.getTileEntity(pos);
                if(te instanceof TileEntityPortal)
                {
                    ((TileEntityPortal)te).setup(rayTraceResult.sideHit.getAxis() != EnumFacing.Axis.Y, isOrange(), rayTraceResult.sideHit);
                }
                if(rayTraceResult.sideHit.getAxis() != EnumFacing.Axis.Y)
                {
                    world.setBlockState(pos.down(), PortalGunClassic.blockPortalGun.getDefaultState());
                    te = world.getTileEntity(pos.down());
                    if(te instanceof TileEntityPortal)
                    {
                        ((TileEntityPortal)te).setup(false, isOrange(), rayTraceResult.sideHit);
                    }
                }
                PortalGunClassic.eventHandlerServer.getSaveData(world).set(isOrange(), rayTraceResult.sideHit.getAxis() != EnumFacing.Axis.Y ? pos.down() : pos);

                world.playSound(null, this.posX, this.posY + (this.height / 2F), this.posZ, isOrange() ? SoundRegistry.openred : SoundRegistry.openblue, SoundCategory.BLOCKS, 0.3F, 1.0F);
            }
            else
            {
                world.playSound(null, this.posX, this.posY + (this.height / 2F), this.posZ, SoundRegistry.invalid, SoundCategory.NEUTRAL, 0.5F, 1.0F);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10D;

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        setOrange(tag.getBoolean("orange"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setBoolean("orange", isOrange());
    }
}
