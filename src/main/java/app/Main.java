package app;

public class Main {
    public static void main(String[] args) {
        JarWorker jarWorker = new JarWorker();
        //String input = "--i Invaders.jar --script script.txt --o jarChanged2.jar";
        //jarWorker.run(Arrays.asList(input.split(" ")));
        StringBuilder input = new StringBuilder();
        for (String arg : args) {
            input.append(arg).append(" ");
        }
        if(input.toString().isEmpty())
            throw new IllegalStateException("Empty argument list");
        else jarWorker.run(input.toString());
    }
}
