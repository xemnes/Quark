package vazkii.quark.base.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {

	String name() default "";
	String description() default "";
	String flag() default "";
	double min() default Double.MAX_VALUE;
	double max() default Double.MIN_VALUE;
	
}
