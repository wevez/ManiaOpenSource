package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.src.Reflector;

public class ModelAdapterMinecart extends ModelAdapter
{
    public ModelAdapterMinecart()
    {
        super(EntityMinecart.class, "minecart", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelMinecart();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelMinecart))
        {
            return null;
        }
        else
        {
            ModelMinecart modelminecart = (ModelMinecart)model;
            return modelPart.equals("minecart") ? modelminecart.sideModels[0] : null;
        }
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderMinecart renderminecart = new RenderMinecart(rendermanager);

        if (!Reflector.setFieldValue(renderminecart, RenderMinecart.class, ModelBase.class, 0, modelBase))
        {
            return null;
        }
        else
        {
            renderminecart.shadowSize = shadowSize;
            return renderminecart;
        }
    }
}
