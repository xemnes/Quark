/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [26/03/2016, 22:09:14 (GMT)]
 */
package vazkii.quark.vanity.command;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Files;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.network.message.MessageDoEmote;
import vazkii.quark.vanity.feature.EmoteSystem;

public class CommandEmote extends CommandBase {

	@Override
	public String getName() {
		return "emote";
	}

	@Override
	public String getUsage(ICommandSender p_71518_1_) {
		return "<emote>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0 && sender instanceof EntityPlayer) {
			String emoteName = args[0];
			NetworkHandler.INSTANCE.sendToAll(new MessageDoEmote(emoteName, sender.getName()));

			if(EmoteSystem.emoteCommands) {
				String filename = emoteName + ".mcfunction";
				if(filename.startsWith("custom:"))
					filename = filename.substring("custom:".length());
				File file = new File(EmoteSystem.emotesDir, filename);

				if(file.exists())
					try {
						FunctionObject func = FunctionObject.create(server.getFunctionManager(), Files.readLines(file, StandardCharsets.UTF_8));
						server.getFunctionManager().execute(func, new EmoteCommandSender(server, sender));
					} catch(IOException e) {
						throw new CommandException(e.getMessage());
					}
			}
		}
	}

	private static class EmoteCommandSender implements ICommandSender {
		
		final MinecraftServer server;
		final ICommandSender superSender;
		
		public EmoteCommandSender(MinecraftServer server, ICommandSender superSender) {
			this.server = server;
			this.superSender = superSender;
		}
	
		@Override
		public MinecraftServer getServer() {
			return server;
		}

		@Override
		public String getName() {
			return "Quark-Emotes[" + superSender.getName() + "]";
		}

		@Override
		public World getEntityWorld() {
			return superSender.getEntityWorld();
		}

		@Override
		public boolean canUseCommand(int permLevel, String commandName) {
			return !commandName.equals("emote") && permLevel <= 2;
		}
		
		@Override
		public BlockPos getPosition() {
			return superSender.getPosition();
		}
		
		@Override
		public Vec3d getPositionVector() {
			return superSender.getPositionVector();
		}
		
		@Override
		public Entity getCommandSenderEntity() {
			return superSender.getCommandSenderEntity();
		}
		
		@Override
		public boolean sendCommandFeedback() {
			return EmoteSystem.customEmoteDebug && getEntityWorld().getGameRules().getBoolean("commandBlockOutput");
		}

	}
}
