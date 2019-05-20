/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [May 20, 2019, 10:35 AM (EST)]
 */
package vazkii.quark.base.handler;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.Quark;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class OverrideRegistryHandler {

	private static void crackFinalField(Field field) throws NoSuchFieldException, IllegalAccessException {
		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
	}

	private static Level revokeLog() {
		Level prior = FMLLog.log.getLevel();
		if (FMLLog.log instanceof Logger)
			((Logger) FMLLog.log).setLevel(Level.OFF);
		return prior;
	}

	private static void restoreLog(Level level) {
		if (FMLLog.log instanceof Logger)
			((Logger) FMLLog.log).setLevel(level);
	}

	public static void registerBlock(Block block, String baseName) {
		Level revoked = revokeLog();
		ResourceLocation regName = new ResourceLocation("minecraft", baseName);
		block.setRegistryName(regName);
		restoreLog(revoked);

		ProxyRegistry.register(block);

		for (Field declared : Blocks.class.getDeclaredFields()) {
			if (Modifier.isStatic(declared.getModifiers()) && declared.getType().isAssignableFrom(block.getClass())) {
				try {
					Block blockInField = (Block) declared.get(null);
					if (regName.equals(blockInField.getRegistryName())) {
						crackFinalField(declared);
						declared.set(null, block);
					}
				} catch (IllegalAccessException | NoSuchFieldException e) {
					Quark.LOG.warn("Was unable to replace registry entry for " + regName + ", may cause issues", e);
				}
			}
		}
	}


}
