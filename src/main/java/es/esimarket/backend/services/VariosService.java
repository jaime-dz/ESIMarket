package es.esimarket.backend.services;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import java.time.ZoneId;

import java.util.*;

@Component
public class VariosService
{

    public static ZoneId zonaHoraria = ZoneId.of("Europe/Madrid");

    public LocalDateTime ObtenerFecha()
    {
        //DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now(zonaHoraria);
    }

    public HashMap<String,String> StringToDictionary(String Cadena)
    {
        HashMap<String,String> Dictionary = new HashMap<String,String>();

        for(String par : Cadena.split(","))
        {
            String[] KeyValue = par.split("=");
            Dictionary.put(KeyValue[0],KeyValue[1]);
        }

        return Dictionary;

    }

}