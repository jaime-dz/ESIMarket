package es.esimarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="chat")
public class Chat{

    @Id
    @Column(name = "ID", nullable=false)
    private Integer id;

    @Column(name = "uDNI1", nullable=false)
    private String uDNI1;

    @Column(name = "uDNI2", nullable=false)
    private String uDNI2;

    @Column(name = "uDNIMenor")
    private String uDNI_Menor;

    @Column(name = "uDNIMayor")
    private String uDNI_Mayor;

    public Chat() {}

    public Chat(Integer id, String dni1, String dni2)
    {
        this.id = id;
        this.uDNI1 = dni1;
        this.uDNI2 = dni2;
    }

    public Integer getID() {return id;}
    public void SetID(Integer id) {this.id = id;}

    public String getuDNI1() {return uDNI1;}
    public void setuDNI1(String uDNI1) {this.uDNI1=uDNI1;}

    public String getuDNI2() {return uDNI2;}
    public void setuDNI2(String uDNI2) {this.uDNI2=uDNI2;}

    public String getuDNI_Menor() {return uDNI_Menor;}
    public void setuDNI_Menor(String uDNI_Menor) {this.uDNI_Menor = uDNI_Menor;}

    public String getuDNI_Mayor() {return uDNI_Mayor;}
    public void setuDNI_Mayor(String uDNI_Mayor) {this.uDNI_Mayor = uDNI_Mayor;}





}