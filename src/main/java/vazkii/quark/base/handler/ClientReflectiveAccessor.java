/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jun 13, 2019, 22:36 AM (EST)]
 */
package vazkii.quark.base.handler;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientReflectiveAccessor {
	private static final MethodHandle CHAT_DRAWN_LINES,
			CHAT_SCROLL_POS,
			SETUP_ITEM_GUI_TRANSFORM;

	static {
		try {
			Field f = ObfuscationReflectionHelper.findField(NewChatGui.class, ReflectionKeys.NewChatGui.DRAWN_CHAT_LINES);
			CHAT_DRAWN_LINES = MethodHandles.lookup().unreflectGetter(f);

			f = ObfuscationReflectionHelper.findField(NewChatGui.class, ReflectionKeys.NewChatGui.SCROLL_POS);
			CHAT_SCROLL_POS = MethodHandles.lookup().unreflectGetter(f);

			Method m = ObfuscationReflectionHelper.findMethod(ItemRenderer.class, ReflectionKeys.ItemRenderer.SETUP_GUI_TRANSFORM, Integer.TYPE, Integer.TYPE, Boolean.TYPE); // setupGuiTransform
			SETUP_ITEM_GUI_TRANSFORM = MethodHandles.lookup().unreflect(m);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<ChatLine> getChatDrawnLines(NewChatGui chat) {
		try {
			return (List<ChatLine>) CHAT_DRAWN_LINES.invokeExact(chat);
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	public static int getScrollPos(NewChatGui chat) {
		try {
			return (int) CHAT_SCROLL_POS.invokeExact(chat);
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	public static void setupGuiTransform(ItemRenderer render, int x, int y, boolean isGui3d) {
		try {
			SETUP_ITEM_GUI_TRANSFORM.invokeExact(render, x, y, isGui3d);
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}
}
