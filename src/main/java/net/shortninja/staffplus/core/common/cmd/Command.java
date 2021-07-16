package net.shortninja.staffplus.core.common.cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Command {
    String[] permissions() default "";
    String description() default "";
    String usage() default  "";
    boolean delayable() default  false;
    String command();
    PlayerRetrievalStrategy playerRetrievalStrategy() default PlayerRetrievalStrategy.NONE;
}