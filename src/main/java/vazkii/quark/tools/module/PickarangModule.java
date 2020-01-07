package vazkii.quark.tools.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.tools.client.render.PickarangRenderer;
import vazkii.quark.tools.entity.PickarangEntity;
import vazkii.quark.tools.item.PickarangItem;

@LoadModule(category = ModuleCategory.TOOLS)
public class PickarangModule extends Module {
	public static EntityType<PickarangEntity> pickarangType;

	@Config(description = "How long it takes before the pickarang starts returning to the player if it doesn't hit anything.")
	public static int timeout = 20;
	@Config(description = "2 is Iron, 3 is Diamond.")
	public static int harvestLevel = 3;
	@Config(description = "Set to -1 to have the Pickarang be unbreakable.")
	public static int durability = 800;
	@Config(description = "22.5 is ender chests, 25.0 is monster boxes, 50 is obsidian. Most things are below 5.")
	public static double maxHardness = 20.0;
	@Config(description = "Set this to true to use the recipe without the Heart of Diamond, even if the Heart of Diamond is enabled.", flag = "pickarang_never_uses_heart")
	public static boolean neverUseHeartOfDiamond = false;
	@Config(description = "Set this to true to disable the short cooldown between throwing pickarangs.")
	public static boolean noCooldown = false;
	
	public static Item pickarang;

	@Override
	public void construct() {
		pickarangType = EntityType.Builder.<PickarangEntity>create(PickarangEntity::new, EntityClassification.MISC)
				.size(0.4F, 0.4F)
				.setTrackingRange(80)
				.setUpdateInterval(3)
				.setShouldReceiveVelocityUpdates(true)
				.setCustomClientFactory((spawnEntity, world) -> new PickarangEntity(pickarangType, world))
				.build("pickarang");
		RegistryHelper.register(pickarangType, "pickarang");

		Item.Properties properties = new Item.Properties()
				.maxStackSize(1)
				.group(ItemGroup.TOOLS)
				.addToolType(ToolType.PICKAXE, harvestLevel)
				.addToolType(ToolType.AXE, harvestLevel)
				.addToolType(ToolType.SHOVEL, harvestLevel);

		if (durability > 0)
			properties.maxDamage(durability);

		pickarang = new PickarangItem("pickarang", this, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		RenderingRegistry.registerEntityRenderingHandler(PickarangEntity.class, PickarangRenderer::new);
	}

    private static final ThreadLocal<PickarangEntity> ACTIVE_PICKARANG = new ThreadLocal<>();

	public static void setActivePickarang(PickarangEntity pickarang) {
		ACTIVE_PICKARANG.set(pickarang);
	}

	public static DamageSource createDamageSource(PlayerEntity player) {
		PickarangEntity pickarang = ACTIVE_PICKARANG.get();

		if (pickarang == null)
			return null;

		return new IndirectEntityDamageSource("player", pickarang, player).setProjectile();
	}


}
