import com.tracker.TaskManager;


public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();
        var task1 = taskManager.createNewTask("Задание 1", "Оценка");
        var task2 = taskManager.createNewTask("Задание 2", "Исполнение");

        var epic1 = taskManager.createNewEpic("Эпическое задание 1", "Размером с Техас");
        var subTask11 = taskManager.createNewSubtask("Подзадание 1", "Какая-то подзадача");
        var subTask12 = taskManager.createNewSubtask("Подзадание 2", "Еще какая-то подзадача");

        taskManager.addTaskToEpic(subTask11, epic1);
        taskManager.addTaskToEpic(subTask12, epic1);

        var epic2 = taskManager.createNewEpic("Эпическое задание 2", "Размером с Солнце");
        var subTask21 = taskManager.createNewSubtask("Подзадание 1", "Какая-то подзадача");
        taskManager.addTaskToEpic(subTask21, epic2);

        System.out.println("Созданные задачи:");
        System.out.println(taskManager);
        System.out.println();

        System.out.println("#ТЕСТ1: Изменение статуса задачи и подзадачи");

        taskManager.setTaskStatusDone(task1.getId());
        taskManager.setTaskStatusDone(subTask21.getId());

        System.out.println(taskManager);
        System.out.println();

        System.out.println("#TЕСТ2: Удаление подзадачи");

        taskManager.deleteTask(subTask21);
        System.out.println(taskManager);

        System.out.println();

        System.out.println("#TЕСТ3: Удаление эпика:");

        taskManager.deleteTask(epic2);
        System.out.println(taskManager);

    }
}
