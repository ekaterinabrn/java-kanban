package javacourse;

public class Main {

    public static void main(String[] args) {
       TaskManager tm=new TaskManager();
       Task taskFirst=new Task(Status.NEW, "Таск1", "Купить подарок");
       tm.createTask(taskFirst);
        System.out.println("Задачи: " + tm.getAllTask());


        System.out.println("Поехали!");
    }
}
