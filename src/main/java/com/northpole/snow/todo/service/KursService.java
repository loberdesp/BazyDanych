package com.northpole.snow.todo.service;

import com.northpole.snow.todo.domain.Kurs;
import com.northpole.snow.todo.domain.KursRepository;
import com.northpole.snow.todo.domain.Trasa;
import com.northpole.snow.todo.domain.TrasaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class KursService {

  private final KursRepository kursRepository;
  private final TrasaRepository trasaRepository;

  public KursService(KursRepository kursRepository, TrasaRepository trasaRepository) {
    this.kursRepository = kursRepository;
    this.trasaRepository = trasaRepository;
  }

  public List<Kurs> findAll() {
    return kursRepository.findAllWithTrasa();
  }

  public boolean addKurs(Integer numerTrasy, LocalTime godzinaStartu) {
    Optional<Trasa> trasaOpt = trasaRepository.findAll().stream()
        .filter(t -> t.getNumertrasy().equals(numerTrasy))
        .findFirst();
    if (trasaOpt.isEmpty())
      return false;

    Kurs kurs = new Kurs();
    kurs.setTrasaid(trasaOpt.get());
    kurs.setGodzinastartu(godzinaStartu);
    kursRepository.save(kurs);
    return true;
  }

  public boolean addKursByTrasaId(Integer trasaId, LocalTime godzinaStartu) {
    Optional<Trasa> trasaOpt = trasaRepository.findById(trasaId);
    if (trasaOpt.isEmpty())
      return false;

    Kurs kurs = new Kurs();
    kurs.setTrasaid(trasaOpt.get());
    kurs.setGodzinastartu(godzinaStartu);
    kursRepository.save(kurs);
    return true;
  }
}
