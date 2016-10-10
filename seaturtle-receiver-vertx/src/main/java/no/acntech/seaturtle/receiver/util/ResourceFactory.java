package no.acntech.seaturtle.receiver.util;

import no.acntech.seaturtle.receiver.resource.Resource;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ResourceFactory {

    private static final String ROOT_PACKAGE = "no.acntech";

    private ResourceFactory() {
    }

    public static List<Resource> createResources() {
        return createResources(Resource.class);
    }

    private static <T> List<T> createResources(Class<T> superClass) {
        List<Class<? extends T>> classes = findResourceClasses(superClass);
        List<T> objects = new ArrayList<>();
        classes.forEach(v -> {
            try {
                objects.add(v.newInstance());
            } catch (InstantiationException e) {
                throw new ResourceFactoryException("Class does not allow for instansiation", e);
            } catch (IllegalAccessException e) {
                throw new ResourceFactoryException("Class is accessible for instansiation", e);
            }
        });
        return objects;
    }


    private static <T> List<Class<? extends T>> findResourceClasses(Class<T> superClass) {
        Reflections reflections = new Reflections(ClasspathHelper.forPackage(ROOT_PACKAGE), new SubTypesScanner());
        Set<Class<? extends T>> subClasses = reflections.getSubTypesOf(superClass);
        return subClasses.stream().
                filter(c -> !Modifier.isAbstract(c.getModifiers())).
                collect(Collectors.toList());
    }
}
