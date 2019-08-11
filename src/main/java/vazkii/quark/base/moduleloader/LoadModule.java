package vazkii.quark.base.moduleloader;

public @interface LoadModule {

	String category();
	
	String name() default "";
	String requiredMod() default "";
	String description() default "";
	String[] antiOverlap() default { };
	SubscriptionTarget subscriptions() default SubscriptionTarget.NO;
	boolean enabledByDefault() default true;
	
}
