import model.Datasource;
import model.Task;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        Datasource.getInstance().open();


       // datasource.createView();
    /*    datasource.insertUser("Adrian","Lachociagowski");
        datasource.insertUser("Jacek","Placek");
        datasource.insertUser("Dawid","Podsiadlo");
        datasource.insertUser("Krzysztof","Zalewski");
        datasource.insertUser("Piotr","Zola");
        datasource.insertUser("Miuosh","Borycki");


        datasource.insertTask("Adrian","Lachociagowski","Zrobic cos na pokaz",false);
        datasource.insertTask("Jacek","Placek","Oranie na kolanie",false);
        datasource.insertTask("Dawid","Podsiadlo","Wyrwac sie z malego miasteczka",false);
        datasource.insertTask("Krzysztof","Zalewski","Biec do gwiazd",false);
        datasource.insertTask("Piotr","Zola","Kupic Bilet",false);
        datasource.insertTask("Miuosh","Borycki","Pierdolic to wszystko",false);
        datasource.insertTask("Miuosh","Borycki","Isc, ciagle isc",false);*/
        try {
            for (Task task: Datasource.getInstance().queryTasks(Datasource.getInstance().queryUser("Miuosh", "Borycki")))
                System.out.println("ID: " + task.get_id() + " Description: " + task.getDescription() + " Task done?: " + task.isTaskDone() + " Create Date " + task.getCreateDate());
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }


        Datasource.getInstance().close();

    }
}
