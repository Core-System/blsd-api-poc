package com.example.calendar.config;

import com.example.calendar.dto.NotificacaoEmailRequest;
import com.example.calendar.dto.NotificacaoSmsWhatsappRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificacao-client", url = "${notificacao.api.url}")
public interface NotificacaoCliente {

    @PostMapping("/sms")
    void enviarSms(@RequestBody NotificacaoSmsWhatsappRequest request);

    @PostMapping("/whatsapp")
    void enviarWhatsapp(@RequestBody NotificacaoSmsWhatsappRequest request);

    @PostMapping("/email")
    void enviarEmail(@RequestBody NotificacaoEmailRequest request);
}
