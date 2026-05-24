package ro.uaic.info;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class HomeworkRunner {

    public void scanFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Eroare: Folderul " + folderPath + " nu exista sau nu este un director.");
            return;
        }

        try {
            URLClassLoader loader = new URLClassLoader(
                    new URL[]{folder.toURI().toURL()},
                    this.getClass().getClassLoader()
            );

            List<Class<?>> annotationClasses = new ArrayList<>();
            List<Class<?>> publicClasses = new ArrayList<>();

            List<File> classFiles = new ArrayList<>();
            findClassFiles(folder, classFiles);

            for (File classFile : classFiles) {
                String relativePath = folder.toURI().relativize(classFile.toURI()).getPath();
                String className = relativePath.replace('/', '.').replace('\\', '.').replace(".class", "");

                try {
                    Class<?> clazz = loader.loadClass(className);
                    if (clazz.isAnnotation()) {
                        annotationClasses.add(clazz);
                        System.out.println("Adnotare identificata: " + clazz.getName());
                    } else if (Modifier.isPublic(clazz.getModifiers())) {
                        publicClasses.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Nu s-a putut incarca: " + className);
                }
            }

            System.out.println("\n==================== PROTOTIPURI CLASE ====================");
            for (Class<?> clazz : publicClasses) {
                displayPrototype(clazz);
            }

            System.out.println("\n==================== EXECUTARE METODE ADNOTATE ====================");
            for (Class<?> clazz : publicClasses) {
                invokeAnnotatedMethods(clazz, annotationClasses);
            }

        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void findClassFiles(File dir, List<File> result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                findClassFiles(file, result);
            } else if (file.getName().endsWith(".class")) {
                result.add(file);
            }
        }
    }

    private void displayPrototype(Class<?> clazz) {
        System.out.println("\n--- " + clazz.getName() + " ---");
        System.out.println("  Pachet: " + clazz.getPackageName());
        System.out.println("  Modificatori: " + Modifier.toString(clazz.getModifiers()));
        System.out.println("  Superclasa: " + clazz.getSuperclass().getName());
        System.out.println("  Interfete: " + java.util.Arrays.toString(clazz.getInterfaces()));
        System.out.println("  Metode:");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("    " + Modifier.toString(method.getModifiers()) + " "
                    + method.getReturnType().getSimpleName() + " "
                    + method.getName() + "("
                    + getParameterTypes(method) + ")");
        }
    }

    private String getParameterTypes(Method method) {
        Class<?>[] params = method.getParameterTypes();
        if (params.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(params[i].getSimpleName());
        }
        return sb.toString();
    }

    private void invokeAnnotatedMethods(Class<?> clazz, List<Class<?>> annotationClasses) {
        if (annotationClasses.isEmpty()) {
            System.out.println("Nicio adnotare gasita in folder. Se sari peste " + clazz.getName());
            return;
        }

        Object instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("Nu se poate instantia " + clazz.getName() + " (fara constructor default?)");
            return;
        }

        for (Method method : clazz.getDeclaredMethods()) {
            for (Class<?> annClass : annotationClasses) {
                @SuppressWarnings("unchecked")
                Class<? extends Annotation> annType = (Class<? extends Annotation>) annClass;
                if (method.isAnnotationPresent(annType)) {
                    System.out.println("\n  Metoda adnotata: " + method.getName()
                            + " (@" + annClass.getSimpleName() + ")");

                    Class<?>[] paramTypes = method.getParameterTypes();
                    try {
                        if (paramTypes.length == 0) {
                            method.setAccessible(true);
                            method.invoke(instance);
                            System.out.println("    -> Executata cu succes (fara parametri)");
                        } else if (paramTypes.length == 1 && paramTypes[0] == int.class) {
                            method.setAccessible(true);
                            method.invoke(instance, 42);
                            System.out.println("    -> Executata cu succes (mock value: 42)");
                        } else {
                            System.out.println("    -> Sari peste: tipuri de parametri neacceptate");
                        }
                    } catch (Exception e) {
                        System.out.println("    -> Esec: " + e.getCause().getMessage());
                    }
                }
            }
        }
    }
}
