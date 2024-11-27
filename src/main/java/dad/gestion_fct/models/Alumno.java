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
    private final StringProperty tutorEmpresa = new SimpleStringProperty();
    private IntegerProperty idDocente = new SimpleIntegerProperty();
    private IntegerProperty idTutor = new SimpleIntegerProperty();

    public Alumno() {
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

    public String getTutorEmpresa() {return tutorEmpresa.get(); }

    public StringProperty tutorEmpresaProperty() { return tutorEmpresa; }

    public void setTutorEmpresa(String tutorEmpresa) { this.tutorEmpresa.set(tutorEmpresa); }

    public int getIdDocente() {
        return idDocente.get();
    }

    public IntegerProperty idDocenteProperty() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente.set(idDocente);
    }

    public int getIdTutor() {
        return idTutor.get();
    }

    public IntegerProperty idTutorProperty() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor.set(idTutor);
    }
}
