package possibletriangle.armorpieces.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import possibletriangle.armorpieces.Content;

import java.util.Optional;
import java.util.UUID;

public class PufferfishSummoned extends PufferfishEntity implements ISummonedFish {

    private static final DataParameter<Optional<UUID>> OWNER = ISummonedFish.OWNER.apply(PufferfishSummoned.class);

    @SuppressWarnings("unused")
    public PufferfishSummoned(World world) {
        this(Content.PUFFERFISH.get(), world);
    }

    public PufferfishSummoned(EntityType<PufferfishEntity> type, World world) {
        super(type, world);
    }

    public PufferfishSummoned(LivingEntity owner) {
        this(Content.PUFFERFISH.get(), owner.world);
        this.dataManager.set(OWNER, Optional.of(owner.getUniqueID()));
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
        if (!findOwner().map(this::getDistance).filter(i -> i < MAX_DISTANCE).isPresent()) this.remove();
    }

    @Override
    public void onCollideWithPlayer(PlayerEntity player) {
        if (!isOwner(player)) super.onCollideWithPlayer(player);
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        return isOwner(entity);
    }

    @Override
    protected void collideWithEntity(Entity entity) {
        if (!isOwner(entity)) super.collideWithEntity(entity);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER, Optional.empty());
    }

}
