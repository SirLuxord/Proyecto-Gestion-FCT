package dad.gestion_fct.controllers.empresa;

import dad.gestion_fct.controllers.RootController;
import dad.gestion_fct.models.Empresa;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmpresaController implements Initializable {

    // model

    private ListProperty<Empresa> empresas = new SimpleListProperty<>(FXCollections.observableArrayList());

    // view

    @FXML
    private SplitPane splitEmpresa;

    @FXML
    private BorderPane root;

    @FXML
    private TableColumn<Empresa, String> cpEmpresaColumn;

    @FXML
    private TableColumn<Empresa, String> direccionEmpresaColumn;

    @FXML
    private TableView<Empresa> empresasTable;

    @FXML
    private TableColumn<Empresa, String> localidadEmpresaColumn;

    @FXML
    private TableColumn<Empresa, String> nifColumn;

    @FXML
    private TableColumn<Empresa, String> nombreEmpresaColumn;

    @FXML
    private TableColumn<Empresa, Boolean> publicaColumn;


    public EmpresaController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/empresa/empresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // binding

        empresasTable.itemsProperty().bind(empresas);

        // cell values factories

        nifColumn.setCellValueFactory(v -> v.getValue().nifEmpresaProperty());
        nombreEmpresaColumn.setCellValueFactory(v -> v.getValue().nombreProperty());
        direccionEmpresaColumn.setCellValueFactory(v -> v.getValue().dirrecionProperty());
        localidadEmpresaColumn.setCellValueFactory(v -> v.getValue().localidadProperty());
        cpEmpresaColumn.setCellValueFactory(v -> v.getValue().codigoPostalProperty());
        publicaColumn.setCellValueFactory(v -> v.getValue().publicaProperty());

    }

    @FXML
    void onAddEmpresaAction(ActionEvent event) {

    }

    @FXML
    void onDeleteEmpresaAction(ActionEvent event) {

    }

    @FXML
    void onModifyEmpresaAction(ActionEvent event) {

    }

    @FXML
    void onSearchAllEmpresaAction(ActionEvent event) {

    }

    @FXML
    void onSearchEmpresaAction(ActionEvent event) {

    }

    // Getters and Setters

    public BorderPane getRoot() {
        return root;
    }
}
