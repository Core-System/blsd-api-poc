package com.example.calendar.service;

import com.example.calendar.dto.AttendeeDTO;
import com.example.calendar.dto.CalComRequisicaoDTO;
import com.example.calendar.entity.Cliente;
import com.example.calendar.entity.Consulta;
import com.example.calendar.repositories.ClienteRepository;
import com.example.calendar.repositories.ConsultaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@Service
public class CalComService {

    @Value("${calcom.api.key}")
    private String apiKey;
    private final ConsultaRepository consultaRepository;
    private final ClienteRepository clienteRepository;

    public CalComService(ConsultaRepository consultaRepository, ClienteRepository clienteRepository) {
        this.consultaRepository = consultaRepository;
        this.clienteRepository = clienteRepository;
    }

    public String criarAgendamento(String nome, String email, String inicio){
        RestTemplate restTemplate = new RestTemplate();

        String endpoint = "https://api.cal.com/v2/bookings";

        AttendeeDTO cliente = new AttendeeDTO();
        cliente.setName(nome);
        cliente.setEmail(email);

        CalComRequisicaoDTO agendamento = new CalComRequisicaoDTO();
        agendamento.setEventTypeId(5180374);
        agendamento.setStart(inicio);
        LocalDateTime dataHoraInicio = LocalDateTime.parse(inicio);
        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(60);
        agendamento.setAttendee(cliente);

        Cliente clienteMock = clienteRepository.findByEmail(email);

        Consulta consulta = new Consulta();
        consulta.setDataHoraInicio(dataHoraInicio);
        consulta.setDataHoraFim(dataHoraFim);
        consulta.setCliente(clienteMock);
        consultaRepository.save(consulta);

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
