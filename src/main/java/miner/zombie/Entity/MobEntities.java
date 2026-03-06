package miner.zombie.Entity;

import miner.zombie.Entity.MinerZombie.MinerZombieEntity;
import miner.zombie.Minerzombie;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MobEntities {
    public static final EntityType<MinerZombieEntity> MINER_ZOMBIE =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    Identifier.of(Minerzombie.MOD_ID, "miner_zombie"),
                    EntityType.Builder.create(
                                    MinerZombieEntity::new,
                                    SpawnGroup.CREATURE
                            )
                            .dimensions(0.6f, 1.95f)
                            .build()
            );
}
