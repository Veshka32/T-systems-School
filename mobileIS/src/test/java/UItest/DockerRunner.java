package UItest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class DockerRunner {
    private static final String[] up_args = {"cmd.exe", "/c", "docker-compose up -d"};
    private static final String[] down_args = {"cmd.exe", "/c", "docker-compose down --rmi all"};
    private static String path = "";

    public void run(String path) throws IOException {
        DockerRunner.path = path;
        Process xxx = Runtime.getRuntime().exec(up_args, null, new File(path));
        BufferedReader input = new BufferedReader(new InputStreamReader(xxx.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();
    }

    public void down() throws IOException {
        Runtime.getRuntime().exec(down_args, null, new File(path));
    }

}
