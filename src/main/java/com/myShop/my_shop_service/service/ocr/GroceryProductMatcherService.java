package com.myShop.my_shop_service.service.ocr;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class GroceryProductMatcherService {

    private final Map<String, String> aliases =
            new HashMap<>();

    private final JaroWinklerSimilarity similarity =
            new JaroWinklerSimilarity();

    /*
     * Minimum similarity required before
     * suggesting a product correction.
     *
     * 0.90 = high confidence
     */
    private static final double FUZZY_THRESHOLD = 0.90;

    public GroceryProductMatcherService() {

        add("rice",
                "rice",
                "r1ce",
                "ric",
                "raice");

        add("sugar",
                "sugar",
                "sugr",
                "suqar");

        add("wheat flour",
                "wheat flour",
                "wheatflour",
                "atta",
                "aata",
                "ata");

        add("milk",
                "milk",
                "mi1k");

        add("salt",
                "salt",
                "sait");

        add("cooking oil",
                "oil",
                "cooking oil",
                "cookingoil");

        add("tea",
                "tea",
                "tee");

        add("coffee",
                "coffee",
                "cofee",
                "coffe");

        add("biscuits",
                "biscuit",
                "biscuits",
                "biskit",
                "biskits");

        add("maggi",
                "maggi",
                "magi",
                "maggi noodles");

        add("soap",
                "soap",
                "sope");

        add("shampoo",
                "shampoo",
                "shampu");

        add("dal",
                "dal",
                "daal");
    }

    private void add(
            String standardName,
            String... values
    ) {

        for (String value : values) {

            aliases.put(
                    normalize(value),
                    standardName
            );
        }
    }

    public String match(
            String itemName
    ) {

        if (itemName == null
                || itemName.isBlank()) {

            return itemName;
        }

        String normalized =
                normalize(itemName);

        /*
         * 1. Exact alias match
         */
        String exactMatch =
                aliases.get(normalized);

        if (exactMatch != null) {
            return exactMatch;
        }

        /*
         * 2. Fuzzy matching
         */
        String bestMatch = null;

        double bestScore = 0.0;

        for (String alias : aliases.keySet()) {

            double score =
                    similarity.apply(
                            normalized,
                            alias
                    );

            if (score > bestScore) {

                bestScore = score;

                bestMatch =
                        aliases.get(alias);
            }
        }

        /*
         * Only correct the product when
         * similarity is high enough.
         */
        if (bestMatch != null
                && bestScore >= FUZZY_THRESHOLD) {

            return bestMatch;
        }

        /*
         * Unknown product:
         * keep the original OCR result.
         */
        return itemName.trim();
    }

    private String normalize(
            String value
    ) {

        return value
                .toLowerCase(Locale.ROOT)
                .trim()
                .replaceAll(
                        "\\s+",
                        " "
                );
    }
}