package ie.tcd.scss.countryinfo.service;

import ie.tcd.scss.countryinfo.domain.Country;
import ie.tcd.scss.countryinfo.domain.Translation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is responsible for access the https://restcountries.com/ REST API to retrieve information about
 * countries.
 */
@Service
public class CountryService {

    private final RestTemplate restTemplate;

    // base URL to retrieve country information by name
    private static final String API_URL_BYNAME = "https://restcountries.com/v3.1/name/";

    // base URL to retrieve country information for all countries
    private static final String API_URL_ALL = "https://restcountries.com/v3.1/all/";

    public CountryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getFlagForCountry(String countryName) {
        Country country = getCountryInfo(countryName);
        if (country != null && country.getFlags() != null) {
            return country.getFlags().getPng();
        }
        return null; // return null if no country found
    }

    public String getMapForCountry(String countryName) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<String> getContinentsForCountry(String countryName) {
        Country country = getCountryInfo(countryName);
        if (country != null && country.getContinents() != null) {
            return country.getContinents();
        }
        return List.of(); // return empty list if no countries found
    }


    public List<String> getMostPopulousCountries(String substring) {
        Country[] countries = restTemplate.getForObject(API_URL_ALL, Country[].class);
        if (countries != null) {
            return Stream.of(countries) // convert to Stream
                    .filter(c -> c.getName().getCommon().toLowerCase().contains(substring.toLowerCase())) // filter by substring, case-insensitive
                    .sorted(Comparator.comparingInt(Country::getPopulation).reversed()) // sort by population, descending
                    .map(c -> c.getName().getCommon()) // map country to country name
                    .collect(Collectors.toList()); //
        }
        return List.of(); // return empty list if no countries found
    }

    public List<String> getMostPopulousCountriesWithPopulation(String substring) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Retrieves information about a country matching the given name. If multiple countries are found, only the first
     * one is returned.
     *
     * @param countryName The name of the country or countries to retrieve.
     * @return A Country object with the information about the found country, or null if no country found
     */
    public Country getCountryInfo(String countryName) {
        try {
            Country[] countries = restTemplate.getForObject(API_URL_BYNAME + countryName, Country[].class);
            return countries != null && countries.length > 0 ? countries[0] : null; // return first country if found
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Country not found
                return null;
            }
            // If it's another kind of error, rethrow it
            throw e;
        }
    }


     /**
     * Retrieves information about all countries matching the given name.
     *
     * @param countryName The name of the country or countries to retrieve.
     * @return A List of Country objects.
     */
    public List<Country> getCountriesInfo(String countryName) {
        try {
            Country[] countries = restTemplate.getForObject(API_URL_BYNAME + countryName, Country[].class);
            // If countries are found, convert the array to a List and return it
            if (countries != null) {
                return Arrays.asList(countries);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // No countries found with that name, return an empty list
                return List.of();
            }
            // If it's another kind of error, rethrow it
            throw e;
        }
        // Return an empty list if no countries were found
        return List.of();
    }


    /**
     * Retrieves the translation for the country name in the specified language.
     *
     * @param countryName The name of the country.
     * @param language The language code for the translation (e.g., 'ger' for German).
     * @return The translated name of the country if available, otherwise null.
     */
    public String getTranslationForCountry(String countryName, String language) {
        Country country = getCountryInfo(countryName);

        if (country == null) {
            return null;
        }

        // TODO: Implement the lookup of the translation for the given language
        // The country object contains data that corresponds to JSON that looks like below. Each 4 lines with a language code and official/common name is a
        // "Translation".
        //        "translations": {
        //            "ara": {
        //                "official": "الجمهورية الفرنسية",
        //                "common": "فرنسا"
        //            },
        //            "bre": {
        //                "official": "Republik Frañs",
        //                "common": "Frañs"
        //            },
        //            "ces": {
        //                "official": "Francouzská republika",
        //                "common": "Francie"
        //            },
        //            ...
        // All that is stored in the country object. You first need to retrieve the translations for the country. Then, you need to look up the right
        // translation for the given language and handle the case that the requested language is not available.
        // Each Translation then has a "common" name, which you shuold return.

        throw new UnsupportedOperationException("Not implemented yet");
        // return ...
    }
}
