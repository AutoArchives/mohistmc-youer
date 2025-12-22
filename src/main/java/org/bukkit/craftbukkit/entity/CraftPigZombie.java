package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.zombie.EntityPigZombie;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {

    public CraftPigZombie(CraftServer server, EntityPigZombie entity) {
        super(server, entity);
    }

    @Override
    public int getAnger() {
        int remainingAnger = (int) (getHandle().getPersistentAngerEndTime() - getWorld().getGameTime());
        return (remainingAnger > 0) ? remainingAnger : 0;
    }

    @Override
    public void setAnger(int level) {
        getHandle().setTimeToRemainAngry(level);
    }

    @Override
    public void setAngry(boolean angry) {
        setAnger(angry ? 400 : 0);
    }

    @Override
    public boolean isAngry() {
        return getAnger() > 0;
    }

    @Override
    public EntityPigZombie getHandle() {
        return (EntityPigZombie) entity;
    }

    @Override
    public String toString() {
        return "CraftPigZombie";
    }

    @Override
    public boolean isConverting() {
        return false;
    }

    @Override
    public int getConversionTime() {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }

    @Override
    public void setConversionTime(int time) {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }
}
