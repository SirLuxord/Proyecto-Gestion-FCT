package dad.gestion_fct.controllers.plazasEmpresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.controllers.tutorEmpresa.TutorEmpresaController;
import dad.gestion_fct.models.Ciclos;
import dad.gestion_fct.models.Empresa;
import dad.gestion_fct.models.PlazasEmpresa;
import dad.gestion_fct.models.TutorEmpresa;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PlazasEmpresaModifyController implements Initializable {

    // controller

    private final PlazasEmpresaController plazasEmpresaController;

    // model

    private ObjectProperty<PlazasEmpresa> plaza = new SimpleObjectProperty<>(new PlazasEmpresa());
    private String nombreCicloAnterior;
    private int idEmpresaAntiguo;

    // view

    @FXML
    private ComboBox<Ciclos> cicloCombo;

    @FXML
    private ComboBox<Empresa> empresaCombo;

    @FXML
    private TextField numeroPlazasField;

    @FXML
    private BorderPane root;

    public PlazasEmpresaModifyController(PlazasEmpresaController plazasEmpresaController){
        try{
            this.plazasEmpresaController = plazasEmpresaController;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plazasEmpresa/modifyPlazasEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // bindings

        empresaCombo.getSelectionModel().selectedItemProperty().addListener((o , ov ,nv) -> {
            if (nv != null){
                plaza.get().setIdEmpresa(nv.getIdEmpresa());
                plaza.get().setNombreEmpresa(nv.getNombre());
            }
        });

        cicloCombo.getSelectionModel().selectedItemProperty().addListener((o , ov ,nv) -> {
            if (nv != null){
                plaza.get().setNombreCiclo(nv.toString());
            }
        });

        // populate comboBox
        cicloCombo.getItems().setAll(Ciclos.values());

        numeroPlazasField.textProperty().bindBidirectional(plaza.get().numeroPlazasProperty() , new NumberStringConverter());
    }


    @FXML
    void onCancelAction(ActionEvent event) {
        plazasEmpresaController.getSplitPlazas().getItems().remove(getRoot());
    }

    @FXML
    void onModifyAction(ActionEvent event) {

        String query = "Update PlazasEmpresas set NombreCiclo = ?, IdEmpresa = ?, NumeroPlazas = ? where NombreCiclo = ? and IdEmpresa = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1 , plaza.get().getNombreCiclo());
            statement.setInt(2 , plaza.get().getIdEmpresa());
            statement.setInt(3 , plaza.get().getNumeroPlazas());
            statement.setString(4 , nombreCicloAnterior);
            statement.setInt(5 , idEmpresaAntiguo);

            statement.execute();

            // se le pasa al controlador de empresa los datos del registro seleccionado

            plazasEmpresaController.getPlazaSeleccionada().setIdEmpresa(plaza.get().getIdEmpresa());
            plazasEmpresaController.getPlazaSeleccionada().setNombreEmpresa(plaza.get().getNombreEmpresa());
            plazasEmpresaController.getPlazaSeleccionada().setNombreCiclo(plaza.get().getNombreCiclo());
            plazasEmpresaController.getPlazaSeleccionada().setNumeroPlazas(plaza.get().getNumeroPlazas());

            cicloCombo.getSelectionModel().select(null); // se resetea la selección del combo box

            plazasEmpresaController.getSplitPlazas().getItems().remove(getRoot());

        }  catch (SQLException e) {
            plazasEmpresaController.mostrarError(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

    }

    public BorderPane getRoot() {
        return root;
    }

    public ComboBox<Empresa> getEmpresaCombo() {
        return empresaCombo;
    }

    public ComboBox<Ciclos> getCicloCombo() {
        return cicloCombo;
    }

    public void setEmpresaCombo(ComboBox<Empresa> empresaCombo) {
        this.empresaCombo = empresaCombo;
    }

    public PlazasEmpresa getPlaza() {
        return plaza.get();
    }

    public ObjectProperty<PlazasEmpresa> plazaProperty() {
        return plaza;
    }

    public void setPlaza(PlazasEmpresa plaza) {
        this.plaza.set(plaza);
    }

    public void setNombreCicloAnterior(String nombreCicloAnterior) {
        this.nombreCicloAnterior = nombreCicloAnterior;
    }

    public void setIdEmpresaAntiguo(int idEmpresaAntiguo) {
        this.idEmpresaAntiguo = idEmpresaAntiguo;
    }
}
