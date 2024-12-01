package dad.gestion_fct.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlazasEmpresa {

    private StringProperty nombreCiclo = new SimpleStringProperty();
    private IntegerProperty idEmpresa = new SimpleIntegerProperty();
    private StringProperty nombreEmpresa = new SimpleStringProperty();
    private IntegerProperty numeroPlazas = new SimpleIntegerProperty();

    public String getNombreCiclo() {
        return nombreCiclo.get();
    }

    public StringProperty nombreCicloProperty() {
        return nombreCiclo;
    }

    public void setNombreCiclo(String nombreCiclo) {
        this.nombreCiclo.set(nombreCiclo);
    }

    public int getIdEmpresa() {
        return idEmpresa.get();
    }

    public IntegerProperty idEmpresaProperty() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa.set(idEmpresa);
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

    public int getNumeroPlazas() {
        return numeroPlazas.get();
    }

    public IntegerProperty numeroPlazasProperty() {
        return numeroPlazas;
    }

    public void setNumeroPlazas(int numeroPlazas) {
        this.numeroPlazas.set(numeroPlazas);
    }
}
