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
    private String UDNIcomprador;

    @Column(name = "uDNIvendedor", nullable=false)
    private String UDNIvendedor;

    @Column(name = "IdProducto", nullable = false)
    private int IdProducto;


    public Chat() {}

    public Chat( String DNIcomprador, String DNIvendedor ,int IdProducto)
    {
        this.UDNIcomprador = DNIcomprador;
        this.UDNIvendedor = DNIvendedor;
        this.IdProducto = IdProducto;
    }

    public Integer getId() {return id;}
    public void SetId(Integer id) {this.id = id;}

    public String getuDNIcomprador() {return UDNIcomprador;}
    public void setuDNIcomprador(String uDNIcomprador) {this.UDNIcomprador = uDNIcomprador;}

    public String getUDNIvendedor() {return UDNIvendedor;}
    public void setUDNIvendedor(String UDNIvendedor) {this.UDNIvendedor = UDNIvendedor;}

    public int getIdProducto() {return IdProducto;}
    public void setIdProducto(int IdProducto) {this.IdProducto = IdProducto;}


}