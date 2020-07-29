package possibletriangle.armorpieces.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.world.World;
import possibletriangle.armorpieces.Content;

import java.util.Optional;
import java.util.UUID;

public class TropicalFishSummoned extends TropicalFishEntity implements ISummonedFish {

    private static final DataParameter<Optional<UUID>> OWNER = ISummonedFish.OWNER.apply(TropicalFishSummoned.class);

    @SuppressWarnings("unused")
    public TropicalFishSummoned(World world) {
        this(Content.TROPICAL_FISH.get(), world);
    }

    public TropicalFishSummoned(EntityType<TropicalFishEntity> type, World world) {
        super(type, world);
    }

    public TropicalFishSummoned(LivingEntity owner) {
        this(Content.TROPICAL_FISH.get(), owner.world);
        this.dataManager.set(OWNER, Optional.of(owner.getUniqueID()));

        int i, j, k, l;
        if ((double)this.rand.nextFloat() < 0.9D) {
            int i1 = TropicalFishEntity.SPECIAL_VARIANTS[this.rand.nextInt(TropicalFishEntity.SPECIAL_VARIANTS.length)];
            i = i1 & 255;
            j = (i1 & '\uff00') >> 8;
            k = (i1 & 16711680) >> 16;
            l = (i1 & -16777216) >> 24;
        } else {
            i = this.rand.nextInt(2);
            j = this.rand.nextInt(6);
            k = this.rand.nextInt(15);
            l = this.rand.nextInt(15);
        }

        setVariant(i | j << 8 | k << 16 | l << 24);
    }

    @Override
    public void onCollideWithPlayer(PlayerEntity player) {
        if(!isOwner(player)) super.onCollideWithPlayer(player);
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        return isOwner(entity);
    }

    @Override
    protected void collideWithEntity(Entity entity) {
        if(!isOwner(entity)) super.collideWithEntity(entity);
    }

    @Override
    protected ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        return ActionResultType.PASS;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 40));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this));
    }

    public Optional<LivingEntity> findOwner() {
        return this.getDataManager().get(OWNER).map(uuid -> world.getPlayerByUuid(uuid));
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if(!findOwner().map(this::getDistance).filter(i -> i < MAX_DISTANCE).isPresent()) this.remove();
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER, Optional.empty());
    }

}
