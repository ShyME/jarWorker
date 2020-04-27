package jar;

import lombok.Data;

@Data
public class JarContext {
    public static final JarContext instance = new JarContext();
    private JarContext() {}

    private final String tempJar = "temp.jar";
    private final String tempJar2 = "temp2.jar";
    private String inputJar;

    public void setInputJar(String inputJar) {
        this.inputJar = inputJar;
    }
}
