package dad.gestion_fct.models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Visita {

    private ObjectProperty<LocalDate> fechaVisita = new SimpleObjectProperty<>();
    private IntegerProperty idAlumno = new SimpleIntegerProperty();
    private StringProperty nombreAlumno = new SimpleStringProperty();
    private StringProperty apellidoAlumno = new SimpleStringProperty();
    private IntegerProperty idDocente = new SimpleIntegerProperty();
    private StringProperty nombreDocente = new SimpleStringProperty();
    private StringProperty apellidoDocente = new SimpleStringProperty();
    private StringProperty observacion = new SimpleStringProperty();

    public Visita() {
    }

    public LocalDate getFechaVisita() {
        return fechaVisita.get();
    }

    public ObjectProperty<LocalDate> fechaVisitaProperty() {
        return fechaVisita;
    }

    public void setFechaVisita(LocalDate fechaVisita) {
        this.fechaVisita.set(fechaVisita);
    }

    public String getNombreAlumno() {
        return nombreAlumno.get();
    }

    public StringProperty nombreAlumnoProperty() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno.set(nombreAlumno);
    }

    public String getApellidoAlumno() {
        return apellidoAlumno.get();
    }

    public StringProperty apellidoAlumnoProperty() {
        return apellidoAlumno;
    }

    public void setApellidoAlumno(String apellidoAlumno) {
        this.apellidoAlumno.set(apellidoAlumno);
    }

    public String getNombreDocente() {
        return nombreDocente.get();
    }

    public StringProperty nombreDocenteProperty() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente.set(nombreDocente);
    }

    public String getApellidoDocente() {
        return apellidoDocente.get();
    }

    public StringProperty apellidoDocenteProperty() {
        return apellidoDocente;
    }

    public void setApellidoDocente(String apellidoDocente) {
        this.apellidoDocente.set(apellidoDocente);
    }

    public String getObservacion() {
        return observacion.get();
    }

    public StringProperty observacionProperty() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion.set(observacion);
    }

    public int getIdAlumno() {
        return idAlumno.get();
    }

    public IntegerProperty idAlumnoProperty() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno.set(idAlumno);
    }

    public int getIdDocente() {
        return idDocente.get();
    }

    public IntegerProperty idDocenteProperty() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente.set(idDocente);
    }
}
