package net.shortninja.staffplus.core.common.permissions.aspect;

import be.garagepoort.mcioc.ReflectionUtils;
import net.shortninja.staffplus.core.StaffPlus;
import net.shortninja.staffplus.core.common.permissions.Permission;
import net.shortninja.staffplus.core.common.permissions.PermissionHandler;
import net.shortninja.staffplus.core.common.permissions.PlayerParam;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
public class PermissionMethodAspect {

    @Pointcut("@annotation(permission)")
    public void annotatedMethod(Permission permission) {
    }

    @Before("annotatedMethod(permission) && execution(* *(..))")
    public void around(JoinPoint pjp, Permission permission) {
        PermissionHandler permissionHandler = StaffPlus.get().getIocContainer().get(PermissionHandler.class);
        Object[] args = pjp.getArgs();
        Annotation[][] parameterAnnotations = ((MethodSignature) pjp.getStaticPart().getSignature()).getMethod().getParameterAnnotations();

        Optional<Integer> playerArgIndex = getPlayerArgIndex(args, parameterAnnotations);
        if (!playerArgIndex.isPresent()) {
            throw new RuntimeException("Methods annotated with Permission must have a PlayerParam argument");
        }

        CommandSender commandSender = (CommandSender) args[playerArgIndex.get()];

        Set<String> permissions = Arrays.stream(permission.permissions())
            .map(p -> ReflectionUtils.getConfigValue(p, StaffPlus.get().getFileConfigurations()))
            .filter(Optional::isPresent)
            .map(p -> (String) p.get())
            .collect(Collectors.toSet());

        permissionHandler.validateAny(commandSender, permissions);
    }

    private Optional<Integer> getPlayerArgIndex(Object[] args, Annotation[][] parameterAnnotations) {
        assert args.length == parameterAnnotations.length;
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (annotation instanceof PlayerParam) {
                    return Optional.of(argIndex);
                }
            }
        }
        return Optional.empty();
    }
}