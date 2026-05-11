package com.example.calendar.service;

import com.example.calendar.config.NotificacaoCliente;
import com.example.calendar.dto.AttendeeDTO;
import com.example.calendar.dto.CalComRequisicaoDTO;
import com.example.calendar.dto.NotificacaoEmailRequest;
import com.example.calendar.dto.NotificacaoSmsWhatsappRequest;
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
import java.time.ZonedDateTime;

@Service
public class CalComService {

    @Value("${calcom.api.key}")
    private String apiKey;
    private final ConsultaRepository consultaRepository;
    private final ClienteRepository clienteRepository;
    private final NotificacaoCliente notificacaoCliente;

    public CalComService(ConsultaRepository consultaRepository, ClienteRepository clienteRepository, NotificacaoCliente notificacaoCliente) {
        this.consultaRepository = consultaRepository;
        this.clienteRepository = clienteRepository;
        this.notificacaoCliente = notificacaoCliente;
    }

    public String criarAgendamento(String nome, String email, String inicio) {
        RestTemplate restTemplate = new RestTemplate();
        String endpoint = "https://api.cal.com/v2/bookings";

        AttendeeDTO attendee = new AttendeeDTO();
        attendee.setName(nome);
        attendee.setEmail(email);

        CalComRequisicaoDTO agendamento = new CalComRequisicaoDTO();
        agendamento.setEventTypeId(5180374);
        agendamento.setStart(inicio);
        agendamento.setAttendee(attendee);

        ZonedDateTime zonedInicio = ZonedDateTime.parse(inicio);
        LocalDateTime dataHoraInicio = zonedInicio.toLocalDateTime();
        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(60);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("cal-api-version", "2026-02-25");
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<CalComRequisicaoDTO> entity = new HttpEntity<>(agendamento, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);



        if (response.getStatusCode().is2xxSuccessful()) {
            Cliente clienteEncontrado = clienteRepository.findByEmail(email);

            if (clienteEncontrado != null) {
                Consulta consulta = new Consulta();
                consulta.setDataHoraInicio(dataHoraInicio);
                consulta.setDataHoraFim(dataHoraFim);
                consulta.setCliente(clienteEncontrado);
                consultaRepository.save(consulta);

                NotificacaoSmsWhatsappRequest requestSms = new NotificacaoSmsWhatsappRequest(
                        "Sistema Blessed7",
                        11,
                        "989977147",
                        "Olá, "+ clienteEncontrado.getNome() +", Seu agendamento foi realizado com sucesso!"
                );

                NotificacaoEmailRequest emailRequest = new NotificacaoEmailRequest(
                        clienteEncontrado.getEmail(),
                        "Agendamento no Studio site Blessed7",
                        "Olá, "+ clienteEncontrado.getNome() +", Seu agendamento foi realizado com sucesso!"
                );

                notificacaoCliente.enviarSms(requestSms);
                notificacaoCliente.enviarWhatsapp(requestSms);
                notificacaoCliente.enviarEmail(emailRequest);
            } else {
                System.out.println("Aviso: cliente com email " + email + " não encontrado no banco.");
            }
        }

        return "Sucesso no agendamento: " + response.getStatusCode();
    }
}