package vazkii.quark.client.module;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.client.tooltip.AttributeTooltips;
import vazkii.quark.client.tooltip.FoodTooltips;

/**
 * @author WireSegal
 * Created at 6:19 PM on 8/31/19.
 */
@LoadModule(category = ModuleCategory.CLIENT, hasSubscriptions = true, subscribeOn = Dist.CLIENT)
public class ImprovedTooltipsModule extends Module {

    @Config
    public static boolean attributeTooltips = true;
    @Config
    public static boolean foodTooltips = true;


    @Config(description = "The value of each shank of food. " +
            "Tweak this when using mods like Hardcore Hunger which change that value.")
    public static int foodDivisor = 2;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void makeTooltip(ItemTooltipEvent event) {
        if (attributeTooltips)
            AttributeTooltips.makeTooltip(event);
        if (foodTooltips)
            FoodTooltips.makeTooltip(event);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void renderTooltip(RenderTooltipEvent.PostText event) {
        if (attributeTooltips)
            AttributeTooltips.renderTooltip(event);
        if (foodTooltips)
            FoodTooltips.renderTooltip(event);
    }
}
