package vazkii.quark.vanity.client.emotes.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLLog;
import scala.tools.nsc.backend.icode.analysis.ReachingDefinitions;
import vazkii.aurelienribon.tweenengine.Timeline;
import vazkii.aurelienribon.tweenengine.Tween;
import vazkii.quark.vanity.feature.EmoteSystem;

public class EmoteTemplate {

	private static final Map<String, Integer> parts = new HashMap();
	private static final Map<String, Integer> tweenables = new HashMap();

	private static final Map<String, Function> functions = new HashMap();

	static {
		parts.put("head", ModelAccessor.HEAD);
		parts.put("body", ModelAccessor.BODY);
		parts.put("right_arm", ModelAccessor.RIGHT_ARM);
		parts.put("left_arm", ModelAccessor.LEFT_ARM);
		parts.put("right_leg", ModelAccessor.RIGHT_LEG);
		parts.put("left_leg", ModelAccessor.LEFT_LEG);

		tweenables.put("head_x", ModelAccessor.HEAD_X);
		tweenables.put("head_y", ModelAccessor.HEAD_Y);
		tweenables.put("head_z", ModelAccessor.HEAD_Z);
		tweenables.put("body_x", ModelAccessor.BODY_X);
		tweenables.put("body_y", ModelAccessor.BODY_Y);
		tweenables.put("body_z", ModelAccessor.BODY_Z);
		tweenables.put("right_arm_x", ModelAccessor.RIGHT_ARM_X);
		tweenables.put("right_arm_y", ModelAccessor.RIGHT_ARM_Y);
		tweenables.put("right_arm_z", ModelAccessor.RIGHT_ARM_Z);
		tweenables.put("left_arm_x", ModelAccessor.LEFT_ARM_X);
		tweenables.put("left_arm_y", ModelAccessor.LEFT_ARM_Y);
		tweenables.put("left_arm_z", ModelAccessor.LEFT_ARM_Z);
		tweenables.put("right_leg_x", ModelAccessor.RIGHT_LEG_X);
		tweenables.put("right_leg_y", ModelAccessor.RIGHT_LEG_Y);
		tweenables.put("right_leg_z", ModelAccessor.RIGHT_LEG_Z);
		tweenables.put("left_leg_x", ModelAccessor.LEFT_LEG_X);
		tweenables.put("left_leg_y", ModelAccessor.LEFT_LEG_Y);
		tweenables.put("left_leg_z", ModelAccessor.LEFT_LEG_Z);

		functions.put("use", EmoteTemplate::use);
		functions.put("animation", EmoteTemplate::animation);
		functions.put("section", EmoteTemplate::section);
		functions.put("end", EmoteTemplate::end);
		functions.put("move", EmoteTemplate::move);
		functions.put("pause", EmoteTemplate::pause);
		functions.put("yoyo", EmoteTemplate::yoyo);
		functions.put("repeat", EmoteTemplate::repeat);
	}

	final String file;
	
	List<String> readLines;
	List<Integer> usedParts;
	Stack<Timeline> timelineStack;
	boolean compiled = false;
	boolean compiledOnce = false;
	
	public EmoteTemplate(String file) {
		this.file = file;
	}

	public Timeline getTimeline(EntityPlayer player, ModelBiped model) {
		compiled = false;

		if(readLines == null)
			return readAndMakeTimeline(player, model);
		else {
			Timeline timeline = null;
			timelineStack = new Stack();

			int i = 0;
			try {
				for(; i < readLines.size() && !compiled; i++)
					timeline = handle(model, timeline, readLines.get(i));
			} catch(Exception e) {
				logError(e, i);
				return Timeline.createSequence();
			}
			
			if(timeline == null) 
				return Timeline.createSequence();
			
			return timeline;
		}
	}
	
	public Timeline readAndMakeTimeline(EntityPlayer player, ModelBiped model) {
		Timeline timeline = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(EmoteTemplate.class.getResourceAsStream("/assets/quark/emotes/" + file)));
		usedParts = new ArrayList();
		timelineStack = new Stack();
		int lines = 0;
		
