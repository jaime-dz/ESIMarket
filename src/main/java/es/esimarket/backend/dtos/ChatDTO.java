package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Usuario;

import java.io.Serializable;
import java.util.Arrays;

public class ChatDTO implements Serializable {

    private int id;
    private byte[] foto;
    private String nombreProducto;
    private String nombreUsu;
    private String apellidosUsu;
    private Usuario.Carrera carreraUsu;
    private boolean isOwner;

    public ChatDTO() {}

    public ChatDTO(int id, byte[] foto, String nombreProducto, String nombreVendedor, String apellidosVendedor, Usuario.Carrera carreraVendedor, boolean isOwner) {
        this.id = id;
        this.foto = foto;
        this.nombreProducto = nombreProducto;
        this.nombreUsu = nombreVendedor;
        this.apellidosUsu = apellidosVendedor;
        this.carreraUsu = carreraVendedor;
        this.isOwner = isOwner;
    }

    public int getId() {return id;}
    public byte[] getFoto() {return foto;}
    public String getNombreProducto() {return nombreProducto;}
    public String getNombreUsu() {return nombreUsu;}
    public String getApellidosUsu() {return apellidosUsu;}
    public Usuario.Carrera getCarreraUsu() {return carreraUsu;}
    public boolean isOwner() {return isOwner;}

    public void setId(int id) {this.id = id;}
    public void setFoto(byte[] foto) {this.foto = foto;}
    public void setNombreProducto(String nombreProducto) {this.nombreProducto = nombreProducto;}
    public void setNombreUsu(String nombreUsu) {this.nombreUsu = nombreUsu;}
    public void setApellidosUsu(String apellidosUsu) {this.apellidosUsu = apellidosUsu;}
    public void setCarreraUsu(Usuario.Carrera carreraVEndedor) {this.carreraUsu = carreraVEndedor;}
    public void setOwner(boolean owner) {isOwner = owner;}

    @Override
    public String toString() {
        return "ChatDTO{" +
                "id=" + id +
                ", foto=" + Arrays.toString(foto) +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", nombreUsu='" + nombreUsu + '\'' +
                ", apellidosUsu='" + apellidosUsu + '\'' +
                ", carreraUsu=" + carreraUsu +
                ", isOwner=" + isOwner +
                '}';
    }
}
