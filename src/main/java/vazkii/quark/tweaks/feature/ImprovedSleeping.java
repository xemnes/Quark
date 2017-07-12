package vazkii.quark.tweaks.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.network.message.MessageUpdateAfk;

public class ImprovedSleeping extends Feature {

	private int timeSinceKeystroke;
	private static List<String> sleepingPlayers = new ArrayList();

	private static boolean enableAfk;
	private static int afkTime, percentReq;
	
	private static String TAG_AFK = "quark:afk";

	@Override
	public void setupConfig() {
		enableAfk = loadPropBool("Enable AFK", "", true);
		afkTime = loadPropInt("Time for AFK", "How many ticks are required for a player to be marked AFK", 2 * 1200);
		percentReq = loadPropInt("Required Percentage", "The percentage of the (non-afk) server that needs to be sleeping for the time to change.", 100);
	}

	public static void updateAfk(EntityPlayer player, boolean afk) {
		if(!enableAfk)
			return;

		if(player.world.playerEntities.size() != 1) {
			if(afk) {
				player.getEntityData().setBoolean(TAG_AFK, true);
				TextComponentTranslation text = new TextComponentTranslation("quarkmisc.nowAfk");
				text.getStyle().setColor(TextFormatting.AQUA);
				player.sendMessage(text);
			} else {
				player.getEntityData().setBoolean(TAG_AFK, false);
				TextComponentTranslation text = new TextComponentTranslation("quarkmisc.leftAfk");
				text.getStyle().setColor(TextFormatting.AQUA);
				player.sendMessage(text);
			}
		}
	}

	public static boolean isEveryoneAsleep(World world) {
		if(!ModuleLoader.isFeatureEnabled(ImprovedSleeping.class))
			return vanillaCheck(world);

		Pair<Integer, Integer> counts = getPlayerCounts(world);
		int legitPlayers = counts.getLeft();
		int sleepingPlayers = counts.getRight();

		return legitPlayers > 0 && ((float) sleepingPlayers / (float) legitPlayers) * 100 >= percentReq;
	}

	private static boolean vanillaCheck(World world) {
		for(EntityPlayer entityplayer : world.playerEntities) {
			if(!entityplayer.isSpectator() && !entityplayer.isPlayerFullyAsleep())
				return false;
		}

		return true;
	}

	private static boolean doesPlayerCountForSleeping(EntityPlayer player) {
		return !player.isSpectator() && !player.getEntityData().getBoolean(TAG_AFK);
	}

	private static boolean isPlayerSleeping(EntityPlayer player) {
		return player.isPlayerFullyAsleep();
	}

	private static Pair<Integer, Integer> getPlayerCounts(World world) {
		int legitPlayers = 0;
		int sleepingPlayers = 0;
		for(EntityPlayer player : world.playerEntities)
			if(doesPlayerCountForSleeping(player)) {
				legitPlayers++;

				if(isPlayerSleeping(player))
					sleepingPlayers++;
			}

		return Pair.of(legitPlayers, sleepingPlayers);
	}

	@SubscribeEvent
	public void onWorldTick(PlayerTickEvent event) {
		World world = event.player.world;
		if(world.isRemote || world.provider.getDimension() != 0 || world.playerEntities.indexOf(event.player) != 0 || event.phase != Phase.END)
			return;
		
		List<String> newSleepingPlayers = new ArrayList();
		String sleeper = "";

		for(EntityPlayer player : world.playerEntities)
			if(doesPlayerCountForSleeping(player) && isPlayerSleeping(player)) {
				String name = player.getName();
				if(!sleepingPlayers.contains(name))
					sleeper = name;
				newSleepingPlayers.add(name);
			}
		sleepingPlayers = newSleepingPlayers;

		if(!sleeper.isEmpty() && world.playerEntities.size() != 1) {
			Pair<Integer, Integer> counts = getPlayerCounts(world);
			int legitPlayers = counts.getLeft();
			int sleepingPlayers = counts.getRight();
			int requiredPlayers = Math.max((int) Math.ceil(((float) legitPlayers * (float) percentReq / 100F)), 0);

			TextComponentTranslation text = new TextComponentTranslation("quarkmisc.personSleeping", sleeper, sleepingPlayers, requiredPlayers);
			text.getStyle().setColor(TextFormatting.GOLD);
			for(EntityPlayer player : world.playerEntities)
				player.sendMessage(text);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onClientTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			timeSinceKeystroke++;

			if(timeSinceKeystroke == afkTime)
				NetworkHandler.INSTANCE.sendToServer(new MessageUpdateAfk(true));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeystroke(KeyInputEvent event) {
		registerPress();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeystroke(GuiScreenEvent.KeyboardInputEvent.Pre event) {
		registerPress();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onMousePress(MouseInputEvent event) {
		if(Mouse.getEventButtonState() && Minecraft.getMinecraft().currentScreen != null)
			registerPress();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onMousePress(GuiScreenEvent.MouseInputEvent.Pre event) {
		if(Mouse.getEventButtonState() && Minecraft.getMinecraft().currentScreen != null)
			registerPress();
	}

	private void registerPress() {
		if(timeSinceKeystroke >= afkTime)
			NetworkHandler.INSTANCE.sendToServer(new MessageUpdateAfk(false));
		timeSinceKeystroke = 0;
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
	@Override
	public String[] getIncompatibleMods() {
		return new String[] { "morpheus" };
	}

}
