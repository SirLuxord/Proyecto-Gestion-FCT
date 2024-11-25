package dad.gestion_fct.controllers.empresa;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmpresaSearchDialog extends Dialog<String> implements Initializable {

    // model

    private StringProperty nif = new SimpleStringProperty();

    // view

    @FXML
    private TextField nifField;

    @FXML
    private BorderPane root;

    public EmpresaSearchDialog(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/searchEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // init dialog

        setTitle("Buscar");
        getDialogPane().setContent(root);
        getDialogPane().getButtonTypes().setAll(new ButtonType("Buscar" , ButtonBar.ButtonData.OK_DONE) , ButtonType.CANCEL);
        setResultConverter(this::onResult);

        // Bindings

        nif.bind(nifField.textProperty());

    }

    private String onResult(ButtonType buttonType) {
        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
            return nif.get();
        }
        return null;
    }
}
