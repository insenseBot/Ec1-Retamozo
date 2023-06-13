package com.example.accessingdatamysql;

import javax.management.ConstructorParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.lang.Object;

@Controller
@RequestMapping(path = "/mascotas")
public class MainController {
  @Autowired
  private MascotaRepository mascotaRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping(path = "/add") // POST http://localhost:8080/mascotas/add
  public @ResponseBody String addNewMascota(@RequestParam String nombre, @RequestParam String raza,
      @RequestParam String propietario) {
    Mascota m = new Mascota();
    m.setNombre(nombre);
    m.setRaza(raza);
    m.setPropietario(propietario);
    mascotaRepository.save(m);
    return "Mascota Saved";
  }

  @GetMapping(path = "/all") // GET http://localhost:8080/mascotas/all
  public @ResponseBody Iterable<Mascota> getAllMascotas() {
    return mascotaRepository.findAll();
  }

  @GetMapping(path = "/ver/{id}") // GET http://localhost:8080/mascotas/ver/id
  public @ResponseBody Mascota getMascota(@PathVariable("id") Integer id) {
    return mascotaRepository.findById(id).orElse(null);
  }

  @PutMapping(path = "/edit") // PUT http://localhost:8080/mascotas/edit
  public @ResponseBody String editMascota(@RequestParam Integer id, @RequestParam String nombre,
      @RequestParam String raza, @RequestParam String propietario) {
    Mascota mascota = mascotaRepository.findById(id).orElse(null);
    if (mascota != null) {
      mascota.setNombre(nombre);
      mascota.setRaza(raza);
      mascota.setPropietario(propietario);
      mascotaRepository.save(mascota);
      return "Mascota Edited";
    }
    return "Mascota Not found";
  }

  @DeleteMapping(path = "/del") // DELETE http://localhost:8080/mascotas/del
  public @ResponseBody String deleteMascota(@RequestParam Integer id) {
    Mascota mascota = mascotaRepository.findById(id).orElse(null);
    if (mascota != null) {
      mascotaRepository.delete(mascota);
      return "Mascota Deleted";
    }
    return "Mascota Not found";
  }

  @GetMapping(path = "/get/report") // GET http://localhost:8080/mascotas/get/report
  public @ResponseBody List getReport() {
    String sql = "SELECT CONCAT(nombre, ' ==> ', raza, '|', propietario) as reporte FROM mascota";
    List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(sql);
    return queryResult;
  }
}