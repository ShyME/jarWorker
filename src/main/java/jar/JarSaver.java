package jar;

import javassist.CannotCompileException;
import javassist.CtClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class JarSaver {
    private JarContext jarContext = JarContext.instance;

    public void saveWorkAs(String filename) {
        try {
            copyJarFile(jarContext.getTempJar(), filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(CtClass ctClass) throws IOException {
        try {
            copyJarFile(jarContext.getTempJar(), jarContext.getTempJar2(), ctClass.getName().replaceAll("\\.", "/") + ".class", ctClass.toBytecode());
            ctClass.defrost();
            swapFiles(jarContext.getTempJar(), jarContext.getTempJar2());
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void add(String packName) throws IOException {
        packName = packName.replaceAll("\\.", "/");
        copyJarFile(jarContext.getTempJar(), jarContext.getTempJar2(), packName, null);
        swapFiles(jarContext.getTempJar(), jarContext.getTempJar2());
    }

    public void update(CtClass ctClass) throws IOException {
        add(ctClass);
    }

    public void remove(String entry) {
        try {
            entry = entry.replaceAll("\\.", "/");
            if(!entry.endsWith("/"))
                entry = entry + ".class";
            copyJarFile(jarContext.getTempJar(), jarContext.getTempJar2(), entry);
            swapFiles(jarContext.getTempJar(), jarContext.getTempJar2());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTempJar() {
        try {
            copyJarFile(jarContext.getInputJar(), jarContext.getTempJar());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeTempJar() {
        File tempJarFile = new File(jarContext.getTempJar());
        File tempJarFile2 = new File(jarContext.getTempJar2());
        try {
            Files.delete(tempJarFile.toPath());
            if(tempJarFile2.exists())
                Files.delete(tempJarFile2.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyJarFile(String from, String to, String omit, String toBeAdded, byte[] content) throws IOException {
        File destFile = new File(to);
        FileOutputStream fos = new FileOutputStream(destFile);
        JarOutputStream jos = new JarOutputStream(fos);
        JarFile jarFile = new JarFile(from);
        Enumeration<JarEntry> entries = jarFile.entries();

        boolean added = false;
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(omit != null && entry.getName().startsWith(omit))
                continue;
            InputStream is = jarFile.getInputStream(entry);
            if(entry.getName().equals(toBeAdded)) {
                jos.putNextEntry(new JarEntry(toBeAdded));
                jos.write(content, 0, content.length);
                added = true;
            }
            else {
                jos.putNextEntry(new JarEntry(entry.getName()));
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = is.read(buffer)) != -1) {
                    jos.write(buffer, 0, bytesRead);
                }
            }
            is.close();
            jos.flush();
            jos.closeEntry();
        }
        if(toBeAdded != null && !added) {
            jos.putNextEntry(new JarEntry(toBeAdded));
            if(content != null)
                jos.write(content, 0, content.length);
            jos.flush();
            jos.closeEntry();
        }
        jos.close();
        jarFile.close();
        fos.close();
    }

    public void copyJarFile(String from, String to, String toBeAdded, byte[] content) throws IOException  {
        copyJarFile(from, to, null, toBeAdded, content);
    }

    public void copyJarFile(String from, String to, String omit) throws IOException {
        copyJarFile(from, to, omit, null, null);
    }

    public void copyJarFile(String from, String to) throws IOException {
        copyJarFile(from, to, null, null, null);
    }

    private void swapFiles(String tempJar, String tempJar2) {
        File source = new File(tempJar2);
        File dest = new File(tempJar);

        try {
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!source.delete())
            throw new IllegalStateException("Could not delete a file!");
    }
}

