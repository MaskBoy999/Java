package com.example.CompulsoryLab7;

import com.example.CompulsoryLab7.model.Movie;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;

@Component
public class MovieClient implements CommandLineRunner {

    @Override
    public void run(String[] args) throws Exception {
        org.springframework.http.client.HttpComponentsClientHttpRequestFactory factory = new org.springframework.http.client.HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(factory);
        String url = "http://localhost:8081/movies";

        Movie movie = new Movie("Interstellar", Date.valueOf("2014-11-07"), 8.7, "Sci-Fi");
        String resPost = restTemplate.postForObject(url, movie, String.class);
        System.out.println("Client POST: " + resPost);

        Movie updatedMovie = new Movie("Interstellar", Date.valueOf("2014-11-07"), 9.5, "Sci-Fi");
        restTemplate.put(url + "/Interstellar", updatedMovie);
        System.out.println("Client PUT: Sent request.");

        restTemplate.exchange(url + "/Interstellar/score?score=10.0", HttpMethod.PATCH, null, String.class);
        System.out.println("Client PATCH: Updated score.");

        restTemplate.delete(url + "/Interstellar");
        System.out.println("Client DELETE: Delted movie.");
    }
}