package UItest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

class DockerRunner {
    private static final String[] up_args = {"cmd.exe", "/c", "docker-compose up -d"};
    private static final String[] down_args = {"cmd.exe", "/c", "docker-compose down -v --rmi all"};
    private static final String[] copy_log_args = {"cmd.exe", "/c", "docker cp resources_web_1:/opt/jboss/wildfly/standalone/log /Users/stas/wildfly-log"};
    private static String path = "";

    void run(String path) throws IOException {
        DockerRunner.path = path;
        Process xxx = Runtime.getRuntime().exec(up_args, null, new File(path));
        BufferedReader input = new BufferedReader(new InputStreamReader(xxx.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();
    }

    void down() throws IOException {
        Process xxx = Runtime.getRuntime().exec(down_args, null, new File(path));
        BufferedReader input = new BufferedReader(new InputStreamReader(xxx.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();
    }

    void copyLog() throws IOException {
        Process xxx = Runtime.getRuntime().exec(copy_log_args, null, new File(path));
    }

}
