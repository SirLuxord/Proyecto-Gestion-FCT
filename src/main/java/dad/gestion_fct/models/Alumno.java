package dad.gestion_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Alumno {

    private final IntegerProperty idAlumno = new SimpleIntegerProperty();
    private final StringProperty cialAlumno = new SimpleStringProperty();
    private final StringProperty nombreAlumno = new SimpleStringProperty();
    private final StringProperty apellidoAlumno = new SimpleStringProperty();
    private final StringProperty cicloAlumno = new SimpleStringProperty();
    private final StringProperty nussAlumno = new SimpleStringProperty();
    private final StringProperty nombreDocente = new SimpleStringProperty();
    private final StringProperty tutorDocente = new SimpleStringProperty();

    public Alumno() {
    }

    public String getCialAlumno() {
        return cialAlumno.get();
    }

    public StringProperty cialAlumnoProperty() {
        return cialAlumno;
    }

    public void setCialAlumno(String cialAlumno) {
        this.cialAlumno.set(cialAlumno);
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

    public String getCicloAlumno() {
        return cicloAlumno.get();
    }

    public StringProperty cicloAlumnoProperty() {
        return cicloAlumno;
    }

    public void setCicloAlumno(String cicloAlumno) {
        this.cicloAlumno.set(cicloAlumno);
    }

    public String getNussAlumno() {
        return nussAlumno.get();
    }

    public StringProperty nussAlumnoProperty() {
        return nussAlumno;
    }

    public void setNussAlumno(String nussAlumno) {
        this.nussAlumno.set(nussAlumno);
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

    public String getTutorDocente() {return tutorDocente.get(); }

    public StringProperty tutorDocenteProperty() { return tutorDocente; }

    public void setTutorDocente(String tutorDocente) { this.tutorDocente.set(tutorDocente); }
}
