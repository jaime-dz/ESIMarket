package es.esimarket.backend.dtos;
import es.esimarket.backend.entities.Donaciones;

import java.io.Serializable;

public class DonacionesDTO implements Serializable {

    private int Num;

    public DonacionesDTO() {}

    public DonacionesDTO( Donaciones donaciones ) {
        this.Num = donaciones.getNum();
    }

    public DonacionesDTO(int num) {Num = num;}

    public int getNum() {return Num;}

    public void setNum(int num) {Num = num;}

    @Override
    public String toString() {
        return "DonacionesDTO{" +
                "Num=" + Num +
                '}';
    }

}
