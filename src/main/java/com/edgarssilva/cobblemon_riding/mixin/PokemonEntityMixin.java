package com.edgarssilva.cobblemon_riding.mixin;

import com.cobblemon.mod.common.entity.EntityProperty;
import com.cobblemon.mod.common.entity.PoseType;
import com.cobblemon.mod.common.entity.pokemon.PokemonBehaviourFlag;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.edgarssilva.cobblemon_riding.RideablePokemon;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonEntity.class)
public abstract class PokemonEntityMixin extends ShoulderRidingEntity implements RideablePokemon {
    @Final
    @Shadow
    private EntityProperty<PoseType> poseType;

    @Shadow
    private Pokemon pokemon;

    @Shadow
    public abstract void travel(@NotNull Vec3 movementInput);

    @Shadow
    public abstract boolean getBehaviourFlag(@NotNull PokemonBehaviourFlag flag);

    @Shadow
    public abstract void setBehaviourFlag(@NotNull PokemonBehaviourFlag flag, boolean on);

    @Unique
    private float cobblemon_riding$pendingJump = 0;

    @Unique
    private boolean cobblemon_riding$isJumping = false;

    @Unique
    private boolean cobblemon_riding$isFlying = false;

    @Unique
    private double cobblemon_riding$leftGroundTicks = 0;

    @Unique
    private static final EntityDataAccessor<Boolean> cobblemon_riding$canFly = SynchedEntityData.defineId(PokemonEntity.class, EntityDataSerializers.BOOLEAN);

    @Unique
    private boolean cobblemon_riding$holdUp = false;

    @Unique
    private boolean cobblemon_riding$flyUp = false;

    @Unique
    private boolean cobblemon_riding$flyDown = false;

    @Unique
    private boolean cobblemon_riding$sprint = false;

    protected PokemonEntityMixin(EntityType<? extends ShoulderRidingEntity> p_29893_, Level p_29894_) {
        super(p_29893_, p_29894_);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        setNoAi(this.getControllingPassenger() != null);
        /*if (this.getControllingPassenger() instanceof Player) {
            super.tick();
            return;
        }*/
    }

