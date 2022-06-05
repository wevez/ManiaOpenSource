package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Reflector;
import net.minecraft.src.ReflectorField;
import net.minecraft.tileentity.TileEntitySkull;

public class ModelAdapterHeadHumanoid extends ModelAdapter
{
    public ModelAdapterHeadHumanoid()
    {
        super(TileEntitySkull.class, "head_humanoid", 0.0F);
    }

    public ModelBase makeModel()
    {
        return new ModelHumanoidHead();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelHumanoidHead))
        {
            return null;
        }
        else
        {
            ModelHumanoidHead modelhumanoidhead = (ModelHumanoidHead)model;

            if (modelPart.equals("head"))
            {
                return modelhumanoidhead.skeletonHead;
            }
            else if (modelPart.equals("head2"))
            {
                ReflectorField reflectorfield = Reflector.getReflectorField(ModelHumanoidHead.class, ModelRenderer.class);
                return reflectorfield != null && reflectorfield.exists() ? (ModelRenderer)Reflector.getFieldValue(modelhumanoidhead, reflectorfield) : null;
            }
            else
            {
                return null;
            }
        }
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        TileEntityRendererDispatcher tileentityrendererdispatcher = TileEntityRendererDispatcher.instance;
        TileEntitySpecialRenderer tileentityspecialrenderer = tileentityrendererdispatcher.getSpecialRendererByClass(TileEntitySkull.class);

        if (!(tileentityspecialrenderer instanceof TileEntitySkullRenderer))
        {
            return null;
        }
        else
        {
            if (tileentityspecialrenderer.getEntityClass() == null)
            {
                tileentityspecialrenderer = new TileEntitySkullRenderer();
                tileentityspecialrenderer.setRendererDispatcher(tileentityrendererdispatcher);
            }

            ReflectorField reflectorfield = Reflector.getReflectorField(TileEntitySkullRenderer.class, ModelSkeletonHead.class, 1);

            if (reflectorfield == null)
            {
                return null;
            }
            else
            {
                reflectorfield.setValue(tileentityspecialrenderer, modelBase);
                return tileentityspecialrenderer;
            }
        }
    }
}
