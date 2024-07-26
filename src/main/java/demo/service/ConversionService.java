package demo.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConversionService {

    private static final Map<Character, Integer> ROMAN_TO_INTEGER = new HashMap<>();
    static {
        ROMAN_TO_INTEGER.put('I', 1);
        ROMAN_TO_INTEGER.put('V', 5);
        ROMAN_TO_INTEGER.put('X', 10);
        ROMAN_TO_INTEGER.put('L', 50);
        ROMAN_TO_INTEGER.put('C', 100);
        ROMAN_TO_INTEGER.put('D', 500);
        ROMAN_TO_INTEGER.put('M', 1000);
    }

    private static final String VALID_ROMAN_REGEX = "^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";

    private Map<String, String> intergalacticToRoman = new HashMap<>();
    private Map<String, Double> metalPrices = new HashMap<>();

    public ConversionService() {
        // Initialize conversion maps
        intergalacticToRoman.put("glob", "I");
        intergalacticToRoman.put("prok", "V");
        intergalacticToRoman.put("pish", "X");
        intergalacticToRoman.put("tegj", "L");

        // Initialize metal prices
        metalPrices.put("Silver", 17.0);
        metalPrices.put("Gold", 14450.0);
        metalPrices.put("Iron", 195.5);
    }

    public String processQuery(String query) {
        if (query.startsWith("¿Cuánto es ")) {
            return handleConversionQuery(query);
        } else if (query.contains("créditos")) {
            return handleCreditsQuery(query);
        } else {
            return "No tengo idea de qué estás hablando";
        }
    }

    private String handleConversionQuery(String query) {
        String[] parts = query.replace("¿Cuánto es ", "").replace("?", "").split(" ");
        StringBuilder romanNumeral = new StringBuilder();

        for (String part : parts) {
            if (intergalacticToRoman.containsKey(part)) {
                romanNumeral.append(intergalacticToRoman.get(part));
            } else {
                return "No tengo idea de qué estás hablando";
            }
        }

        int sum = romanToInt(romanNumeral.toString());
        return query.replace("¿Cuánto es ", "").replace("?", "") + " es " + sum;
    }

    private String handleCreditsQuery(String query) {
        String[] parts = query.replace("¿Cuántos créditos es ", "").replace("?", "").split(" ");
        double totalCredits = 0;
        String metalName = null;
        StringBuilder romanNumeral = new StringBuilder();

        for (String part : parts) {
            if (intergalacticToRoman.containsKey(part)) {
                romanNumeral.append(intergalacticToRoman.get(part));
            } else if (metalPrices.containsKey(part)) {
                metalName = part;
            }
        }

        int totalRomanValue = romanToArabic(romanNumeral.toString());

        if (metalName != null && metalPrices.containsKey(metalName)) {
            double creditsPerUnit = totalRomanValue * metalPrices.get(metalName);
            return query.replace("¿Cuántos créditos es ", "").replace("?", "") + " es " + (int) creditsPerUnit + " créditos";
        } else {
            return "No tengo idea de qué estás hablando";
        }
    }

    private int romanToInt(String s) {
        if (!s.matches(VALID_ROMAN_REGEX)) {
            throw new IllegalArgumentException("Invalid Roman numeral");
        }

        int total = 0;
        int prevValue = 0;

        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            int value = ROMAN_TO_INTEGER.get(c);

            if (value >= prevValue) {
                total += value;
            } else {
                total -= value;
            }

            prevValue = value;
        }

        return total;
    }

    private int romanToArabic(String roman) {
        int result = 0;
        int i = 0;

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        while (i < symbols.length) {
            while (roman.startsWith(symbols[i])) {
                result += values[i];
                roman = roman.substring(symbols[i].length());
            }
            i++;
        }
        return result;
    }
}
