package dad.gestion_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ContactoEmp {
    private final StringProperty nombreContacto = new SimpleStringProperty();;
    private final StringProperty apellidoContacto = new SimpleStringProperty();;
    private final StringProperty telefonoContacto = new SimpleStringProperty();;
    private final StringProperty correoContacto = new SimpleStringProperty();
    private final IntegerProperty idContacto = new SimpleIntegerProperty();

/*    public ContactoEmp() {
        this.nombreContacto = new SimpleStringProperty();
        this.apellidoContacto = new SimpleStringProperty();
        this.telefonoContacto = new SimpleStringProperty();
        this.correoContacto = new SimpleStringProperty();
        //this.idContacto = new SimpleIntegerProperty();
    }  */

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

    public String getTelefonoContacto() {
        return telefonoContacto.get();
    }

    public StringProperty telefonoContactoProperty() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto.set(telefonoContacto);
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
}
