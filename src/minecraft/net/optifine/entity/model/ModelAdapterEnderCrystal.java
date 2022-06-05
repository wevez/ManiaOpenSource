package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.src.Reflector;

public class ModelAdapterEnderCrystal extends ModelAdapter
{
    public ModelAdapterEnderCrystal()
    {
        this("end_crystal");
    }

    protected ModelAdapterEnderCrystal(String name)
    {
        super(EntityEnderCrystal.class, name, 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelEnderCrystal(0.0F, true);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelEnderCrystal))
        {
            return null;
        }
        else
        {
            ModelEnderCrystal modelendercrystal = (ModelEnderCrystal)model;
            return modelPart.equals("cube") ? (ModelRenderer)Reflector.getFieldValue(modelendercrystal, ModelEnderCrystal.class, ModelRenderer.class, 0) : (modelPart.equals("glass") ? (ModelRenderer)Reflector.getFieldValue(modelendercrystal, ModelEnderCrystal.class, ModelRenderer.class, 1) : (modelPart.equals("base") ? (ModelRenderer)Reflector.getFieldValue(modelendercrystal, ModelEnderCrystal.class, ModelRenderer.class, 2) : null));
        }
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderEnderCrystal renderendercrystal = new RenderEnderCrystal(rendermanager);

        if (!Reflector.setFieldValue(renderendercrystal, RenderEnderCrystal.class, ModelBase.class, 0, modelBase))
        {
            return null;
        }
        else
        {
            renderendercrystal.shadowSize = shadowSize;
            return renderendercrystal;
        }
    }
}
