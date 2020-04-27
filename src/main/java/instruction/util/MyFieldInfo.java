package instruction.util;

import javassist.CtClass;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyFieldInfo {
    private String fieldName;
    private CtClass type;
    private CtClass parent;
}
