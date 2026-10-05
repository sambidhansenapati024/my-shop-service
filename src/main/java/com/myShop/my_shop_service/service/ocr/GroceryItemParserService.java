package com.myShop.my_shop_service.service.ocr;

import com.myShop.my_shop_service.dto.ocr.DetectedItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GroceryItemParserService {

    private final GroceryProductMatcherService
            groceryProductMatcherService;

    public GroceryItemParserService(
            GroceryProductMatcherService groceryProductMatcherService
    ) {
        this.groceryProductMatcherService =
                groceryProductMatcherService;
    }

    /*
     * Format:
     *
     * Rice 2 kg
     * Rice 2kg
     * Rice 2 kgs
     */
    private static final Pattern ITEM_AFTER_NAME =
            Pattern.compile(
                    "^(.+?)\\s+" +
                            "(\\d+(?:\\.\\d+)?)\\s*" +
                            "(gm|g|kg|kgs|ml|l|ltr|litre|liter|litres|liters|" +
                            "piece|pieces|pc|pcs|" +
                            "packet|packets|packer|packers|pkt|pkts|" +
                            "box|boxes|dozen|dozens)" +
                            "\\s*$",
                    Pattern.CASE_INSENSITIVE
            );

    /*
     * Format:
     *
     * 2 kg Rice
     * 2kg Rice
     * 2 packets Maggi
     */
    private static final Pattern QUANTITY_BEFORE_NAME =
            Pattern.compile(
                    "^(\\d+(?:\\.\\d+)?)\\s*" +
                            "(gm|g|kg|kgs|ml|l|ltr|litre|liter|litres|liters|" +
                            "piece|pieces|pc|pcs|" +
                            "packet|packets|packer|packers|pkt|pkts|" +
                            "box|boxes|dozen|dozens)" +
                            "\\s+(.+?)$",
                    Pattern.CASE_INSENSITIVE
            );

    /*
     * Format:
     *
     * Rice - 2 kg
     * Rice - 2kg
     * Rice : 2 kg
     */
    private static final Pattern SEPARATOR_FORMAT =
            Pattern.compile(
                    "^(.+?)\\s*[-:]\\s*" +
                            "(\\d+(?:\\.\\d+)?)\\s*" +
                            "(gm|g|kg|kgs|ml|l|ltr|litre|liter|litres|liters|" +
                            "piece|pieces|pc|pcs|" +
                            "packet|packets|packer|packers|pkt|pkts|" +
                            "box|boxes|dozen|dozens)" +
                            "\\s*$",
                    Pattern.CASE_INSENSITIVE
            );

    /*
     * Format:
     *
     * Maggi x 2
     * Biscuit × 3
     */
    private static final Pattern MULTIPLICATION_FORMAT =
            Pattern.compile(
                    "^(.+?)\\s*[x×]\\s*" +
                            "(\\d+(?:\\.\\d+)?)\\s*$",
                    Pattern.CASE_INSENSITIVE
            );

    public List<DetectedItem> parse(
            String extractedText
    ) {

        List<DetectedItem> items =
                new ArrayList<>();

        if (extractedText == null
                || extractedText.isBlank()) {

            return items;
        }

        String[] lines =
                extractedText.split("\\r?\\n");

        for (String line : lines) {

            DetectedItem item =
                    parseLine(line);

            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }

    private DetectedItem parseLine(
            String originalLine
    ) {

        if (originalLine == null) {
            return null;
        }

        String line =
                cleanLine(originalLine);

        if (line.isBlank()) {
            return null;
        }

        /*
         * 1. Rice 2 kg
         */
        Matcher matcher =
                ITEM_AFTER_NAME.matcher(line);

        if (matcher.matches()) {

            return createItem(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3)
            );
        }

        /*
         * 2. Rice - 2 kg
         */
        matcher =
                SEPARATOR_FORMAT.matcher(line);

        if (matcher.matches()) {

            return createItem(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3)
            );
        }

        /*
         * 3. 2 kg Rice
         */
        matcher =
                QUANTITY_BEFORE_NAME.matcher(line);

        if (matcher.matches()) {

            return createItem(
                    matcher.group(3),
                    matcher.group(1),
                    matcher.group(2)
            );
        }

        /*
         * 4. Maggi x 2
         *
         * No unit was written.
         * We treat it as "piece".
         */
        matcher =
                MULTIPLICATION_FORMAT.matcher(line);

        if (matcher.matches()) {

            return createItem(
                    matcher.group(1),
                    matcher.group(2),
                    "piece"
            );
        }

        return null;
    }

    private DetectedItem createItem(
            String itemName,
            String quantityText,
            String unit
    ) {

        try {

            BigDecimal quantity =
                    new BigDecimal(
                            quantityText
                    );

            itemName =
                    itemName
                            .trim()
                            .replaceAll(
                                    "^[\\-:]+|[\\-:]+$",
                                    ""
                            )
                            .trim();
            itemName =
                    groceryProductMatcherService.match(
                            itemName
                    );

            if (itemName.isBlank()) {
                return null;
            }

            if (quantity.compareTo(
                    BigDecimal.ZERO
            ) <= 0) {
                return null;
            }

            return new DetectedItem(
                    itemName,
                    quantity,
                    normalizeUnit(unit)
            );

        } catch (NumberFormatException e) {

            return null;
        }
    }

    private String cleanLine(
            String line
    ) {

        return line
                .trim()
                .replaceAll(
                        "^[\\s\\-*•]+",
                        ""
                )
                .replaceAll(
                        "\\s+",
                        " "
                );
    }

    private String normalizeUnit(
            String unit
    ) {

        String normalized =
                unit
                        .toLowerCase(Locale.ROOT)
                        .trim();

        return switch (normalized) {

            case "g", "gm" ->
                    "gm";

            case "kg", "kgs" ->
                    "kg";

            case "ml" ->
                    "ml";

            case "l",
                 "ltr",
                 "litre",
                 "liter",
                 "litres",
                 "liters" ->
                    "litre";

            case "piece",
                 "pieces",
                 "pc",
                 "pcs" ->
                    "piece";

            case "packet",
                 "packets",
                 "packer",
                 "packers",
                 "pkt",
                 "pkts" ->
                    "packet";

            case "box",
                 "boxes" ->
                    "box";

            case "dozen",
                 "dozens" ->
                    "dozen";

            default ->
                    normalized;
        };
    }
}