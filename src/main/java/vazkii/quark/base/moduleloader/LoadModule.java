package vazkii.quark.base.moduleloader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadModule {

	String category();
	
	String name() default "";
	String requiredMod() default "";
	String description() default "";
	String[] antiOverlap() default { };
	SubscriptionTarget subscriptions() default SubscriptionTarget.NO;
	boolean enabledByDefault() default true;
	
}
