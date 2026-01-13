package es.esimarket.backend.services;

import es.esimarket.backend.controllers.responses.MessageResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.esimarket.backend.entities.Mensaje;
import es.esimarket.backend.repositories.MensajeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class MensajeService
{
    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private VariosService variosService;

    public Mensaje CrearMensaje(int chat, String uDNI, String texto, LocalDateTime Fecha)
    {
        Mensaje m = new Mensaje(chat,uDNI,Fecha,texto);

        mensajeRepository.save(m);

        return m;
    }

    public List<MessageResponse>mostrar_mensajes(List<Mensaje> m , String dni){

        List<MessageResponse> messagesRes = new ArrayList<>();
        LocalDateTime fechaAct = variosService.ObtenerFecha();
        LocalDateTime fechaAyer = fechaAct.minusDays(1);
        String dia = null;

        for ( Mensaje mess : m ){

            if ( fechaAct.toLocalDate().equals(mess.getFecha().toLocalDate())) dia = "Hoy";
            else if ( fechaAct.toLocalDate().equals(fechaAyer.toLocalDate())) dia = "Ayer";
            else dia = mess.getFechaDia();
            messagesRes.add(new MessageResponse(mess.getId(),mess.getTexto(),mess.getuDNIremitente(),dia,mess.getHoraMin(),null,false));
        }

        return messagesRes;
    }

    /*public ResponseEntity<List<Mensaje>> MostrarChat(int chat)
    {
        List<Mensaje> l = mensajeRepository.findByid_IDChat(chat);
        
        l= JdbcTemplate
        
    }*/


    public Boolean ContienePalabrasProhibidas(String txt)
    {
        List<String> PalabrasProhibidas = List.of("idiota", "imbecil", "imbécil", "estupido", "estupido", "tonto", "tarado", "bobo", "burro",
        "pendejo", "boludo", "pelotudo", "gilipollas", "cretino", "zopenco", "menso", "baboso", "inutil", 
        "inútil", "bastardo", "bastarda", "descerebrado", "payaso", "zangano", "zángano",
        "huevon", "huevón", "mamon", "mamón", "cabrón", "cabron", "puto", "puta", "putita", 
        "pedorro", "pedorra", "imbecil", "tarugo", "subnormal", "desgraciado", "infeliz", "pobre diablo",
        "despojo", "gentuza", "escoria", "basura", "degenerado", "depravado", "asqueado", "asqueroso", "asquerosa",
        "chupapollas", "maricon", "maricón", "marica", "traga leche", "tragaleche", "culiao", "culero",
        "chingar", "chinga", "chingado", "chupasangre", "culiado", "cagar", "cagado", "zorra", "perra",
        "cabrona", "zorra", "malparido", "malparida", "ratahumana","rata", "prostituta", "ramera",
        "polla", "falo", "pito", "verga", "pija", "cuca","follar", "chocho", "concha", "clitoris", "clítoris",
        "tetas", "pezones", "nalgas", "culo", "ortiga", "ortiga", "madre", "putamadre", "hijodeputa",
        "hijodeperra", "sucia", "apestado", "apestado", "apestoso", "sarnoso", "traidor", "cerdo",
        "chancho", "porqueria", "porquería", "excremento", "mierda", "caca", "mojón", "mierdero", "furcia",
        "gonorrea", "maldito", "maldita", "pajero", "onanista", "lamebotas", "lamesuelas", "malnacido", "malnacida",
        "chupacabras", "anoréxica", "anorexica", "bulímica", "gorda", "gordo", "obeso", "fea", "feo",
        "montonera", "escualido", "escualida", " gremlin", "muertodehambre", "puerco", "zafio", 
        "nini", "pelele", "anoréxica", "anoréxico", "flacucho", "caradeculo","culo", "culoroto", "malparido",
        "expulsado", "nini", "mostruo", "pelmazo", "tarugo", "pelma", "papanatas", "cretina",
        "baboso", "rastrero", "malagradecido", "asno", "tarugo", "payasa", "gil", "jumento",
        "gilún", "come mierda", "transero", "inepto", "payaso", "zángano",
        "cobarde", "malviviente", "tarambana", "botarate", "pelafustán", "alcornoque",
        "descerebrada", "apestoso", "cuasimodo", "espantajo", "escuálido", "cretina",
        "tunante", "vergonzoso", "panduro", "gandul", "patán", "patana", "bagazo", "bagasa",
        "guasón", "tarambana", "chiflado", "freakie", "gordo apestoso", "narizón", "feo de mierda", 
        "drogado", "merluzo", "pelagatos", "cachalote", "mongolo", "mongólica", "mogólico",
        "zoquete", "bestia", "pegadito", "joputa", "gilún", "papafrita", "patético",
        "pedorrete", "babieca", "merluzo", "atontado", "malcriado", "infeliz", "ahuevonado",
        "descerebrado", "demente", "energúmeno", "retrasado", "deforme", "bizco", "maloliente",
        "babucha", "pedorreta", "mariposón", "tralalá", "lagarta", "celópata", "hijueputa",
        "culicagado", "zángano", "garrapata", "carroña", "rufián", "malviviente", "mosca muerta",
        "tarambana", "beodo", "borrachín", "vaguete", "proxeneta", "macarra", "pederasta",
        "necio", "tarugo", "huachafo", "putañero", "tragasables", "maldito sea", 
        "cagaprisas", "gilipicha", "dedosucio", "orinador", "periquero", "tragón", "malfollado",
        "ojete", "popó", "puerca", "cerda", "percherón", "inmundo");

        String textoEnMinúsculas = txt.toLowerCase();

        return PalabrasProhibidas.stream().anyMatch(palabra -> {
            String regex = "\\b" + Pattern.quote(palabra.toLowerCase()) + "\\b";
            return Pattern.compile(regex).matcher(textoEnMinúsculas).find();
        });

    }



}