		compiled = compiledOnce = false;
		readLines = new ArrayList();
		try {
			try {
				String s;
				while((s = reader.readLine()) != null && !compiled) {
					lines++;
					readLines.add(s);
					timeline = handle(model, timeline, s);
				}
			} catch(Exception e) {
				logError(e, lines);
				return Timeline.createSequence();
			}

			if(timeline == null) 
				return Timeline.createSequence();

			return timeline;
		} finally {
			compiledOnce = true;
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void logError(Exception e, int line) {
		FMLLog.log.error("[Quark Custom Emotes] Error loading line " + line);
		if(!(e instanceof IllegalArgumentException)) {
			FMLLog.log.error("[Quark Custom Emotes] This is an Internal Error, and not one in the emote file, please report it");
			e.printStackTrace();
		}
		else FMLLog.log.error("[Quark Custom Emotes] " + e.getMessage());
	}

	private Timeline handle(ModelBiped model, Timeline timeline, String s) throws IllegalArgumentException {
		s = s.trim().toLowerCase();
		if(s.startsWith("#") || s.isEmpty())
			return timeline;
		
		String[] tokens = s.trim().split(" ");
		String function = tokens[0];
		
		if(functions.containsKey(function))
			return functions.get(function).invoke(this, model, timeline, tokens);

		throw new IllegalArgumentException("Illegal function name " + function);
	}

	private static Timeline use(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
		if(em.compiledOnce)
			return timeline;
		
		assertParamSize(tokens, 2);

		String part = tokens[1];

		if(parts.containsKey(part))
			em.usedParts.add(parts.get(part));
		else throw new IllegalArgumentException("Illgal part name for function use: " + part);

		return timeline;
	}

	private static Timeline animation(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
		if(timeline != null)
			throw new IllegalArgumentException("Illegal use of function animation, animation already started");

		assertParamSize(tokens, 2);

		String type = tokens[1];
		switch(type) {
		case "sequence":
			return Timeline.createSequence();
		case "parallel":
			return Timeline.createParallel();
		default: throw new IllegalArgumentException("Illegal animation type: " + type);
		}
	}

	private static Timeline section(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
		assertParamSize(tokens, 2);

		String type = tokens[1];
		Timeline newTimeline;
		switch(type) {
		case "sequence":
			newTimeline = Timeline.createSequence();
			break;
		case "parallel":
			newTimeline = Timeline.createParallel();
			break;
		default: throw new IllegalArgumentException("Illegal section type: " + type);
		}

		em.timelineStack.push(timeline);
		return newTimeline;
	}

	private static Timeline end(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
		assertParamSize(tokens, 1);

		if(em.timelineStack.isEmpty()) {
			em.compiled = true;
			return timeline;
		}

		Timeline poppedLine = em.timelineStack.pop();
		poppedLine.push(timeline);
		return poppedLine;
	}

	private static Timeline move(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
		if(tokens.length < 4)
			throw new IllegalArgumentException(String.format("Illgal parameter amount for function move: %d (at least 4 are required)", tokens.length));

		String partStr = tokens[1];
		int part;
		if(tweenables.containsKey(partStr))
			part = tweenables.get(partStr);
		else throw new IllegalArgumentException("Illgal part name for function move: " + partStr);

		int time = Integer.parseInt(tokens[2]);
		double target = Double.parseDouble(tokens[3]);

		Tween tween = Tween.to(model, part, time).target((float) target);
		if(tokens.length > 4) {
			int index = 4;
			while(index < tokens.length) {
				String cmd = tokens[index++];
				int times, delay;
				switch(cmd) {
				case "delay":
					assertParamSize("delay", tokens, 1, index);
					delay = Integer.parseInt(tokens[index++]);
					tween = tween.delay(delay);
					break;
				case "yoyo":
					assertParamSize("yoyo", tokens, 2, index);
					times = Integer.parseInt(tokens[index++]);
					delay = Integer.parseInt(tokens[index++]);
					tween = tween.repeatYoyo(times, delay);
					break;
				case "repeat":
					assertParamSize("repeat", tokens, 2, index);
					times = Integer.parseInt(tokens[index++]);
					delay = Integer.parseInt(tokens[index++]);
					tween = tween.repeat(times, delay);
					break;
				default:
					throw new IllegalArgumentException(String.format("Invalid modifier %s for move function", cmd));
				}
			}
		}

		return timeline.push(tween);
	}

	private static Timeline pause(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
		assertParamSize(tokens, 2);
		int ms = Integer.parseInt(tokens[1]);
		return timeline.pushPause(ms);
	}

	private static Timeline yoyo(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
		assertParamSize(tokens, 3);
		int times = Integer.parseInt(tokens[1]);
		int delay = Integer.parseInt(tokens[2]);
		return timeline.repeatYoyo(times, delay);
	}

	private static Timeline repeat(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException {
		assertParamSize(tokens, 3);
		int times = Integer.parseInt(tokens[1]);
		int delay = Integer.parseInt(tokens[2]);
		return timeline.repeat(times, delay);
	}

	private static void assertParamSize(String[] tokens, int expect) throws IllegalArgumentException {
		if(tokens.length != expect)
			throw new IllegalArgumentException(String.format("Illgal parameter amount for function %s: %d (expected %d)", tokens[0], tokens.length, expect));
	}

	private static void assertParamSize(String mod, String[] tokens, int expect, int startingFrom) throws IllegalArgumentException {
		if(tokens.length - startingFrom < expect)
			throw new IllegalArgumentException(String.format("Illgal parameter amount for move modifier %s: %d (expected at least %d)", mod, tokens.length, expect));
	}

	public boolean usesBodyPart(int part) {
		return usedParts.contains(part);
	}

	private static interface Function {
		Timeline invoke(EmoteTemplate em, ModelBiped model, Timeline timeline, String[] tokens) throws IllegalArgumentException;
	}

}
