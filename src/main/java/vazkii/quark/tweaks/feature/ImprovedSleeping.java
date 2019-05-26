package vazkii.quark.tweaks.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.network.message.MessageUpdateAfk;

import java.util.ArrayList;
import java.util.List;

public class ImprovedSleeping extends Feature {

	private int timeSinceKeystroke;
	private static List<String> sleepingPlayers = new ArrayList<>();

	private static boolean enableAfk;
	private static int afkTime, percentReq;
	
	private static final String TAG_AFK = "quark:afk";

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

	public static int isEveryoneAsleep(World world) {
		if(!ModuleLoader.isFeatureEnabled(ImprovedSleeping.class))
			return 0;

		Pair<Integer, Integer> counts = getPlayerCounts(world);
		int legitPlayers = counts.getLeft();
		int sleepingPlayers = counts.getRight();

		boolean everybody = (legitPlayers > 0 && ((float) sleepingPlayers / legitPlayers) * 100 >= percentReq);
		return everybody ? 2 : 1;
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
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		World world = event.world;
		MinecraftServer server = world.getMinecraftServer();

		if(event.side == Side.CLIENT ||
				world.provider.getDimension() != 0 ||
				event.phase != Phase.END ||
				server == null)
			return;
		
		List<String> sleepingPlayers = new ArrayList<>();
		List<String> newSleepingPlayers = new ArrayList<>();
		List<String> nonSleepingPlayers = new ArrayList<>();
		int legitPlayers = 0;

		for(EntityPlayer player : world.playerEntities)
			if(doesPlayerCountForSleeping(player)) {
				String name = player.getName();
				if(isPlayerSleeping(player)) {
					if(!ImprovedSleeping.sleepingPlayers.contains(name))
						newSleepingPlayers.add(name);
					sleepingPlayers.add(name);
				} else nonSleepingPlayers.add(name);
				
				legitPlayers++;
			}

		ImprovedSleeping.sleepingPlayers = sleepingPlayers;
		
		if(!newSleepingPlayers.isEmpty() && world.playerEntities.size() != 1) {
			int requiredPlayers = Math.max((int) Math.ceil((legitPlayers * percentReq / 100F)), 0);

			ITextComponent sibling = new TextComponentString("(" + sleepingPlayers.size() + "/" + requiredPlayers + ")");

			ITextComponent sleepingList = new TextComponentString("");

			for(String s : sleepingPlayers)
				sleepingList.appendSibling(new TextComponentString("\n\u2714 " + s).setStyle(new Style().setColor(TextFormatting.GREEN)));
			for(String s : nonSleepingPlayers)
				sleepingList.appendSibling(new TextComponentString("\n\u2718 " + s).setStyle(new Style().setColor(TextFormatting.RED)));

			ITextComponent hoverText = new TextComponentTranslation("quarkmisc.sleepingListHeader", sleepingList);

			for (String sleeper : newSleepingPlayers) {
				ITextComponent message = new TextComponentTranslation("quarkmisc.personSleeping", sleeper);
				message.getStyle().setColor(TextFormatting.GOLD);
				message.appendText(" ");

				message.appendSibling(sibling.createCopy());

				HoverEvent hover = new HoverEvent(Action.SHOW_TEXT, hoverText.createCopy());
				sibling.getStyle().setHoverEvent(hover);
				sibling.getStyle().setUnderlined(true);

				server.getPlayerList().sendMessage(message, true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event) {
		World logoutWorld = event.player.world;
		List<EntityPlayer> players = logoutWorld.playerEntities;
		if(players.size() == 1) {
			EntityPlayer lastPlayer = players.get(0);
			if(lastPlayer.getEntityData().getBoolean(TAG_AFK)) {
				lastPlayer.getEntityData().setBoolean(TAG_AFK, false);
				TextComponentTranslation text = new TextComponentTranslation("quarkmisc.leftAfk");
				text.getStyle().setColor(TextFormatting.AQUA);
				lastPlayer.sendMessage(text);
			}
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
		return new String[] { "morpheus", "sleepingoverhaul" };
	}

}
