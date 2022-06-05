package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.src.Reflector;

public class ModelAdapterDragon extends ModelAdapter
{
    public ModelAdapterDragon()
    {
        super(EntityDragon.class, "dragon", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelDragon(0.0F);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelDragon))
        {
            return null;
        }
        else
        {
            ModelDragon modeldragon = (ModelDragon)model;
            return modelPart.equals("head") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 0) : (modelPart.equals("spine") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 1) : (modelPart.equals("jaw") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 2) : (modelPart.equals("body") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 3) : (modelPart.equals("rear_leg") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 4) : (modelPart.equals("front_leg") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 5) : (modelPart.equals("rear_leg_tip") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 6) : (modelPart.equals("front_leg_tip") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 7) : (modelPart.equals("rear_foot") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 8) : (modelPart.equals("front_foot") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 9) : (modelPart.equals("wing") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 10) : (modelPart.equals("wing_tip") ? (ModelRenderer)Reflector.getFieldValue(modeldragon, ModelDragon.class, ModelRenderer.class, 11) : null)))))))))));
        }
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderDragon renderdragon = new RenderDragon(rendermanager);
        renderdragon.mainModel = modelBase;

        if (!Reflector.setFieldValue(renderdragon, RenderDragon.class, ModelDragon.class, modelBase))
        {
            return null;
        }
        else
        {
            renderdragon.shadowSize = shadowSize;
            return renderdragon;
        }
    }
}
