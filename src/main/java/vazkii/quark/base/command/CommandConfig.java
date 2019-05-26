/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [15/07/2016, 05:22:28 (GMT)]
 */
package vazkii.quark.base.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.module.GlobalConfig;
import vazkii.quark.base.network.message.MessageChangeConfig;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandConfig extends CommandBase {

	private static final Pattern TOKENIZER = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
	
	@Nonnull
	@Override
	public String getName() {
		return "quarkconfig";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender) {
		return "commands.quarkconfig.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;	
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
		String fullInput = String.join(" ", args);
		Matcher m = TOKENIZER.matcher(fullInput);
		
		List<String> matches = new ArrayList<>();
		while(m.find()) {
			String s = m.group(0);
			if(s.startsWith("\"") && s.endsWith("\"")) {
				s = s.replaceAll("\"$", "");
				s = s.replaceAll("^\"", "");
			}
				
			matches.add(s);
		}
		
		if(matches.size() < 4)
			 throw new WrongUsageException(getUsage(sender));
		
		boolean save = matches.size() > 4 && matches.get(4).equals("save");
		
		String moduleName = matches.get(0);
		String category = matches.get(1);
		String key = matches.get(2);
		String value = matches.get(3);
		
		GlobalConfig.changeConfig(moduleName, category, key, value, save);
		
		String player = matches.size() > 5 ? matches.get(5) : null;
		if(player != null) {
			EntityPlayerMP playerMP = getPlayer(server, sender, player);
			NetworkHandler.INSTANCE.sendTo(new MessageChangeConfig(moduleName, category, key, value), playerMP);
		} else NetworkHandler.INSTANCE.sendToAll(new MessageChangeConfig(moduleName, category, key, value));
	}

}
