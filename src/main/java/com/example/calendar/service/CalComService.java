package com.example.calendar.service;

import com.example.calendar.dto.AttendeeDTO;
import com.example.calendar.dto.CalComRequisicaoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CalComService {

    @Value("${calcom.api.key}")
    private String apiKey;

    public String criarAgendamento(String nome, String email, String inicio){
        RestTemplate restTemplate = new RestTemplate();

        String endpoint = "https://api.cal.com/v2/bookings";

        AttendeeDTO cliente = new AttendeeDTO();
        cliente.setName(nome);
        cliente.setEmail(email);

        CalComRequisicaoDTO agendamento = new CalComRequisicaoDTO();
        agendamento.setEventTypeId(5179222);
        agendamento.setStart(inicio);
        agendamento.setAttendee(cliente);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("cal-api-version", "2026-02-25");
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<CalComRequisicaoDTO> entity = new HttpEntity<>(agendamento, headers);

        try{
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);
            return "Sucesso no agendamento utilizando a versão v2 do cal.com " + response.getStatusCode();
        } catch (Exception e) {
            return "Ocorreu um erro na integração com o cal.com" + e.getMessage();
        }
    }
}
