package com.example.calendar.controller;

import com.example.calendar.dto.EventRequestDTO;
import com.example.calendar.service.GoogleCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendario")
public class CalendarioController {
    private final GoogleCalendarService calendarService;

    public CalendarioController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/agendar")
    public ResponseEntity<String> criarAgendamento(@RequestBody EventRequestDTO request){
        try{
            String link = calendarService.criarEvento(request.getTitulo(), request.getDescricao(), request.getDataHoraInicio(),
                    request.getDataHoraFim());
            return ResponseEntity.status(200).body("Sucesso! Link do evento: " + link);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Erro: " + e.getMessage());
        }
    }
}
