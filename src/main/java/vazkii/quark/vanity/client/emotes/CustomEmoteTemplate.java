package vazkii.quark.vanity.client.emotes;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.vanity.module.EmoteModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@OnlyIn(Dist.CLIENT)
public class CustomEmoteTemplate extends EmoteTemplate {

	private String name;
	
	public CustomEmoteTemplate(String file) {
		super(file + ".emote");
		
		if(name == null)
			name = file;
	}
	
	@Override
	BufferedReader createReader() throws FileNotFoundException {
		return new BufferedReader(new FileReader(new File(EmoteModule.emotesDir, file)));
	}
	
	@Override
	void setName(String[] tokens) {
		StringBuilder builder = new StringBuilder();
		for(int i = 1; i < tokens.length; i++) {
			builder.append(tokens[i]);
			builder.append(" ");
		}
		
		name = builder.toString().trim();
	}
	
	public String getName() {
		return name;
	}
	
}
