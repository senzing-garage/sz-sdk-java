package com.senzing.sdk;

class Utilities {
    /**
     * Formats a <code>long</code> integer value as hexadecimal with 
     * spaces between each group of for hex digits.
     * 
     * @param value The value to format.
     * 
     * @return The value formatted as a {@link String}.
     */
    public static String hexFormat(long value) {
        StringBuilder   sb      = new StringBuilder(20);
        long            mask    = 0xFFFF << 48;
        String          prefix  = "";

        for (int index = 0; index < 4; index++) {
            long masked = value & mask;
            mask = mask >>> 16;
            masked = masked >>> ((3 - index) * 16);
            sb.append(prefix);
            String hex = Long.toUnsignedString(masked, 16);
            for (int zero = hex.length(); zero < 4; zero++) {
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
        if (string == null) return "null";
        int escapeCount = 0;
        for (int index = 0; index < string.length(); index++) {
            char c = string.charAt(index);
            escapeCount += switch (c) {
                case '\b','\f','\n','\r','\t','"','\\':
                    yield 1;
                default:
                    yield (c < ' ') ? 6 : 0;
            };
        }
        if (escapeCount == 0) return "\"" + string + "\"";
        StringBuilder sb = new StringBuilder(string.length() + escapeCount + 2);
        sb.append('"');
        for (int index = 0; index < string.length(); index++) {
            char c = string.charAt(index);
            switch (c) {
                case '"','\\':
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
                    if (c >= ' ') sb.append(c);
                    else {
                        sb.append("\\u00");
                        String hex = Integer.toHexString(c);
                        if (hex.length() == 1) {
                            sb.append("0"); // one more zero if single-digit hex
                        }
                        sb.append(hex);
                    }
            };
        }
        sb.append('"');

        // return the escaped string
        return sb.toString();
    }
}
