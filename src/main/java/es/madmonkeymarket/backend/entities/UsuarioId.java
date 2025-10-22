package es.madmonkeymarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class UsuarioId implements Serializable {

    @Column(name="correo_electronico")
    private String Correo;

    @Column(name="contrasenna")
    private String Contrasenna;

    public UsuarioId() {}

    public UsuarioId(String correo, String contrasenna) {
        this.Correo = correo;
        this.Contrasenna = contrasenna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioId that)) return false;
        return Objects.equals(Correo, that.Correo) &&
                Objects.equals(Contrasenna, that.Contrasenna);
    }
}
