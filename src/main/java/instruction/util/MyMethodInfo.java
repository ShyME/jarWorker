package instruction.util;

import javassist.CtClass;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyMethodInfo {
    private CtClass returnType;
    private String name;
    private CtClass[] parameters;
    private CtClass parent;
}
