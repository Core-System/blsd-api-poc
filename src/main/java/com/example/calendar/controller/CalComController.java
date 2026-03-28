package com.example.calendar.controller;

import com.example.calendar.dto.AttendeeDTO;
import com.example.calendar.service.CalComService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendario")
public class CalComController {
    private final CalComService calendarService;

    public CalComController(CalComService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/agendar")
    public ResponseEntity<String> criarAgendamento(@RequestBody AttendeeDTO request){
        try{
            String link = calendarService.criarEvento(request.getTitulo(), request.getDescricao(), request.getDataHoraInicio(),
                    request.getDataHoraFim());
            return ResponseEntity.status(200).body("Sucesso! Link do evento: " + link);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Erro: " + e.getMessage());
        }
    }
}
