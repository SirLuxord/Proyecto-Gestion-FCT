package dad.gestion_fct.models;
import javafx.beans.property.*;

import java.sql.Date;
import java.time.LocalDate;

public class ComentariosEmpresa {
    private final IntegerProperty idComentario = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> fechaComentario = new SimpleObjectProperty<>();
    private final IntegerProperty idEmpresa = new SimpleIntegerProperty();
    private final IntegerProperty IdDocente = new SimpleIntegerProperty();
    private final StringProperty comentarios = new SimpleStringProperty();
    private final StringProperty nombreEmpresa = new SimpleStringProperty();
    private final StringProperty nombreDocente = new SimpleStringProperty();
    private final StringProperty telefonoDocente = new SimpleStringProperty();

    public IntegerProperty idComentarioProperty() {
        return idComentario;
    }

    public String getTelefonoDocente() {
        return telefonoDocente.get();
    }

    public void setTelefonoDocente(String telefonoDocente) {
        this.telefonoDocente.set(telefonoDocente);
    }

    public StringProperty telefonoDocenteProperty() {
        return telefonoDocente;
    }

    public int getIdComentario() { return idComentario.get(); }

    public void setIdComentario(int idComentario) {
        this.idComentario.set(idComentario);
    }

    public LocalDate getFechaComentario() {
        return fechaComentario.get();
    }

    public void setFechaComentario(LocalDate fechaComentario) {
        this.fechaComentario.set(fechaComentario);
    }



    public ObjectProperty<LocalDate> fechaComentarioProperty() {
        return fechaComentario;
    }
    public int getIdEmpresa() {
        return idEmpresa.get();
    }

    public IntegerProperty idEmpresaProperty() {
        return idEmpresa;
    }

    public int getIdDocente() {
        return IdDocente.get();
    }

    public IntegerProperty idDocenteProperty() {
        return IdDocente;
    }

    public String getComentarios() {
        return comentarios.get();
    }

    public StringProperty comentariosProperty() {
        return comentarios;
    }


    public StringProperty nombreEmpresaProperty() {
        return nombreEmpresa;
    }

    public StringProperty nombreDocenteProperty() {
        return nombreDocente;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa.set(nombreEmpresa);
    }




    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente.set(nombreDocente);
    }


    // Setters

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa.set(idEmpresa);
    }

    public void setIdDocente(int idDocente) {
        this.IdDocente.set(idDocente);
    }

    public void setComentarios(String comentarios) {
        this.comentarios.set(comentarios);
    }


    public String getNombreEmpresa() {
        return nombreEmpresa.get();
    }

    public String getNombreDocente() {
        return nombreDocente.get();
    }
}
