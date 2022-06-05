package wtf.mania.util;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import optifine.Reflector;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import wtf.mania.MCHook;

public class RayTraceUtils implements MCHook {
	
	public static RayTraceResult raytraceBlock(float[] rotations, double range) {
		Vec3d Vec3dd = new Vec3d(mc.player.posX, mc.player.boundingBox.minY + (double) mc.player.getEyeHeight(), mc.player.posZ);
        Vec3d Vec3dd1 = mc.player.getVectorForRotation(rotations[1], rotations[0]);
        Vec3d Vec3dd2 = Vec3dd.addVector(Vec3dd1.xCoord * range, Vec3dd1.yCoord * range, Vec3dd1.zCoord * range);
        return mc.world.rayTraceBlocks(Vec3dd, Vec3dd2, false);
	}
	
	public static Entity raytraceEntity(float[] rotations, double range)
    {
        double d1 = range;
        double d2 = d1;
        Vec3d Vec3d1 = mc.player.getPositionEyes(1.0F);
        boolean bool1 = false;
        boolean bool2 = true;
        if (d1 > 3.0D)
            bool1 = true;
        Vec3d Vec3d2 = mc.player.getVectorForRotation(rotations[1], rotations[0]);
        Vec3d Vec3d3 = Vec3d1.addVector(Vec3d2.xCoord * d1, Vec3d2.yCoord * d1, Vec3d2.zCoord * d1);
        Entity entity = null;
        Vec3d Vec3d4 = null;
        float f = 1.0F;
        List<Entity> list = mc.world.getEntitiesInAABBexcluding(mc.getRenderViewEntity(), mc.getRenderViewEntity().getEntityBoundingBox().addCoord(Vec3d2.xCoord * d1, Vec3d2.yCoord * d1, Vec3d2.zCoord * d1).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d3 = d2;
        for (byte b = 0; b < list.size(); b++) {
            if (list.get(b) instanceof EntityLivingBase) {
            	final EntityLivingBase entity1 = (EntityLivingBase) list.get(b);
            	 if (!entity1.isEntityAlive()) continue;
                 float f1 = entity1.getCollisionBorderSize();
                 AxisAlignedBB axisAlignedBB = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                 RayTraceResult movingObjectPosition = axisAlignedBB.calculateIntercept(Vec3d1, Vec3d3);
                 if (axisAlignedBB.isVecInside(Vec3d1)) {
                     if (d3 >= 0.0D) {
                         entity = entity1;
                         Vec3d4 = (movingObjectPosition == null) ? Vec3d1 : movingObjectPosition.hitVec;
                         d3 = 0.0D;
                     }
                 } else if (movingObjectPosition != null) {
                     double d = Vec3d1.distanceTo(movingObjectPosition.hitVec);
                     if (d < f1 * 2) {
                    	 return entity1;
                     
                     }
                     if (d < d3 || d3 == 0.0D) {
                         boolean bool = false;
                         if (Reflector.ForgeEntity_canRiderInteract.exists())
                             bool = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                         if (entity1 == (mc.getRenderViewEntity()).ridingEntity && !bool) {
                             if (d3 == 0.0D) {
                                 entity = entity1;
                                 Vec3d4 = movingObjectPosition.hitVec;
                             }
                         } else {
                             entity = entity1;
                             Vec3d4 = movingObjectPosition.hitVec;
                             d3 = d;
                         }
                     }
                 }
            }
        }
        return entity;
    }


}
