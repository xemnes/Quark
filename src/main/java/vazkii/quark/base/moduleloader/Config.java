package vazkii.quark.base.moduleloader;

public @interface Config {

	String value();
	
	String description() default "";
	String flag() default "";
	int min() default Integer.MIN_VALUE;
	int max() default Integer.MAX_VALUE;
	
}
