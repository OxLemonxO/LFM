package net.camtech.fopmremastered.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters
{
    String name();

    String description();

    String usage();

    String aliases() default "";

    Rank rank() default Rank.OP;
}
