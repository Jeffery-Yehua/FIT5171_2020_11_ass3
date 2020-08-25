package rockets.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CountryIsValid {

    public static void CountryIsValid(String country) {
        String[] countryCodes = Locale.getISOCountries();
        List<String> CountryName = new ArrayList<>();
        for (String countryCode : countryCodes) {
            Locale locale = new Locale("", countryCode);


            String countryname = locale.getDisplayCountry(Locale.US);


            CountryName.add(countryname);
        }
        CountryName.add("USA");
        CountryName.add("Europe");
        if (!CountryName.contains(country))
            throw new IllegalArgumentException("Country must exist in the world");
    }
}
