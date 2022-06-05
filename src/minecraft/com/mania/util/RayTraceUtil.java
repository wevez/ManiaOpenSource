package com.mania.util;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import com.mania.MCHook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RayTraceUtil implements MCHook {
	
	public static RayTraceResult raytraceBlock(float[] rotations, double range) {
		final Vec3d Vec3dd = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + (double) mc.player.getEyeHeight(), mc.player.posZ);
		final Vec3d Vec3dd1 = mc.player.getLookVec();
		final Vec3d Vec3dd2 = Vec3dd.addVector(Vec3dd1.xCoord * range, Vec3dd1.yCoord * range, Vec3dd1.zCoord * range);
        return mc.world.rayTraceBlocks(Vec3dd, Vec3dd2, false);
	}
	
	public static EntityLivingBase raytraceEntity(float[] rotations, double range)
    {
		Entity pointedEntity = null;
        Vec3d eyePos = mc.player.getPositionEyes(1.0f);
        Vec3d look = mc.player.getLook(1.0f);
        Vec3d vec32 = eyePos.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);
        float f = 1.0f;
        List list = mc.world.getEntitiesInAABBexcluding(mc.player, mc.player.getEntityBoundingBox().addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
			public boolean apply(Entity p_apply_1_) {
				return p_apply_1_.canBeCollidedWith();
			}
		}));
        double d2 = range;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity) list.get(i);

            if (entity1 == mc.player)
                continue;

            float f1 = entity1.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
            RayTraceResult movingobjectposition = axisalignedbb.calculateIntercept(eyePos, vec32);

            if (axisalignedbb.isVecInside(eyePos)) {
                if (d2 >= 0.0D) {
                    pointedEntity = entity1;
                    d2 = 0.0D;
                }
            } else if (movingobjectposition != null) {
                double d3 = eyePos.distanceTo(movingobjectposition.hitVec);

                if (d3 < d2 || d2 == 0.0D) {
                    pointedEntity = entity1;
                    d2 = d3;
                }
            }
        }

        return (EntityLivingBase) pointedEntity;
    }


}
