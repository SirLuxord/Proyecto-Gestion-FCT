    package dad.gestion_fct.controllers.ContactoEmp;

    import javafx.beans.property.SimpleStringProperty;
    import javafx.beans.property.StringProperty;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.control.*;
    import javafx.scene.layout.BorderPane;

    import java.io.IOException;
    import java.net.URL;
    import java.util.ResourceBundle;

    public class ContactoEmpSearchDialog extends Dialog<String> implements Initializable {

        // model

        private StringProperty campo = new SimpleStringProperty();

        // view
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            fieldComboBox.getItems().addAll("Nombre", "Apellido", "Correo", "Teléfono", "NombreEmpresa");

            // bindings
            campo.bind(fieldComboBox.getSelectionModel().selectedItemProperty());

            setTitle("Buscar");
            setHeaderText("Elija el campo a buscar");
            getDialogPane().setContent(root);
            getDialogPane().getButtonTypes().setAll(new ButtonType("Buscar", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);

            setResultConverter(this::onResult);
        }

        public ContactoEmpSearchDialog() {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchView.fxml"));
                loader.setController(this);
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private String onResult(ButtonType buttonType) {
            if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE){
                return campo.get();
            }
            return "";
        }


        @FXML
        private ComboBox<String> fieldComboBox;

        @FXML
        private BorderPane root;

        public BorderPane getRoot() {
            return root;
        }



    }