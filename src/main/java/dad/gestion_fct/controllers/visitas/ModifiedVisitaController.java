package dad.gestion_fct.controllers.visitas;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifiedVisitaController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ModifiedVisitaController() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(""));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
