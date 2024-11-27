package dad.gestion_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ContactoEmp {
    private final IntegerProperty idContacto = new SimpleIntegerProperty();
    private final IntegerProperty idEmpresa = new SimpleIntegerProperty();
    private final StringProperty nombreContacto = new SimpleStringProperty();;
    private final StringProperty apellidoContacto = new SimpleStringProperty();;
    private final StringProperty telefono = new SimpleStringProperty();;
    private final StringProperty correoContacto = new SimpleStringProperty();
    private final StringProperty nombreEmpresa = new SimpleStringProperty();


    public String getNombreContacto() {
        return nombreContacto.get();
    }

    public StringProperty nombreContactoProperty() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto.set(nombreContacto);
    }

    public String getApellidoContacto() {
        return apellidoContacto.get();
    }

    public StringProperty apellidoContactoProperty() {
        return apellidoContacto;
    }

    public void setApellidoContacto(String apellidoContacto) {
        this.apellidoContacto.set(apellidoContacto);
    }

    public String getTelefono() {
        return telefono.get();
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public String getCorreoContacto() {
        return correoContacto.get();
    }

    public StringProperty correoContactoProperty() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto.set(correoContacto);
    }

    public int getIdContacto() {
        return idContacto.get();
    }
    public void setIdContacto(int idContacto) {
        this.idContacto.set(idContacto);
    }

    public IntegerProperty idContactoProperty() {
        return idContacto;
    }

    public IntegerProperty idEmpresaProperty() {
        return idEmpresa;
    }


    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa.set(idEmpresa);
    }

    public int getIdEmpresa() {
        return idEmpresa.get();
    }


    public String getNombreEmpresa() {
        return nombreEmpresa.get();
    }

    public StringProperty nombreEmpresaProperty() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa.set(nombreEmpresa);
    }
}
