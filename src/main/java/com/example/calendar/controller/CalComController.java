package com.example.calendar.controller;

import com.example.calendar.dto.AgendamentoRequestDTO;
import com.example.calendar.dto.AttendeeDTO;
import com.example.calendar.service.CalComService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/calendario")
public class CalComController {
    private final CalComService calendarService;

    public CalComController(CalComService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/calcom/agendar")
    public ResponseEntity<String> agendar(@RequestBody AgendamentoRequestDTO request){
        String resultado = calendarService.criarAgendamento(request.getNome(), request.getEmail(), request.getDataHoraInicio());

        return ResponseEntity.ok(resultado);
    }
}
