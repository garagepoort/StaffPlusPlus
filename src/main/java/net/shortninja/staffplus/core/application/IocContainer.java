package net.shortninja.staffplus.core.application;

import net.shortninja.staffplus.core.StaffPlus;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class IocContainer {

    private static final Predicate<Class<?>> conditionalOnProperty = a -> {
        IocBean annotation = a.getAnnotation(IocBean.class);
        String conditionalOnProperty = annotation.conditionalOnProperty();
        if (!StringUtils.isEmpty(conditionalOnProperty)) {
            String[] split = conditionalOnProperty.split("=");
            String key = split[0];
            String value = split[1];
            return Objects.requireNonNull(StaffPlus.get().getConfig().getString(key)).equalsIgnoreCase(value);
        }
        return true;
    };

    private static StaffPlus staffPlus;

    private static final Map<Class, Object> beans = new HashMap<>();

    public static void init(StaffPlus staffPlus) {
        IocContainer.staffPlus = staffPlus;
        Reflections reflections = new Reflections("net.shortninja.staffplus.core");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(IocBean.class).stream().filter(conditionalOnProperty).collect(Collectors.toSet());
        for (Class<?> aClass : typesAnnotatedWith) {
            instantiateBean(reflections, aClass, typesAnnotatedWith, false);
        }
    }

    private static Object instantiateBean(Reflections reflections, Class<?> aClass, Set<Class<?>> validBeans, boolean multi) {
        StaffPlus.get().getLogger().info("Instantiating bean [" + aClass.getName() + "]");

        if (multi) {
            beans.putIfAbsent(aClass, new ArrayList<>());
            return beans.get(aClass);
        }

        if (aClass.isAnnotationPresent(IocMultiProvider.class)) {
            Class multiClass = aClass.getAnnotation(IocMultiProvider.class).value();
            beans.putIfAbsent(multiClass, new ArrayList<>());
            Object bean = createBean(reflections, aClass, validBeans);
            ((List) beans.get(multiClass)).add(bean);
            return bean;
        }

        if (aClass.isInterface()) {
            Optional<Object> existingBean = beans.keySet().stream().filter(aClass::isAssignableFrom).map(beans::get).findFirst();
            if (existingBean.isPresent()) {
                return existingBean.get();
            }

            Set<Class<?>> subTypesOf = reflections.getSubTypesOf((Class<Object>) aClass).stream().filter(validBeans::contains).collect(Collectors.toSet());
            if (subTypesOf.isEmpty()) {
                throw new RuntimeException("Cannot instantiate bean with interface " + aClass.getName() + ". No classes implementing this interface");
            }
            if (subTypesOf.size() == 1) {
                return createBean(reflections, subTypesOf.iterator().next(), validBeans);
            }
            return subTypesOf.stream().map(s -> createBean(reflections, s, validBeans)).collect(Collectors.toList());
        }
        return createBean(reflections, aClass, validBeans);
    }

    private static Object createBean(Reflections reflections, Class<?> aClass, Set<Class<?>> validBeans) {
        if (beans.containsKey(aClass)) {
            return beans.get(aClass);
        }
        if (!aClass.isAnnotationPresent(IocBean.class)) {
            throw new RuntimeException("Cannot instantiate bean. No IocBean annotation present. [" + aClass.getName() + "]");
        }
        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        if (declaredConstructors.length > 1) {
            throw new RuntimeException("Cannot instantiate bean with type " + aClass.getName() + ". Only one constructor should be defined");
        }

        StaffPlus.get().getLogger().info("Start creation of bean [" + aClass.getName() + "]");
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructors()[0];
        List<Object> constructorParams = new ArrayList<>();
        Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> classParam = parameterTypes[i];
            Annotation[] parameterAnnotations = declaredConstructor.getParameterAnnotations()[i];
            Optional<Annotation> multiAnnotation = Arrays.stream(parameterAnnotations).filter(a -> a.annotationType().equals(IocMulti.class)).findFirst();
            if (multiAnnotation.isPresent()) {
                IocMulti iocMulti = (IocMulti) multiAnnotation.get();
                Object o = instantiateBean(reflections, iocMulti.value(), validBeans, true);
                constructorParams.add(o);
            } else {
                Object o = instantiateBean(reflections, classParam, validBeans, false);
                constructorParams.add(o);
            }
        }
        try {
            StaffPlus.get().getLogger().info("Creating new bean [" + aClass.getName() + "] with constructor arguments [" + constructorParams.stream().map(d -> d.getClass().getName()).collect(Collectors.joining(",")) + "]");
            Object bean = declaredConstructor.newInstance(constructorParams.toArray());
            beans.putIfAbsent(aClass, bean);
            return bean;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot instantiate bean with type " + aClass.getName() + ". " + e.getMessage());
        }
    }

    public static void registerBean(Object o) {
        beans.put(o.getClass(), o);
    }


    public static <T> T get(Class<T> clazz) {
        if (clazz.isInterface()) {
            List<Object> collect = beans.keySet().stream().filter(clazz::isAssignableFrom).map(beans::get).collect(Collectors.toList());
            if (collect.size() > 1) {
                throw new RuntimeException("Cannot retrieve bean with interface " + clazz.getName() + ". Too many implementations registered. Use `getList` to retrieve a list of all beans");
            }
            if (collect.isEmpty()) {
                throw new RuntimeException("Cannot retrieve bean with interface " + clazz.getName() + ". No implementation registered");
            }
            return (T) collect.get(0);
        }
        return (T) beans.get(clazz);
    }

    public static <T> List<T> getList(Class<T> clazz) {
        return (List<T>) beans.get(clazz);
    }

    private static <T> T initBean(Class<T> clazz, Supplier<T> consumer) {
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, consumer.get());
        }
        return (T) beans.get(clazz);
    }

}
