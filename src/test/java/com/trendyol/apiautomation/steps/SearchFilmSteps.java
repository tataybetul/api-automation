package com.trendyol.apiautomation.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.apiautomation.model.Film;
import com.trendyol.apiautomation.model.FilmResponse;
import com.trendyol.apiautomation.model.SearchFilmsResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

public class SearchFilmSteps {

    private String apiKey;
    private String imdbId;
    private FilmResponse filmResponse;

    private static final ObjectMapper objectmapper = new ObjectMapper();
    private static final String url = "https://www.omdbapi.com";

    @Given("^it should valid api key \"([^\"]*)\"$")
    public void it_should_valid_api_key(String apiKey) {
        this.apiKey = apiKey;
        given().param("apiKey", apiKey).when().get(url)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @When("^it should find films by artist \"([^\"]*)\" and film name \"([^\"]*)\"$")
    public void it_should_find_films_by_artist(String artist, String filmName) throws JsonProcessingException {
        var response = given().param("apiKey", apiKey).param("s", artist).when().get(url)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();
        SearchFilmsResponse searchFilmsResponse = objectmapper.readValue(response.asString(), SearchFilmsResponse.class);
        Optional<Film> optionalFilm = searchFilmsResponse.getFilms().stream().filter(film -> film.getTitle().equalsIgnoreCase(filmName)).findFirst();
        Assert.assertTrue(optionalFilm.isPresent());
        Film film = optionalFilm.get();
        this.imdbId = film.getImbdID();
    }

    @When("it should find film by imdb id")
    public void it_should_find_film_by_imdb_id() throws JsonProcessingException {
        var response = given().param("apiKey", apiKey).param("i", imdbId).when().get(url)
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();
        this.filmResponse = objectmapper.readValue(response.asString(), FilmResponse.class);
    }

    @Then("it should valid response")
    public void it_should_valid_response() {
        assertNotNull(filmResponse.getTitle());
        assertNotNull(filmResponse.getYear());
        assertNotNull(filmResponse.getReleased());
    }
}