package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.src.Reflector;
import net.minecraft.src.ReflectorField;
import net.minecraft.tileentity.TileEntitySkull;

public class ModelAdapterHeadDragon extends ModelAdapter
{
    public ModelAdapterHeadDragon()
    {
        super(TileEntitySkull.class, "head_dragon", 0.0F);
    }

    public ModelBase makeModel()
    {
        return new ModelDragonHead(0.0F);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelDragonHead))
        {
            return null;
        }
        else
        {
            ModelDragonHead modeldragonhead = (ModelDragonHead)model;

            if (modelPart.equals("head"))
            {
                ReflectorField reflectorfield1 = Reflector.getReflectorField(ModelDragonHead.class, ModelRenderer.class, 0);
                return reflectorfield1 != null && reflectorfield1.exists() ? (ModelRenderer)Reflector.getFieldValue(modeldragonhead, reflectorfield1) : null;
            }
            else if (modelPart.equals("jaw"))
            {
                ReflectorField reflectorfield = Reflector.getReflectorField(ModelDragonHead.class, ModelRenderer.class, 1);
                return reflectorfield != null && reflectorfield.exists() ? (ModelRenderer)Reflector.getFieldValue(modeldragonhead, reflectorfield) : null;
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

            ReflectorField reflectorfield = Reflector.getReflectorField(TileEntitySkullRenderer.class, ModelDragonHead.class, 0);

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
