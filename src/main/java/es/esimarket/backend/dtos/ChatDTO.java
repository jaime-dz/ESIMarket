package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Usuario;

import java.io.Serializable;

public class ChatDTO implements Serializable {

    private int id;
    private String nombreProducto;
    private String nombreVendedor;
    private String apellidosVendedor;
    private Usuario.Carrera carreraVendedor;

    public ChatDTO() {}

    public ChatDTO(int id, String nombreProducto, String nombreVendedor, String apellidosVendedor, Usuario.Carrera carreraVendedor) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.nombreVendedor = nombreVendedor;
        this.apellidosVendedor = apellidosVendedor;
        this.carreraVendedor = carreraVendedor;
    }

    public int getId() {return id;}
    public String getNombreProducto() {return nombreProducto;}
    public String getNombreVendedor() {return nombreVendedor;}
    public String getApellidosVendedor() {return apellidosVendedor;}
    public Usuario.Carrera getCarreraVendedor() {return carreraVendedor;}


    public void setId(int id) {this.id = id;}
    public void setNombreProducto(String nombreProducto) {this.nombreProducto = nombreProducto;}
    public void setNombreVendedor(String nombreVendedor) {this.nombreVendedor = nombreVendedor;}
    public void setApellidosVendedor(String apellidosVendedor) {this.apellidosVendedor = apellidosVendedor;}
    public void setCarreraVendedor(Usuario.Carrera carreraVEndedor) {this.carreraVendedor = carreraVEndedor;}

    @Override
    public String toString() {
        return "ChatDTO{" +
                "id=" + id +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", nombreVendedor='" + nombreVendedor + '\'' +
                ", apellidosVendedor='" + apellidosVendedor + '\'' +
                ", carreraVendedor=" + carreraVendedor +
                '}';
    }

}
