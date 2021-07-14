package net.shortninja.staffplus.core.common.cmd;

import be.garagepoort.mcioc.ReflectionUtils;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.permissions.Permission;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
public class CmdAspect {

    @Pointcut("@within(net.shortninja.staffplus.core.common.permissions.Permission)")
    public void annotatedClass() {}

    @Before(value = "annotatedClass() && execution(* execute(..))", argNames = "pjp")
    public void beforeExecution(JoinPoint pjp) {
        PermissionHandler permissionHandler = StaffPlus.get().getIocContainer().get(PermissionHandler.class);
        Object[] args = pjp.getArgs();
        CommandSender commandSender = (CommandSender) args[0];

        Permission permission = pjp.getTarget().getClass().getAnnotation(Permission.class);

        Set<String> permissions = Arrays.stream(permission.permissions())
            .map(p -> ReflectionUtils.getConfigValue(p, StaffPlus.get().getFileConfigurations()))
            .filter(Optional::isPresent)
            .map(p -> (String) p.get())
            .collect(Collectors.toSet());

        permissionHandler.validateAny(commandSender, permissions);
    }
}
