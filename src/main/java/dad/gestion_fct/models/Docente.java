package dad.gestion_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Docente {

    private final IntegerProperty idDocente = new SimpleIntegerProperty();
    private final StringProperty nombreDocente = new SimpleStringProperty();
    private final StringProperty apellidoDocente = new SimpleStringProperty();
    private final StringProperty emailDocente = new SimpleStringProperty();
    private final StringProperty telefonoDocente = new SimpleStringProperty();

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

    public String getEmailDocente() {
        return emailDocente.get();
    }

    public StringProperty emailDocenteProperty() {
        return emailDocente;
    }

    public void setEmailDocente(String emailDocente) {
        this.emailDocente.set(emailDocente);
    }

    public String getTelefonoDocente() {
        return telefonoDocente.get();
    }

    public StringProperty telefonoDocenteProperty() {
        return telefonoDocente;
    }

    public void setTelefonoDocente(String telefonoDocente) {
        this.telefonoDocente.set(telefonoDocente);
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

    @Override
    public String toString() {
        return getNombreDocente();
    }
}
