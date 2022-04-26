package net.minecraft.util.math;

import com.sun.javafx.geom.Vec3d;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class RayTraceResult
{
    private BlockPos blockPos;

    /**
     * The type of hit that occured, see {@link RayTraceResult#Type} for possibilities.
     */
    public RayTraceResult.Type typeOfHit;
    public EnumFacing sideHit;

    /** The vector position of the hit */
    public Vec3 hitVec;

    /** The hit entity */
    public Entity entityHit;

    public RayTraceResult(Entity entityIn)
    {
        this(entityIn, new Vec3(entityIn.posX, entityIn.posY, entityIn.posZ));
    }

    public RayTraceResult(RayTraceResult.Type typeIn, Vec3 hitVecIn, EnumFacing sideHitIn, BlockPos blockPosIn)
    {
        this.typeOfHit = typeIn;
        this.blockPos = blockPosIn;
        this.sideHit = sideHitIn;
        this.hitVec = new Vec3(hitVecIn.xCoord, hitVecIn.yCoord, hitVecIn.zCoord);
    }

    public RayTraceResult(Entity entityHitIn, Vec3 hitVecIn)
    {
        this.typeOfHit = RayTraceResult.Type.ENTITY;
        this.entityHit = entityHitIn;
        this.hitVec = hitVecIn;
    }

    public BlockPos getBlockPos()
    {
        return this.blockPos;
    }

    public String toString()
    {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }

    public static enum Type
    {
        MISS,
        BLOCK,
        ENTITY;
    }
}
