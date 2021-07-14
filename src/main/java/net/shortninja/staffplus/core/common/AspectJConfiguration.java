package net.shortninja.staffplus.core.common;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;

@Aspect
@DeclarePrecedence("net.shortninja.staffplus.core.common.permissions.aspect.PermissionMethodAspect")
public class AspectJConfiguration {
}
