package com.example.calendar.dto;

public record NotificacaoSmsWhatsappRequest(String sender, int ddd, String phoneNumber, String message) {
}