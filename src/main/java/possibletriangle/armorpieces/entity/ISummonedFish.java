package possibletriangle.armorpieces.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ISummonedFish {

    int MAX_DISTANCE = 8;
    Function<Class<? extends AbstractFishEntity>,DataParameter<Optional<UUID>>> OWNER = clazz -> EntityDataManager.createKey(clazz, DataSerializers.OPTIONAL_UNIQUE_ID);

    Optional<LivingEntity> findOwner();

    PathNavigator getNavigator();

    default boolean isOwner(Entity entity) {
        return this.findOwner()
                .filter(o -> o.getUniqueID()
                        .equals(entity.getUniqueID())).isPresent();
    }

}
