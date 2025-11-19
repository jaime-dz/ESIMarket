package es.esimarket.backend.services;
import com.fasterxml.jackson.core.JsonProcessingException;

import es.esimarket.backend.entities.Usuario;
import es.esimarket.backend.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import es.esimarket.backend.entities.Mensaje;
import es.esimarket.backend.repositories.MensajeRepository;
import es.esimarket.backend.services.VariosService;

import java.util.List;

@Service
public class MensajeService
{
    @Autowired
    private MensajeRepository mensajeRepository;

    public String CrearMensaje(int chat, String uDNI, String texto)
    {
        VariosService v = new VariosService();
        Mensaje m = new Mensaje(chat,uDNI,v.ObtenerFecha(),texto);

        mensajeRepository.save(m);

        return "Mensaje añadido correctamente";
    }

    /*public ResponseEntity<List<Mensaje>> MostrarChat(int chat)
    {
        List<Mensaje> l = mensajeRepository.findByid_IDChat(chat);
        
        l= JdbcTemplate
        
    }*/

    /*
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

        boolean contiene = PalabrasProhibidas.stream().anyMatch(p -> txt.toLowerCase().contains(p));

        return contiene;
    }

    */


}