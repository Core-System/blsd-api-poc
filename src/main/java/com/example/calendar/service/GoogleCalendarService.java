package com.example.calendar.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Collections;

@Service
public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "Blessed7 PoC";
    private static final String CREDENTIALS = "src/main/resources/blessed7-poc-93832ce87e38.json";
    private static final String CALENDAR_ID = "";

    private Calendar getCalendarService() throws Exception{
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(CREDENTIALS))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
        new HttpCredentialsAdapter(credentials)).setApplicationName(APPLICATION_NAME).build();
    }

    public String criarEvento(String titulo, String descricao, String inicio, String fim) throws Exception{
        Calendar service = getCalendarService();

        Event event = new Event()
                .setSummary(titulo).setDescription(descricao);

        com.google.api.client.util.DateTime startDateTime = new com.google.api.client.util.DateTime(inicio);
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        event.setStart(start);

        com.google.api.client.util.DateTime endDateTime = new com.google.api.client.util.DateTime(fim);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setEnd(end);

        event = service.events().insert(CALENDAR_ID, event).execute();

        return event.getHtmlLink();
    }


}
