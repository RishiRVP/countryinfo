package ie.tcd.scss.countryinfo.controller;

        import ie.tcd.scss.countryinfo.domain.Country;
        import ie.tcd.scss.countryinfo.service.CountryService;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

/**
 * This class is responsible for handling requests to the /countries endpoint.
 */

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * This method handles GET requests to /countries/{countryname} and returns the country information for the given
     * country name.
     *
     * @param countryname The name of the country
     * @return The country information
     */
    @GetMapping("/{countryname}")
    public ResponseEntity<Country> getCountryInfo(@PathVariable String countryname) {
        if(countryname.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Country country = countryService.getCountryInfo(countryname);
        if (country == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(country);
    }

    /**
     * This method handles GET requests to /countries/{countryname}/flags and returns URLs to a picture (in PNG format) representing the flag of the given
     * country.
     *
     * @param countryname The name of the country
     * @return The URLs to the flag pictures
     */
    @GetMapping("/{countryname}/flag")
    public ResponseEntity<String> getCountryFlag(@PathVariable String countryname) {
        String flag = countryService.getFlagForCountry(countryname);
        if (flag == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flag);
    }

    /**
     * This method handles GET requests to /countries/{countryname}/flags and returns URLs to a picture (in PNG format) representing the flag of the given
     * country.
     *
     * @param countryname The name of the country
     * @return The URLs to the flag pictures
     */
    @GetMapping("/{countryname}/map")
    public ResponseEntity<String> getCountryMap(@PathVariable String countryname) {
        String countryMap = countryService.getMapForCountry(countryname);
        if (countryMap == null || countryMap.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(String.join(", ", countryMap));
    }

    /**
     * This method handles GET requests to /countries/{countryname}/continents and returns the continent(s) that the given country is in.
     *
     * @param countryname The name of the country
     * @return The continents that the country is in
     */
    @GetMapping("/{countryname}/continents")
    public ResponseEntity<String> getCountryContinents(@PathVariable String countryname) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }


    /**
     * This method handles GET requests to /countries/{substring}/mostPopulous and returns the names of the countries
     * that contain the given substring in descending order of population.
     *
     * @param substring The substring to search for
     * @return The names of the countries
     */
    @GetMapping("/{substring}/mostPopulous")
    public ResponseEntity<String> getMostPopulousCountries(@PathVariable String substring) {
        List<String> countries = countryService.getMostPopulousCountries(substring);
        if (countries == null || countries.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(String.join(", ", countries));
    }

    /**
     * This method handles GET requests to /countries/{substring}/mostPopulousWithPopulation and returns the names of
     * the countries that contain the given substring in descending order of population, along with their population.
     * For instance, if the substring is "Bu", then the response might be something like
     * "Burkina Faso (20903278), Burundi (11890781), Bulgaria (6927288), Antigua and Barbuda (97928)".
     *
     * @param substring The substring to search for
     * @return The names of the countries and their populations
     */
    @GetMapping("/{substring}/mostPopulousWithPopulation")
    public ResponseEntity<String> getMostPopulousCountriesWithPopulation(@PathVariable String substring) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * This method handles GET requests to /countries/{countryname}/translation/{language} and returns the translation
     * of the country name into the specified language.
     *
     * @param countryname The name of the country
     * @param language The language code for the translation (e.g., 'ger' for German)
     * @return The translated name of the country
     */
    @GetMapping("/{countryname}/translation/{language}")
    public ResponseEntity<String> getCountryNameTranslation(
            @PathVariable String countryname,
            @PathVariable String language
    ) {
        String translation = countryService.getTranslationForCountry(countryname, language);
        if (translation == null) {
            return ResponseEntity.notFound().build(); // no translation found
        }
        return ResponseEntity.ok(translation);
    }
}
