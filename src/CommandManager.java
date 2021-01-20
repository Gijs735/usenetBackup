import java.io.BufferedReader;
import java.io.InputStreamReader;

class CommandManager extends Thread {
    String command;

    public CommandManager(String commandparameter){
        command = commandparameter;
    }

    public void run() {
        try {
            String s;
            Process p;
            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println(s);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
            return;
        } catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
            return;
        }
    }
}