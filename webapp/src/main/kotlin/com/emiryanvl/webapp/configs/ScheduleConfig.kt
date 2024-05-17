package com.emiryanvl.webapp.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
@EnableScheduling
class ScheduleConfig(private val restClient: RestClient) {
    @Value("\${ping-url}")
    private lateinit var url: String

    @Scheduled(fixedRate = 45000)
    fun scheduledSelfPing() {
        restClient.get()
            .uri(url)
            .accept(APPLICATION_JSON)
    }
}