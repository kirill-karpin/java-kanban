import com.tracker.Managers;
import com.tracker.interfaces.TaskManager;
import com.tracker.server.HttpTaskServer;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(manager);
    server.start();
  }
}
