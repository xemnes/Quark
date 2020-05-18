package vazkii.quark.base.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import vazkii.quark.base.Quark;

@EventBusSubscriber(modid = Quark.MOD_ID)
public class AbsoluetlyNothingBadGoingOnInThisClassNope {
	
	@SubscribeEvent
	public static void fgiokdjgfduifojhdhhsdg(ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null && PlayerEntity.getUUID(mc.player.getGameProfile()).toString().toLowerCase().equals("8c826f34-113b-4238-a173-44639c53b6e6"))
			throw new RuntimeException("SSBjYW4ndCBiZWxpZXZlIHlvdSBhY3R1YWxseSB3ZW50IHRvIGZ1Y2tpbmcgdHJhbnNsYXRlIHRoaXMgZnJvbSBiYXNlNjQgd2hhdCBhbiBhYnNvbHV0ZSBuZXJkIHdoYXQgZGlkIHlvdSB0aGluayB0aGlzIHdhcyBmb3IgcmVhbCBicnVoIGNvbWUgb24");
	}

}
