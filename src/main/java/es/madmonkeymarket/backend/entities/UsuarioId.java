package es.madmonkeymarket.backend.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class UsuarioId implements Serializable {

    @Column(name="correo")
    private String correo;

    @Column(name="contrasenna")
    private String contrasenna;

    public UsuarioId() {}

    public UsuarioId(String correo, String contrasenna) {
        this.correo = correo;
        this.contrasenna = contrasenna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioId that)) return false;
        return Objects.equals(correo, that.correo) &&
                Objects.equals(contrasenna, that.contrasenna);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correo, contrasenna);
    }

}
