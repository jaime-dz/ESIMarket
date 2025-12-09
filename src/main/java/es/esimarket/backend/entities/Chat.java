package es.esimarket.backend.entities;
import jakarta.persistence.*;

@Entity
@Table(name="chat")
public class Chat{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable=false)
    private Integer id;

    @Column(name = "uDNIcomprador", nullable=false)
    private String uDNIcomprador;

    @Column(name = "uDNIvendedor", nullable=false)
    private String uDNIvendedor;

    @Column(name = "IdProducto", nullable = false)
    private int IdProducto;


    public Chat() {}

    public Chat( String DNIcomprador, String DNIvendedor ,int IdProducto)
    {
        this.uDNIcomprador = DNIcomprador;
        this.uDNIvendedor = DNIvendedor;
        this.IdProducto = IdProducto;
    }

    public Integer getId() {return id;}
    public void SetId(Integer id) {this.id = id;}

    public String getuDNIcomprador() {return uDNIcomprador;}
    public void setuDNIcomprador(String uDNIcomprador) {this.uDNIcomprador = uDNIcomprador;}

    public String getuDNIvendedor() {return uDNIvendedor;}
    public void setuDNIvendedor(String uDNIvendedor) {this.uDNIvendedor = uDNIvendedor;}

    public int getIdProducto() {return IdProducto;}
    public void setIdProducto(int IdProducto) {this.IdProducto = IdProducto;}


}