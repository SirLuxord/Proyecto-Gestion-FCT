package dad.gestion_fct.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Empresa {
    private final StringProperty idEmpresa = new SimpleStringProperty();
    private final StringProperty nifEmpresa = new SimpleStringProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty dirrecion = new SimpleStringProperty();
    private final StringProperty localidad = new SimpleStringProperty();
    private final StringProperty codigoPostal = new SimpleStringProperty();
    private final BooleanProperty publica = new SimpleBooleanProperty();

    public String getIdEmpresa() {
        return idEmpresa.get();
    }

    public StringProperty idEmpresaProperty() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa.set(idEmpresa);
    }

    public String getNifEmpresa() {
        return nifEmpresa.get();
    }

    public StringProperty nifEmpresaProperty() {
        return nifEmpresa;
    }

    public void setNifEmpresa(String nifEmpresa) {
        this.nifEmpresa.set(nifEmpresa);
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getDirrecion() {
        return dirrecion.get();
    }

    public StringProperty dirrecionProperty() {
        return dirrecion;
    }

    public void setDirrecion(String dirrecion) {
        this.dirrecion.set(dirrecion);
    }

    public String getLocalidad() {
        return localidad.get();
    }

    public StringProperty localidadProperty() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad.set(localidad);
    }

    public String getCodigoPostal() {
        return codigoPostal.get();
    }

    public StringProperty codigoPostalProperty() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal.set(codigoPostal);
    }

    public boolean isPublica() {
        return publica.get();
    }

    public BooleanProperty publicaProperty() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica.set(publica);
    }
}
