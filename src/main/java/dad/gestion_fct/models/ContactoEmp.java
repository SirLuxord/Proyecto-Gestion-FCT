package dad.gestion_fct.models;

import javafx.beans.property.StringProperty;

public class ContactoEmp {
    private StringProperty nombreContacto;
    private StringProperty apellidoContacto;
    private StringProperty telefonoContacto;
    private StringProperty correoContacto;

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
}
