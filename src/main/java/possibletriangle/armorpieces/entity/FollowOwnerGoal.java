package possibletriangle.armorpieces.entity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

public class FollowOwnerGoal extends Goal {

    private final ISummonedFish fish;
    public FollowOwnerGoal(ISummonedFish fish) {
        this.fish = fish;
    }

    @Override
    public boolean shouldExecute() {
        return fish.findOwner().isPresent();
    }

    @Override
    public void tick() {
        fish.findOwner().ifPresent(owner -> fish.getNavigator().tryMoveToEntityLiving(owner, 1.0D));
    }
}
