package vazkii.quark.experimental.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.EntitySpawnHandler;
import vazkii.quark.base.world.config.BiomeTypeConfig;
import vazkii.quark.base.world.config.EntitySpawnConfig;
import vazkii.quark.experimental.client.render.ToretoiseRenderer;
import vazkii.quark.experimental.entity.ToretoiseEntity;

@LoadModule(category = ModuleCategory.EXPERIMENTAL, enabledByDefault = false)
public class ToretoiseModule extends Module {

	public static EntityType<ToretoiseEntity> toretoiseType;
	
	@Override
	public void construct() {
		toretoiseType = EntityType.Builder.<ToretoiseEntity>create(ToretoiseEntity::new, EntityClassification.CREATURE)
				.size(2F, 1.1F)
				.setTrackingRange(80)
				.setUpdateInterval(3)
				.setShouldReceiveVelocityUpdates(true)
				.setCustomClientFactory((spawnEntity, world) -> new ToretoiseEntity(toretoiseType, world))
				.build("toretoise");
		
		RegistryHelper.register(toretoiseType, "toretoise");
		EntitySpawnHandler.addEgg(toretoiseType, 0x55413b, 0x383237, new EntitySpawnConfig(0, 0, 0, new BiomeTypeConfig(true, new String[0])));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		RenderingRegistry.registerEntityRenderingHandler(toretoiseType, ToretoiseRenderer::new);
	}
	
}
