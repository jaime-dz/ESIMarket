package es.esimarket.backend.entities;
import jakarta.persistence.*;


@Entity
@Table(name="FotoProd")
public class FotoProd {

    @Id
    @Column(name = "IdProd", nullable = false)
    private int idProd;

    @Lob
    @Column(name="Foto", columnDefinition = "MEDIUMBLOB")
    private byte[] foto;

    public FotoProd(){}

    public FotoProd(int idProd, byte[] foto) {
        this.idProd = idProd;
        this.foto = foto;
    }

    public int getIdProd() {return idProd;}
    public void setIdProd(int idProd) {this.idProd = idProd;}

    public byte[] getFoto() {return foto;}
    public void setFoto(byte[] foto) {this.foto = foto;}

}
