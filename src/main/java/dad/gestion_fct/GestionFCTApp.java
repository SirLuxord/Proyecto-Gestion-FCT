package dad.gestion_fct;


import dad.gestion_fct.controllers.ContactoEmp.ContactoEmpController;
import dad.gestion_fct.controllers.RootController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GestionFCTApp extends Application {

    private RootController rootController = new RootController();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(rootController.getRoot());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestión FCT");
        primaryStage.show();
    }
}
