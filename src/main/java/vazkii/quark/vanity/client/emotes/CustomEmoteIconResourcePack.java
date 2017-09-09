package vazkii.quark.vanity.client.emotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import vazkii.quark.base.Quark;
import vazkii.quark.vanity.feature.EmoteSystem;

public class CustomEmoteIconResourcePack extends AbstractResourcePack {

	private List<String> verifiedNames = new ArrayList();
	private List<String> existingNames = new ArrayList();

	public CustomEmoteIconResourcePack() {
		super(Loader.instance().activeModContainer().getSource());
	}
	
	@Override
	public Set<String> getResourceDomains() {
		return ImmutableSet.of(EmoteHandler.CUSTOM_EMOTE_NAMESPACE);
	}
	
	@Override
	public InputStream getInputStream(ResourceLocation location) throws IOException {
		return getInputStreamByName(location.toString());
	}

	@Override
	protected InputStream getInputStreamByName(String name) throws IOException {
		if(name.equals("pack.mcmeta"))
			return Quark.class.getResourceAsStream("/proxypack.mcmeta");
		
		File file = getFile(name);
		if(!file.exists())
			return null;
		
		return new FileInputStream(file);
	}

	@Override
	public boolean resourceExists(ResourceLocation location) {
		return hasResourceName(location.toString());
	}
	
	@Override
	protected boolean hasResourceName(String name) {
		if(!verifiedNames.contains(name)) {
			File file = getFile(name);
			if(file.exists())
				existingNames.add(name);
			verifiedNames.add(name);
		}
		
		return existingNames.contains(name);
	}
	
	private File getFile(String name) {
		String filename = name.substring(name.indexOf(":") + 1) + ".png";
		return new File(EmoteSystem.emotesDir, filename);
	}

}
