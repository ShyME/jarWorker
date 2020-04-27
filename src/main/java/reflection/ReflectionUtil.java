package reflection;

import jar.JarContext;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class ReflectionUtil {

    public static CtClass loadClass(String classname) {
        ClassPool pool = ClassPool.getDefault();
        try {
            pool.appendClassPath(JarContext.instance.getTempJar());
            return pool.get(classname);
        } catch (NotFoundException e) {}
        return null;
    }

    public static CtClass createClass(String classname) {
        ClassPool pool = ClassPool.getDefault();
        return pool.makeClass(classname);
    }

    public static CtClass createInterface(String interfaceName) {
        ClassPool pool = ClassPool.getDefault();
        return pool.makeInterface(interfaceName);
    }
}
