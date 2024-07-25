package com.senzing.sdk.core;

final class Utilities {
    /**
     * The default buffer size for hex formatting.
     */
    private static final int HEX_BUFFER_SIZE = 20;

    /**
     * The number of bits to initially shift the <code>0xFFFF</code>
     * mask for hex formatting.
     */
    private static final int HEX_MASK_INITIAL_SHIFT = 48;

    /**
     * The bit mask to use for hex formatting.
     */
    private static final int HEX_MASK = 0xFFFF;

    /**
     * The number of bits to shift the hex bit mask with each 
     * loop iteration when hex formatting.
     */
    private static final int HEX_MASK_BIT_SHIFT = 16;

    /**
     * The number of bits in a "nibble" (used for hex formatting).
     */
    private static final int HEX_NIBBLE_SIZE = 4;

    /**
     * The radix used for hexadecmial (16).
     */
    private static final int HEX_RADIX = 16;

    /**
     * The number of hex digits that are formatted adjacent before
     * requiring a space.
     */
    private static final int HEX_DIGIT_COUNT = 4;

    /**
     * The number of additional characters required to escape a basic
     * control character (e.g.: backspace, tab, newline and other whitespace).
     */
    private static final int JSON_ESCAPE_BASIC_COUNT = 1;

    /**
     * The numeber of additional characters required to escape non-basic
     * control characters (i.e.: those without shortcut escape sequences).
     */
    private static final int JSON_ESCAPE_CONTROL_COUNT = 6;

    /**
     * Private default constructor.
     */
    private Utilities() {
        // do nothing
    }

    /**
     * Formats a <code>long</code> integer value as hexadecimal with 
     * spaces between each group of for hex digits.
     * 
     * @param value The value to format.
     * 
     * @return The value formatted as a {@link String}.
     */
    public static String hexFormat(long value) {
        StringBuilder   sb      = new StringBuilder(HEX_BUFFER_SIZE);
        long            mask    = HEX_MASK << HEX_MASK_INITIAL_SHIFT;
        String          prefix  = "";

        for (int index = 0; index < HEX_NIBBLE_SIZE; index++) {
            long masked = value & mask;
            mask = mask >>> HEX_MASK_BIT_SHIFT;
            masked = masked >>> (((HEX_NIBBLE_SIZE - 1) - index) * HEX_RADIX);
            sb.append(prefix);
            String hex = Long.toUnsignedString(masked, HEX_RADIX);
            for (int zero = hex.length(); zero < HEX_DIGIT_COUNT; zero++) {
                sb.append("0");
            }
            sb.append(hex);
            prefix = " ";
        }

        return sb.toString();
    }

    /**
     * Escapes the specified {@link String} into a JSON string with the
     * the surrounding double quotes.  If the specified {@link String} is
     * <code>null</code> then <code>"null"</code> is returned.
     * 
     * @param string The {@link String} to escape for JSON.
     * 
     * @return The quoted escaped {@link String} or <code>"null"</code>
     *         if the specified parameter is <code>null</code>.
     */
    public static String jsonEscape(String string) {
        if (string == null) {
            return "null";
        }
        int escapeCount = 0;
        for (int index = 0; index < string.length(); index++) {
            char c = string.charAt(index);
            escapeCount += switch (c) {
                case '\b', '\f', '\n', '\r', '\t', '"', '\\':
                    yield JSON_ESCAPE_BASIC_COUNT;
                default:
                    yield (c < ' ') ? JSON_ESCAPE_CONTROL_COUNT : 0;
            };
        }
        if (escapeCount == 0) {
            return "\"" + string + "\"";
        }
        StringBuilder sb = new StringBuilder(string.length() + escapeCount + 2);
        sb.append('"');
        for (int index = 0; index < string.length(); index++) {
            char c = string.charAt(index);
            switch (c) {
                case '"', '\\':
                    sb.append('\\').append(c);
                    break;    
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c >= ' ') {
                        sb.append(c);
                    } else {
                        sb.append("\\u00");
                        String hex = Integer.toHexString(c);
                        if (hex.length() == 1) {
                            sb.append("0"); // one more zero if single-digit hex
                        }
                        sb.append(hex);
                    }
            }
        }
        sb.append('"');

        // return the escaped string
        return sb.toString();
    }
}
