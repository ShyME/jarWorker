package jar;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarUtil {
    public static List<JarEntry> getJarEntries(String inputJarFilename) {
        List<JarEntry> jarEntries = new ArrayList<>();
        try(JarInputStream jis = new JarInputStream(new FileInputStream(inputJarFilename))) {
            JarEntry je;
            while((je=jis.getNextJarEntry()) != null) {
                jarEntries.add(je);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jarEntries;
    }
}
