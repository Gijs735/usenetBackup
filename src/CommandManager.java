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
            System.out.println(command);
            p = Runtime.getRuntime().exec(command);
            synchronized (p) {
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((s = br.readLine()) != null)
                    System.out.println(s);
                p.wait();
            }
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
            e.printStackTrace();
        }
    }
}