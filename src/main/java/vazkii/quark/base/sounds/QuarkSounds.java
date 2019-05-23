/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [May 15, 2019, 00:50 AM (EST)]
 */
package vazkii.quark.base.sounds;

import net.minecraft.util.SoundEvent;
import vazkii.arl.util.ProxyRegistry;

public class QuarkSounds {
	public static final SoundEvent ITEM_SOUL_POWDER_SPAWN = new ModSoundEvent("item.soul_powder.spawn");
	public static final SoundEvent BLOCK_PIPE_SHOOT = new ModSoundEvent("block.pipe.shoot");
	public static final SoundEvent BLOCK_PIPE_PICKUP = new ModSoundEvent("block.pipe.pickup");
	public static final SoundEvent BLOCK_PIPE_SHOOT_LENNY = new ModSoundEvent("block.pipe.shoot.lenny");
	public static final SoundEvent BLOCK_PIPE_PICKUP_LENNY = new ModSoundEvent("block.pipe.pickup.lenny");
	public static final SoundEvent BLOCK_SPONGE_HISS = new ModSoundEvent("block.sponge.hiss");
	public static final SoundEvent ENTITY_BOAT_ADD_ITEM = new ModSoundEvent("entity.boat.add_item");
	public static final SoundEvent ITEM_SOUL_BEAD_CURSE = new ModSoundEvent("item.soul_bead.curse");
	public static final SoundEvent BLOCK_MONSTER_BOX_GROWL = new ModSoundEvent("block.monster_box.growl");
	public static final SoundEvent ENTITY_STONELING_MEEP = new ModSoundEvent("entity.stoneling.meep");
	public static final SoundEvent ENTITY_STONELING_PURR = new ModSoundEvent("entity.stoneling.purr");
	public static final SoundEvent ENTITY_STONELING_GIVE = new ModSoundEvent("entity.stoneling.give");
	public static final SoundEvent ENTITY_STONELING_TAKE = new ModSoundEvent("entity.stoneling.take");
	public static final SoundEvent ENTITY_STONELING_EAT = new ModSoundEvent("entity.stoneling.eat");
	public static final SoundEvent ENTITY_FROG_WEDNESDAY = new ModSoundEvent("entity.frog.wednesday");

	public static void init() {
		for (ModSoundEvent event : ModSoundEvent.allEvents)
			ProxyRegistry.register(event);
	}
}
