package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.servizi;

import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoFieraRepository;
import it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.repositories.EventoVisitaRepository;
import org.springframework.stereotype.Service;

@Service
public class EventoService {
    private final EventoFieraRepository fieraRepository;
    private final EventoVisitaRepository visitaRepository;

    public EventoService(EventoFieraRepository fieraRepository, EventoVisitaRepository visitaRepository) {
        this.fieraRepository = fieraRepository;
        this.visitaRepository = visitaRepository;
    }

}
