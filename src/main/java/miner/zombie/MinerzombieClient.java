package miner.zombie;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import miner.zombie.Entity.MobEntities;

public class MinerzombieClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(
                MobEntities.MINER_ZOMBIE,
                ZombieEntityRenderer::new
        );
    }
}
