package dad.gestion_fct.controllers.ComentariosEmpresa;

import dad.gestion_fct.HikariConnection;
import dad.gestion_fct.models.ComentariosEmpresa;
import dad.gestion_fct.models.Empresa;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ComentariosEmpresaController implements Initializable {

    ComentariosEmpresaModifyController comentariosEmpresaModifyController = new ComentariosEmpresaModifyController(this);
    private ListProperty<ComentariosEmpresa> comentariosEmpresa = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<ComentariosEmpresa> selectedComentariosEmpresa = new SimpleObjectProperty<>();
    private ListProperty<Empresa> empresas = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ComentariosEmpresa getSelectedComentariosEmpresa() {
        return selectedComentariosEmpresa.get();
    }

    public ObjectProperty<ComentariosEmpresa> selectedComentariosEmpresaProperty() {
        return selectedComentariosEmpresa;
    }

    public void setSelectedComentariosEmpresa(ComentariosEmpresa selectedComentariosEmpresa) {
        this.selectedComentariosEmpresa.set(selectedComentariosEmpresa);
    }

    @FXML
    private TableColumn<ComentariosEmpresa, String> comentariosColumn;

    @FXML
    private TableView<ComentariosEmpresa> comentariosEmpresaTable;


    @FXML
    private Button createButton;

    @FXML
    private TableColumn<ComentariosEmpresa, String> fechaComentarioColumn;

    @FXML
    private TableColumn<ComentariosEmpresa, String> nombreDocenteColumn;

    @FXML
    private TableColumn<ComentariosEmpresa, String> telefonoDocenteColumn;

    @FXML
    private TableColumn<ComentariosEmpresa, String> nombreEmpresaColumn;


    @FXML
    private Button modifyButton;


    @FXML
    private Button removeButton;

    @FXML
    private BorderPane root;

    @FXML
    private Button searchAllButton;

    @FXML
    private Button searchButton;

    @FXML
    private SplitPane splitComentariosEmpresa;


    public ComentariosEmpresaController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/comentariosEmpresa/comentariosEmpresaView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onAddAction(ActionEvent event) {
        ComentariosEmpresaCreateDialog dialog = new ComentariosEmpresaCreateDialog();
        seleccionarEmpresas();
        dialog.getEmpresaCombo().getItems().setAll(empresas);
        Optional<ComentariosEmpresa> result = dialog.showAndWait();

        if (result.isPresent()) {
            ComentariosEmpresa comentario = result.get();
            comentario.setIdDocente(buscarIdDocente(comentario.getTelefonoDocente()));

            // Check for empty or null values before trimming
            if (comentario.getNombreDocente().trim().isEmpty()) {
                mostrarAlertaError("Nombre sin introducir");
                throw new IllegalArgumentException("Nombre sin introducir");
            }

            // Check for null or empty telefonoDocente
            if (comentario.getTelefonoDocente() == null || comentario.getTelefonoDocente().trim().isEmpty()) {
                mostrarAlertaError("Telefono sin introducir");
                throw new IllegalArgumentException("Telefono sin introducir");
            }

            if (comentario.getComentarios().trim().isEmpty()) {
                mostrarAlertaError("Comentarios sin introducir");
                throw new IllegalArgumentException("Comentarios sin introducir");
            }

            String query = "Insert into ComentariosCaptacionEmpresa (FechaComentario, IdEmpresa, IdDocente, Comentarios) VALUES (?, ?, ?, ?)";
            try (Connection connection = HikariConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setDate(1, Date.valueOf(comentario.getFechaComentario()));
                statement.setInt(2, comentario.getIdEmpresa());
                statement.setInt(3, comentario.getIdDocente());

                statement.setString(4, comentario.getComentarios());

                statement.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            comentariosEmpresa.add(comentario);
            onSearchAllAction();
        }
    }

    private void seleccionarEmpresas() {
        empresas.setAll(FXCollections.observableArrayList());
        String query = "SELECT IdEmpresa, NombreEmpresa FROM Empresa";

        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Iteramos sobre los resultados para crear los objetos Empresa
            while (resultSet.next()) {
                Empresa empresa = new Empresa();
                empresa.setIdEmpresa(resultSet.getInt("IdEmpresa"));
                empresa.setNombre(resultSet.getString("NombreEmpresa"));

                // Añadimos la empresa a la lista
                empresas.add(empresa);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al cargar las empresas");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        SplitPane.setResizableWithParent(comentariosEmpresaModifyController.getRoot(), false);
        onSearchAllAction();

        modifyButton.setDisable(true);
        removeButton.setDisable(true);

        comentariosEmpresaTable.itemsProperty().bind(comentariosEmpresa);
        selectedComentariosEmpresa.bind(comentariosEmpresaTable.getSelectionModel().selectedItemProperty());
        comentariosEmpresaTable.disableProperty().bind(Bindings.createBooleanBinding(this::onSplitPaneChanged, splitComentariosEmpresa.getItems()));
        comentariosEmpresaModifyController.getEmpresaCombo().itemsProperty().bind(empresas);

        selectedComentariosEmpresa.addListener((o, ov, nv) -> {
            modifyButton.setDisable(nv == null);
            removeButton.setDisable(nv == null);
        });

        comentariosEmpresaTable.disableProperty().addListener((o, ov, nv) -> {
            modifyButton.setDisable(nv);
            removeButton.setDisable(nv);
            createButton.setDisable(nv);
            searchButton.setDisable(nv);
            searchAllButton.setDisable(nv);
        });
        comentariosColumn.setCellValueFactory(v -> v.getValue().comentariosProperty());
        fechaComentarioColumn.setCellValueFactory(v -> v.getValue().fechaComentarioProperty().asString());
        nombreEmpresaColumn.setCellValueFactory(v -> v.getValue().nombreEmpresaProperty());
        nombreDocenteColumn.setCellValueFactory(v -> v.getValue().nombreDocenteProperty());
        telefonoDocenteColumn.setCellValueFactory(v -> v.getValue().telefonoDocenteProperty());
    }



    @FXML
    void onModifyAction(ActionEvent event) {
        seleccionarEmpresas();

        comentariosEmpresaModifyController.setComentario(new ComentariosEmpresa());
        comentariosEmpresaModifyController.getComentario().setIdComentario(selectedComentariosEmpresa.get().getIdComentario());
        comentariosEmpresaModifyController.getComentario().setIdEmpresa(selectedComentariosEmpresa.get().getIdEmpresa());
        comentariosEmpresaModifyController.getComentario().setIdDocente(selectedComentariosEmpresa.get().getIdDocente());
        comentariosEmpresaModifyController.getComentario().setComentarios(selectedComentariosEmpresa.get().getComentarios());
        comentariosEmpresaModifyController.getComentario().setNombreDocente(selectedComentariosEmpresa.get().getNombreDocente());
        comentariosEmpresaModifyController.getComentario().setTelefonoDocente(selectedComentariosEmpresa.get().getTelefonoDocente());
        comentariosEmpresaModifyController.getComentario().setNombreEmpresa(selectedComentariosEmpresa.get().getNombreEmpresa());
        comentariosEmpresaModifyController.getComentario().setFechaComentario(selectedComentariosEmpresa.get().getFechaComentario());
        splitComentariosEmpresa.getItems().add(comentariosEmpresaModifyController.getRoot());

        selectedComentariosEmpresa.bind(comentariosEmpresaTable.getSelectionModel().selectedItemProperty());
        System.out.println(selectedComentariosEmpresa.get().getIdComentario());
        System.out.println(selectedComentariosEmpresa.get().getIdEmpresa());
        System.out.println(selectedComentariosEmpresa.get().getIdDocente());

    }


    public BorderPane getRoot() {
        return root;
    }

    @FXML
    void onDeleteAction(ActionEvent event) throws SQLException {
        String fechaComentario = String.valueOf(selectedComentariosEmpresa.get().getFechaComentario());
        String query = "Delete from ComentariosCaptacionEmpresa where fechaComentario = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fechaComentario);
            statement.execute();
            comentariosEmpresa.remove(selectedComentariosEmpresa.get());
        } catch (SQLException e) {
            mostrarAlertaError("Error al eliminar comentario");
            throw new RuntimeException(e);
        }

    }


    @FXML
    void onSearchAllAction() {
        comentariosEmpresa.setAll(FXCollections.observableArrayList());
        try (Connection connection = HikariConnection.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT IdComentario, FechaComentario, empresa.IdEmpresa, tutordocente.IdDocente, tutordocente.telefonoDocente, Comentarios, empresa.NombreEmpresa, tutordocente.NombreDocente " +
                    "FROM comentarioscaptacionempresa INNER JOIN empresa ON comentarioscaptacionempresa.IdEmpresa = empresa.IdEmpresa INNER JOIN tutordocente ON comentarioscaptacionempresa.IdDocente = tutordocente.IdDocente";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                ComentariosEmpresa comentario = new ComentariosEmpresa();
                comentario.setFechaComentario(LocalDate.parse(resultSet.getString("FechaComentario")));
                comentario.setIdComentario(resultSet.getInt("IdComentario"));
                comentario.setIdEmpresa(resultSet.getInt("IdEmpresa"));
                comentario.setIdDocente(resultSet.getInt("IdDocente"));
                comentario.setComentarios(resultSet.getString("Comentarios"));
                comentario.setNombreEmpresa(resultSet.getString("NombreEmpresa"));
                comentario.setNombreDocente(resultSet.getString("NombreDocente"));
                comentario.setTelefonoDocente(resultSet.getString("TelefonoDocente"));
                comentariosEmpresa.add(comentario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void onSearchAction(ActionEvent event) {
        ComentariosEmpresaSearchDialog searchDialog = new ComentariosEmpresaSearchDialog();
        Optional<String> campo = searchDialog.showAndWait();
        // Se ejecuta solo si el usuario selecciona un valor válido.
        if (campo.isPresent() && !campo.get().isEmpty()) {
            campo.get();
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Introduzca el " + campo.get());
            nameDialog.setContentText(campo.get() + ": ");
            Optional<String> result = nameDialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    buscarComentarioEmpresa(campo.get(), "%" + value + "%");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private void mostrarAlertaError(String titulo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.showAndWait();
    }

    private void buscarComentarioEmpresa(String s, String s1) throws SQLException {
        comentariosEmpresa.clear();

        String query = obtenerString(s);
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, s1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ComentariosEmpresa comentario = new ComentariosEmpresa();
                comentario.setFechaComentario(LocalDate.parse(resultSet.getString("FechaComentario")));
                // comentario.setIdEmpresa(resultSet.getInt("IdEmpresa"));
                // comentario.setIdDocente(resultSet.getInt("IdDocente"));
                comentario.setComentarios(resultSet.getString("Comentarios"));
                comentario.setNombreEmpresa(resultSet.getString("NombreEmpresa"));
                comentario.setNombreDocente(resultSet.getString("NombreDocente"));

                comentariosEmpresa.add(comentario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //obtener String
    private static String obtenerString(String s) {
        System.out.println(s);

        String c = switch (s) {
            case "Fecha Comentario" -> " WHERE FechaComentario LIKE ?";
            case "Nombre Empresa" -> " WHERE Empresa.NombreEmpresa LIKE ?";
            case "Nombre Docente" -> " WHERE TutorDocente.NombreDocente LIKE ?";
            case "Comentarios" -> " WHERE Comentarios LIKE ?";
            default -> ""; // Valor por defecto, no se agrega un WHERE si no se requiere
        };

        // Consulta principal con WHERE agregado correctamente
        String query = "SELECT FechaComentario, Comentarios, Empresa.NombreEmpresa, TutorDocente.NombreDocente " +
                "FROM ComentariosCaptacionEmpresa " +
                "INNER JOIN Empresa ON ComentariosCaptacionEmpresa.IdEmpresa = Empresa.IdEmpresa " +
                "INNER JOIN TutorDocente ON ComentariosCaptacionEmpresa.IdDocente = TutorDocente.IdDocente" + c;


        return query;
    }

    public int buscarIdDocente(String telefono) {
        String query = "SELECT IdDocente FROM TutorDocente WHERE TelefonoDocente = ?";
        try (Connection connection = HikariConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, telefono);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("IdDocente");
            } else {
                mostrarAlertaError("Docente no encontrado para el teléfono ");
                throw new SQLException("Docente no encontrado para el teléfono: " + telefono);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al obtener IdDocente ");

            throw new RuntimeException("Error al obtener IdDocente");
        }
    }



    private Boolean onSplitPaneChanged() {
        if (splitComentariosEmpresa.getItems().size() == 2) {
            return true;
        }
        return false;
    }

    public SplitPane getSplitComentariosEmpresa() {
        return splitComentariosEmpresa;
    }

}