    @Inject(method = "travel", at = @At("HEAD"))
    public void travel(Vec3 direction, CallbackInfo ci) {
        if (this.getControllingPassenger() instanceof Player player) {
            setNoAi(true);
            setNoGravity(false);
            cobblemon_riding$updateLeftGround();

            //Update rotation to the players rotation
            this.setYRot(player.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(player.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.yBodyRot;

            float motionX = player.xxa * 0.5F;
            float motionZ = player.zza;

            if (motionZ <= 0.0F) {
                motionZ *= 0.25F;
            }

            //Check for a jump
            if (cobblemon_riding$pendingJump > 0f && !this.cobblemon_riding$isJumping && isOnGround()) {
                double jump = getJumpPower() * cobblemon_riding$pendingJump * (double) this.getBlockJumpFactor();
                jump += this.getJumpBoostPower();

                if (cobblemon_riding$canFly()) jump *= 5f;
                else jump *= 2f;

                Vec3 delta = this.getDeltaMovement();

                this.setDeltaMovement(delta.x, jump, delta.z);
                cobblemon_riding$isJumping = true;
                this.hasImpulse = true;

               /* if (motionZ > 0.0F) {
                    float f2 = Mth.sin(this.getYRot() * Mth.DEG_TO_RAD);
                    float f3 = Mth.cos(this.getYRot() * Mth.DEG_TO_RAD);

                    //TODO: Only jumps forward if there is not a blcok in front of the pokemon not sure why
                    this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * f2 * pendingJump, 0.0D, 0.4F * f3 * this.pendingJump));
                }*/

                float jumpDirection = player.getYRot() * Mth.DEG_TO_RAD;
                double jumpX = -0.4F * Math.sin(jumpDirection) * cobblemon_riding$pendingJump;
                double jumpZ = 0.4F * Math.cos(jumpDirection) * cobblemon_riding$pendingJump;

                this.setPos(this.getX() + jumpX, this.getY(), this.getZ() + jumpZ);

                cobblemon_riding$pendingJump = 0;
                if (cobblemon_riding$canFly()) cobblemon_riding$isFlying = true;

            }

            //Workaround for the parent class setting zza to the speed
            float zza = this.zza;
            this.setSpeed(cobblemon_riding$sprint ? 0.30f : 0.20f);
            this.zza = zza;

            if (cobblemon_riding$canFly()) this.flyingSpeed = cobblemon_riding$sprint ? 0.075f : 0.05f;

            super.travel(new Vec3(motionX, 0, motionZ));


            if ((cobblemon_riding$canFly() && cobblemon_riding$isFlying) || this.isUnderWater()) {
                Vec3 delta = this.getDeltaMovement();
                double deltaY = delta.y;

                if (this.cobblemon_riding$flyDown) deltaY = -0.2f;
                else if (this.cobblemon_riding$holdUp) deltaY = 0.04f;
                else if (this.cobblemon_riding$flyUp) deltaY = 0.15f;
                else if (deltaY < 0) deltaY *= 0.25f; //Slow down the gravity

                this.setDeltaMovement(delta.x, deltaY, delta.z);
            }

            //Update animation
            boolean isPlayerMoving = motionX != 0 || motionZ != 0;

            //Check if we are still jumping/flying
            if (cobblemon_riding$hasLeftGround()) cobblemon_riding$isJumping = false;
            if (this.isOnGround()) {
                cobblemon_riding$pendingJump = 0;
                cobblemon_riding$isJumping = false;
                cobblemon_riding$isFlying = false;
            } else {
                resetFallDistance();
            }


            PoseType pose = poseType.get();
            if (cobblemon_riding$hasLeftGround() && cobblemon_riding$canFly()) {
                if (isPlayerMoving) pose = PoseType.FLY;
                else pose = PoseType.HOVER;
            } else {
                if (isPlayerMoving && !cobblemon_riding$isJumping) pose = PoseType.WALK;
                else pose = PoseType.STAND;
            }

            cobblemon_riding$checkedSetPose(pose);

            //Basic game checks and updates
            this.calculateEntityAnimation(this, true);
            this.tryCheckInsideBlocks();

            return; //Stop usual travel
        }

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(cobblemon_riding$canFly, false);
    }

    @Unique
    private void cobblemon_riding$updateLeftGround() {
        if (this.isOnGround()) cobblemon_riding$leftGroundTicks = 0;
        else cobblemon_riding$leftGroundTicks++;
    }

    @Unique
    private boolean cobblemon_riding$hasLeftGround() {
        return cobblemon_riding$leftGroundTicks > 60 || cobblemon_riding$isFlying;
    }

    @Override
    public void cobblemon_riding$doPlayerRide(Entity entity) {
        if (entity == null) return;
        if (!(entity instanceof Player player)) return;


        if (!player.level.isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
            this.entityData.set(cobblemon_riding$canFly, pokemon.getForm().getBehaviour().getMoving().getFly().getCanFly());
        }
    }

    @Override
    public boolean canJump() {
        return isOnGround();
    }

    @Override
    public void onPlayerJump(int amount) {

        if (amount < 0) amount = 0;

        if (amount >= 90) this.cobblemon_riding$pendingJump = 1;
        else this.cobblemon_riding$pendingJump = 0.4F + 0.4F * (float) amount / 90F;
    }

    @Override
    public void handleStartJump(int p_21695_) {
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof LivingEntity) {
            return (LivingEntity) entity;
        }
        return null;
    }

    @Unique
    private void cobblemon_riding$checkedSetPose(PoseType pose) {
        if (pose == PoseType.FLY) {
            if (!getBehaviourFlag(PokemonBehaviourFlag.FLYING)) {
                setBehaviourFlag(PokemonBehaviourFlag.FLYING, true);
            }
        } else {
            if (getBehaviourFlag(PokemonBehaviourFlag.FLYING)) {
                setBehaviourFlag(PokemonBehaviourFlag.FLYING, false);
            }
        }

        if (poseType.get() != pose) {
            poseType.set(pose);
        }
    }

    @Override
    public void cobblemon_riding$setPlayerInput(boolean up, boolean holdUp, boolean down, boolean sprint) {
        this.cobblemon_riding$flyDown = down;
        this.cobblemon_riding$flyUp = up;
        this.cobblemon_riding$holdUp = holdUp;
        this.cobblemon_riding$sprint = sprint;
    }

    @Unique
    private boolean cobblemon_riding$canFly() {
        return this.entityData.get(cobblemon_riding$canFly);
    }

    @Unique
    private float cobblemon_riding$getWalkSpeed() {
        return this.pokemon.getForm().getBehaviour().getMoving().getWalk().getWalkSpeed();
    }

    @Override
    public boolean rideableUnderWater() {
        return this.getControllingPassenger() != null;
    }

    @Override
    public boolean isAffectedByFluids() {
        return this.getControllingPassenger() == null;
    }
}
