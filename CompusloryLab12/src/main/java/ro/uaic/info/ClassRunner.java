package ro.uaic.info;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ClassRunner {
    public void executeClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object instance = constructor.newInstance();
            Method runMethod = clazz.getMethod("run");
            runMethod.invoke(instance);
        } catch (ClassNotFoundException e) {
            System.err.println("Eroare: Clasa " + className + " nu a fost gasita in classpath.");
        } catch (NoSuchMethodException e) {
            System.err.println("Eroare: Metoda 'run' fara argumente nu exista in clasa specificata.");
        } catch (Exception e) {
            System.err.println("Eroare la executia prin reflection: " + e.getMessage());
        }
    }
}