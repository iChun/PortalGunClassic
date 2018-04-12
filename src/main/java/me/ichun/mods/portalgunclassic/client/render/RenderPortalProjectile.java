package me.ichun.mods.portalgunclassic.client.render;

import me.ichun.mods.portalgunclassic.common.entity.EntityPortalProjectile;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

public class RenderPortalProjectile extends Render<EntityPortalProjectile>
{
    public static final ResourceLocation txBlue = new ResourceLocation("portalgunclassic", "textures/entity/portalball_blue.png");
    public static final ResourceLocation txOrange = new ResourceLocation("portalgunclassic", "textures/entity/portalball_orange.png");

    protected RenderPortalProjectile(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(EntityPortalProjectile entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if(entity.age < 1)
        {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 0.15F, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(getEntityTexture(entity));

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        float f = 0F;
        float f1 = 1F;
        float f2 = 0F;
        float f3 = 1F;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        bufferbuilder.pos(-0.5D, -0.5D, 0.0D).tex((double)f, (double)f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.5D, -0.5D, 0.0D).tex((double)f1, (double)f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.5D, 0.5D, 0.0D).tex((double)f1, (double)f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(-0.5D, 0.5D, 0.0D).tex((double)f, (double)f2).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPortalProjectile entity)
    {
        return entity.isOrange() ? txOrange : txBlue;
    }

    public static class Factory implements IRenderFactory<EntityPortalProjectile>
    {
        @Override
        public Render<EntityPortalProjectile> createRenderFor(RenderManager manager)
        {
            return new RenderPortalProjectile(manager);
        }
    }
}
