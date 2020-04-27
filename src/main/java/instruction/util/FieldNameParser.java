package instruction.util;

import javassist.*;
import reflection.ReflectionUtil;

public interface FieldNameParser {
    //pck.pck.className pck.pck.method(pck.par1, pck.par2)
    default MyMethodInfo getMethodInfo(String className, String classMethodName) {
        String method = classMethodName.substring(0, classMethodName.lastIndexOf("("));
        String methodName = method.contains(".") ? method.substring(method.lastIndexOf(".")+1) : method;
        String returnType = method.contains(".") ? method.substring(0, method.lastIndexOf(".")) : "void";
        String methodPars = classMethodName.substring(classMethodName.lastIndexOf("(")+1, classMethodName.length()-1);
        String[] methodParNames = methodPars.split(",");
        CtClass[] methodParClass = new CtClass[methodParNames.length];
        for(int i = 0; i < methodParNames.length; i++) {
            methodParClass[i] = ReflectionUtil.loadClass(methodParNames[i]);
        }
        return MyMethodInfo.builder()
                .parent(ReflectionUtil.loadClass(className))
                .returnType(ReflectionUtil.loadClass(returnType))
                .parameters(methodParClass)
                .name(methodName)
                .build();
    }

    //pck.pck.className pck.pck.fieldName
    default MyFieldInfo getFieldInfo(String className, String fieldName) {
        return MyFieldInfo.builder()
                .parent(ReflectionUtil.loadClass(className))
                .fieldName(fieldName.substring(fieldName.lastIndexOf(".")+1))
                .type(ReflectionUtil.loadClass(fieldName.substring(0, fieldName.lastIndexOf("."))))
                .build();
    }

    default CtMethod findMethod(MyMethodInfo methodInfo) {
        CtMethod[] ctMethods = methodInfo.getParent().getDeclaredMethods();
        for(CtMethod ctMethod : ctMethods) {
            if(ctMethod.getName().equals(methodInfo.getName())) {
                try {
                    int i = 0;
                    for(CtClass ctParameterType : ctMethod.getParameterTypes()) {
                        if(!ctParameterType.getName().equals(methodInfo.getParameters()[i].getName()))
                            throw new IllegalStateException();
                        i++;
                    }
                    return ctMethod;
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

     default CtConstructor findConstructor(MyMethodInfo methodInfo) {
        CtConstructor[] ctConstructors = methodInfo.getParent().getDeclaredConstructors();
        for(CtConstructor ctConstructor : ctConstructors) {
            if(ctConstructor.getName().equals(methodInfo.getName())) {
                try {
                    int i = 0;
                    for(CtClass ctParameterType : ctConstructor.getParameterTypes()) {
                        if(!ctParameterType.getName().equals(methodInfo.getParameters()[i].getName()))
                            throw new IllegalStateException();
                        i++;
                    }
                    return ctConstructor;
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    default CtField findField(MyFieldInfo fieldInfo) {
        CtField[] ctFields = fieldInfo.getParent().getDeclaredFields();
        for(CtField ctField : ctFields) {
            try {
                if(ctField.getName().equals(fieldInfo.getFieldName()) && ctField.getType() == fieldInfo.getType())
                    return ctField;
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
