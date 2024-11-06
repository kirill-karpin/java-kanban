import com.tracker.InMemoryTaskManager;
import com.tracker.Managers;
import com.tracker.interfaces.TaskManager;
import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.SubTask;
import com.tracker.task.Task;


public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();

        manager.add(new Task("Первая задача", "Описание", Status.NEW));

        int epicId = manager.add(new Epic("Первая задача", "Описание"));
        manager.getEpic(epicId);
        int subTaskId = manager.add(new SubTask("Первая подзадача", "Описание", Status.NEW));
        SubTask subTask = manager.getSubtask(subTaskId);
        manager.addEpicSubTask(epicId, subTask);
        subTask.setStatusDone();
        manager.addEpicSubTask(epicId, new SubTask("Вторая подзадача", "Описание", Status.DONE));
        manager.getSubtask(subTaskId);
        manager.getEpic(epicId);
        printAllTasks(manager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println();
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println();
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println();
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
