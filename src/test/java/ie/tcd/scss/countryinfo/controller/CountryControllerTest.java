package ie.tcd.scss.countryinfo.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getGermany_shouldReturnSelectedAttributes() {
        // Given the country name "Germany"
        String countryName = "Germany";

        // When making a GET request to /countries/Germany
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName, String.class);

        // Then the response should be 200 OK and contain selected attributes
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // extract fields from body
        String responseBody = response.getBody();
        String commonName = JsonPath.parse(responseBody).read("$.name.common");
        List<String> capitals = JsonPath.parse(responseBody).read("$.capital");
        List<Double> latlng = JsonPath.parse(responseBody).read("$.latlng");
        List<String> continents = JsonPath.parse(responseBody).read("$.continents");

        // Assertions
        assertThat(commonName).isEqualTo("Germany");
        assertThat(capitals).containsExactly("Berlin");
        assertThat(latlng).containsExactly(51.0, 9.0);
        assertThat(continents).containsExactly("Europe");

    }

    @Test
    public void getSouthAfrica_shouldReturnSelectedAttributes() {
        // Given the country name "South Africa"
        String countryName = "South Africa"; // escaping the space as %20 will lead to 404 Not Found

        // When making a GET request to /countries/South%20Africa
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName, String.class);

        // Then the response should be 200 OK and contain selected attributes
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // extract fields from body
        String responseBody = response.getBody();
        String commonName = JsonPath.parse(responseBody).read("$.name.common");
        List<String> capitals = JsonPath.parse(responseBody).read("$.capital");
        List<Double> latlng = JsonPath.parse(responseBody).read("$.latlng");
        List<String> continents = JsonPath.parse(responseBody).read("$.continents");

        // Assertions
        assertThat(commonName).isEqualTo("South Africa");
        assertThat(capitals).containsExactlyInAnyOrder("Pretoria", "Cape Town", "Bloemfontein");
        assertThat(latlng).containsExactly(-29.0, 24.0);
        assertThat(continents).containsExactly("Africa");


    }

    @Test
    public void getContinentsForFrance_shouldReturnEurope() {
        // Given the country name "France"
        String countryName = "France";

        // When making a GET request to /countries/France/continents/
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName + "/continents", String.class);

        // Then the response should be 200 OK and contain selected attributes
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Europe");
    }

    @Test
    public void getContinentsForRussia_shouldReturnEuropeAsia() {
        // Given the country name "Russia"
        String countryName = "Russia";

        // When making a GET request to /countries/Russia/continents/
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName + "/continents",String.class);

        // Then the response should be 200 OK and contain selected attributes
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Europe, Asia");
    }

    @Test
    public void getFlagForFrance_shouldReturnFlagUrl() {
        // Given the country name "France"
        String countryName = "France";

        // When making a GET request to /countries/France/flag/
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName + "/flag", String.class);

        // Then the response should be 200 OK and contain the flag URL
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("https://flagcdn.com/w320/fr.png");
    }

    @Test
    public void getMapForFrance_shouldReturnMapUrl() {
        // Given the country name "Bolivia"
        String countryName = "Bolivia";

        // When making a GET request to /countries/Bolivia/map/
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName + "/map", String.class);

        // Then the response should be 200 OK and contain the flag URL
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("https://goo.gl/maps/9DfnyfbxNM2g5U9b9");
    }

    @Test
    public void translationForFranceToGerman_shouldReturnFrankreich() {
        // Given the country name "France"
        String countryName = "France";
        String languageId = "deu";

        // When making a GET request to /countries/France/translation/ger/
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName + "/translation/" + languageId, String.class);

        // Then the response should be 200 OK and contain the flag URL
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Frankreich");
    }

    @Test
    public void translationForFranceToJapanese_shouldReturnJapaneseTranslation() {
        // Given the country name "France"
        String countryName = "France";
        String languageId = "jpn";

        // When making a GET request to /countries/France/translation/ger/
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName + "/translation/" + languageId, String.class);

        // Then the response should be 200 OK and contain the flag URL
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("フランス");
    }


    @Test
    public void translationForInvalidLanguage_shouldReturnNotFound() {
        // Given the country name "France"
        String countryName = "France";
        String languageId = "invalid-language-id";

        // When making a GET request to /countries/France/translation/invalid-language-id/
        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/countries/" + countryName + "/translation/" + languageId, String.class);

        // Then the response should be 200 OK without a response body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void getCountryInfo_withInvalidCountryName_shouldReturnNotFound() {
        // Given an invalid country name
        String countryName = "InvalidCountry";

        // When making a GET request to /country
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/country?name=" + countryName, String.class);

        // Then the response should be 404 Not Found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    public void getNonExistentEndpoint_shouldReturnNotFound() {
        // Given a non-existent endpoint
        String endpoint = "/nonexistent";

        // When making a GET request to /countries/nonexistent
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + endpoint, String.class);

        // Then the response should be 404 Not Found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getMostPopulousCountries_withNoMatchingCountries_shouldReturnNotFound() {
        // Given a substring that matches no countries
        String substring = "Xyz";

        // When making a GET request to /countries/Xyz/mostPopulous/
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/countries/" + substring + "/mostPopulous", String.class);

        // Then the response should be 404 Not Found
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}