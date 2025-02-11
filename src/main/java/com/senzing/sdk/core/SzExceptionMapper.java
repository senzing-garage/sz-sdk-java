package com.senzing.sdk.core;

import java.util.Map;
import com.senzing.sdk.*;

/**
 * Package-access class for mapping Senzing error code to
 * instances of {@link SzException}.
 */
final class SzExceptionMapper {
    /**
     * Private constructor since all methods are static.
     */
    private SzExceptionMapper() {
        // do nothing
    }

    /**
     * Obtains the exception class for the specified error code.
     * 
     * @param map The {@link Map} to which to add the mappings of
     *            error code keys and exception class objects.
     */
    public static void registerExceptions(Map<Integer, Class<? extends SzException>> map) {
        map.put(2, SzBadInputException.class);
        map.put(5, SzException.class);
        map.put(7, SzBadInputException.class);
        map.put(10, SzRetryTimeoutExceededException.class);
        map.put(14, SzConfigurationException.class);
        map.put(18, SzException.class);
        map.put(19, SzConfigurationException.class);
        map.put(20, SzConfigurationException.class);
        map.put(21, SzConfigurationException.class);
        map.put(22, SzBadInputException.class);
        map.put(23, SzBadInputException.class);
        map.put(24, SzBadInputException.class);
        map.put(25, SzBadInputException.class);
        map.put(26, SzBadInputException.class);
        map.put(27, SzException.class);
        map.put(28, SzConfigurationException.class);
        map.put(29, SzException.class);
        map.put(30, SzConfigurationException.class);
        map.put(31, SzException.class);
        map.put(32, SzException.class);
        map.put(33, SzNotFoundException.class);
        map.put(34, SzConfigurationException.class);
        map.put(35, SzConfigurationException.class);
        map.put(36, SzConfigurationException.class);
        map.put(37, SzNotFoundException.class);
        map.put(38, SzException.class);
        map.put(39, SzException.class);
        map.put(40, SzConfigurationException.class);
        map.put(41, SzException.class);
        map.put(42, SzException.class);
        map.put(43, SzException.class);
        map.put(44, SzException.class);
        map.put(45, SzException.class);
        map.put(46, SzException.class);
        map.put(47, SzException.class);
        map.put(48, SzNotInitializedException.class);
        map.put(50, SzNotInitializedException.class);
        map.put(51, SzBadInputException.class);
        map.put(52, SzException.class);
        map.put(53, SzBadInputException.class);
        map.put(54, SzDatabaseException.class);
        map.put(55, SzException.class);
        map.put(56, SzException.class);
        map.put(57, SzException.class);
        map.put(58, SzException.class);
        map.put(60, SzConfigurationException.class);
        map.put(61, SzConfigurationException.class);
        map.put(62, SzConfigurationException.class);
        map.put(63, SzNotInitializedException.class);
        map.put(64, SzConfigurationException.class);
        map.put(65, SzBadInputException.class);
        map.put(66, SzBadInputException.class);
        map.put(67, SzConfigurationException.class);
        map.put(68, SzException.class);
        map.put(69, SzException.class);
        map.put(76, SzException.class);
        map.put(77, SzException.class);
        map.put(78, SzException.class);
        map.put(79, SzException.class);
        map.put(80, SzException.class);
        map.put(81, SzException.class);
        map.put(82, SzException.class);
        map.put(83, SzException.class);
        map.put(84, SzException.class);
        map.put(85, SzException.class);
        map.put(86, SzException.class);
        map.put(87, SzUnhandledException.class);
        map.put(88, SzBadInputException.class);
        map.put(89, SzConfigurationException.class);
        map.put(90, SzConfigurationException.class);
        map.put(91, SzException.class);
        map.put(92, SzException.class);
        map.put(93, SzException.class);
        map.put(94, SzException.class);
        map.put(95, SzException.class);
        map.put(96, SzException.class);
        map.put(999, SzLicenseException.class);
        map.put(1000, SzDatabaseException.class);
        map.put(1001, SzDatabaseException.class);
        map.put(1002, SzDatabaseException.class);
        map.put(1003, SzDatabaseException.class);
        map.put(1004, SzDatabaseException.class);
        map.put(1005, SzDatabaseException.class);
        map.put(1006, SzDatabaseConnectionLostException.class);
        map.put(1007, SzDatabaseConnectionLostException.class);
        map.put(1008, SzDatabaseException.class);
        map.put(1009, SzDatabaseException.class);
        map.put(1010, SzDatabaseException.class);
        map.put(1011, SzDatabaseException.class);
        map.put(1012, SzDatabaseException.class);
        map.put(1013, SzDatabaseException.class);
        map.put(1014, SzDatabaseException.class);
        map.put(1015, SzDatabaseException.class);
        map.put(1016, SzDatabaseException.class);
        map.put(1017, SzDatabaseException.class);
        map.put(1018, SzDatabaseException.class);
        map.put(1019, SzConfigurationException.class);
        map.put(2001, SzConfigurationException.class);
        map.put(2002, SzException.class);
        map.put(2003, SzException.class);
        map.put(2005, SzException.class);
        map.put(2006, SzException.class);
        map.put(2007, SzException.class);
        map.put(2009, SzException.class);
        map.put(2010, SzException.class);
        map.put(2012, SzConfigurationException.class);
        map.put(2015, SzConfigurationException.class);
        map.put(2027, SzException.class);
        map.put(2029, SzConfigurationException.class);
        map.put(2034, SzConfigurationException.class);
        map.put(2036, SzConfigurationException.class);
        map.put(2037, SzConfigurationException.class);
        map.put(2038, SzConfigurationException.class);
        map.put(2041, SzConfigurationException.class);
        map.put(2045, SzConfigurationException.class);
        map.put(2047, SzConfigurationException.class);
        map.put(2048, SzConfigurationException.class);
        map.put(2049, SzConfigurationException.class);
        map.put(2050, SzConfigurationException.class);
        map.put(2051, SzConfigurationException.class);
        map.put(2057, SzBadInputException.class);
        map.put(2061, SzConfigurationException.class);
        map.put(2062, SzConfigurationException.class);
        map.put(2065, SzConfigurationException.class);
        map.put(2066, SzConfigurationException.class);
        map.put(2067, SzConfigurationException.class);
        map.put(2069, SzConfigurationException.class);
        map.put(2070, SzConfigurationException.class);
        map.put(2071, SzConfigurationException.class);
        map.put(2073, SzException.class);
        map.put(2074, SzException.class);
        map.put(2075, SzConfigurationException.class);
        map.put(2076, SzConfigurationException.class);
        map.put(2079, SzConfigurationException.class);
        map.put(2080, SzConfigurationException.class);
        map.put(2081, SzConfigurationException.class);
        map.put(2082, SzConfigurationException.class);
        map.put(2083, SzConfigurationException.class);
        map.put(2084, SzConfigurationException.class);
        map.put(2088, SzConfigurationException.class);
        map.put(2089, SzConfigurationException.class);
        map.put(2090, SzConfigurationException.class);
        map.put(2091, SzConfigurationException.class);
        map.put(2092, SzConfigurationException.class);
        map.put(2093, SzConfigurationException.class);
        map.put(2094, SzConfigurationException.class);
        map.put(2095, SzConfigurationException.class);
        map.put(2097, SzException.class);
        map.put(2099, SzConfigurationException.class);
        map.put(2101, SzConfigurationException.class);
        map.put(2102, SzConfigurationException.class);
        map.put(2103, SzConfigurationException.class);
        map.put(2104, SzConfigurationException.class);
        map.put(2105, SzConfigurationException.class);
        map.put(2106, SzConfigurationException.class);
        map.put(2107, SzConfigurationException.class);
        map.put(2108, SzConfigurationException.class);
        map.put(2109, SzConfigurationException.class);
        map.put(2110, SzConfigurationException.class);
        map.put(2111, SzConfigurationException.class);
        map.put(2112, SzConfigurationException.class);
        map.put(2113, SzConfigurationException.class);
        map.put(2114, SzConfigurationException.class);
        map.put(2116, SzException.class);
        map.put(2117, SzConfigurationException.class);
        map.put(2118, SzConfigurationException.class);
        map.put(2120, SzConfigurationException.class);
        map.put(2121, SzConfigurationException.class);
        map.put(2123, SzConfigurationException.class);
        map.put(2131, SzConfigurationException.class);
        map.put(2135, SzConfigurationException.class);
        map.put(2136, SzConfigurationException.class);
        map.put(2137, SzConfigurationException.class);
        map.put(2138, SzConfigurationException.class);
        map.put(2139, SzConfigurationException.class);
        map.put(2207, SzUnknownDataSourceException.class);
        map.put(2209, SzConfigurationException.class);
        map.put(2210, SzConfigurationException.class);
        map.put(2211, SzConfigurationException.class);
        map.put(2212, SzConfigurationException.class);
        map.put(2213, SzConfigurationException.class);
        map.put(2214, SzConfigurationException.class);
        map.put(2215, SzConfigurationException.class);
        map.put(2216, SzConfigurationException.class);
        map.put(2217, SzConfigurationException.class);
        map.put(2218, SzConfigurationException.class);
        map.put(2219, SzConfigurationException.class);
        map.put(2220, SzConfigurationException.class);
        map.put(2221, SzConfigurationException.class);
        map.put(2222, SzConfigurationException.class);
        map.put(2223, SzConfigurationException.class);
        map.put(2224, SzConfigurationException.class);
        map.put(2225, SzConfigurationException.class);
        map.put(2226, SzConfigurationException.class);
        map.put(2227, SzConfigurationException.class);
        map.put(2228, SzConfigurationException.class);
        map.put(2230, SzConfigurationException.class);
        map.put(2231, SzConfigurationException.class);
        map.put(2232, SzConfigurationException.class);
        map.put(2233, SzConfigurationException.class);
        map.put(2234, SzConfigurationException.class);
        map.put(2235, SzConfigurationException.class);
        map.put(2236, SzConfigurationException.class);
        map.put(2237, SzConfigurationException.class);
        map.put(2238, SzConfigurationException.class);
        map.put(2239, SzConfigurationException.class);
        map.put(2240, SzConfigurationException.class);
        map.put(2241, SzConfigurationException.class);
        map.put(2242, SzConfigurationException.class);
        map.put(2243, SzConfigurationException.class);
        map.put(2244, SzConfigurationException.class);
        map.put(2245, SzConfigurationException.class);
        map.put(2246, SzConfigurationException.class);
        map.put(2247, SzConfigurationException.class);
        map.put(2248, SzConfigurationException.class);
        map.put(2249, SzConfigurationException.class);
        map.put(2250, SzConfigurationException.class);
        map.put(2251, SzConfigurationException.class);
        map.put(2252, SzConfigurationException.class);
        map.put(2253, SzConfigurationException.class);
        map.put(2254, SzConfigurationException.class);
        map.put(2255, SzConfigurationException.class);
        map.put(2256, SzConfigurationException.class);
        map.put(2257, SzConfigurationException.class);
        map.put(2258, SzConfigurationException.class);
        map.put(2259, SzConfigurationException.class);
        map.put(2260, SzConfigurationException.class);
        map.put(2261, SzConfigurationException.class);
        map.put(2262, SzConfigurationException.class);
        map.put(2263, SzConfigurationException.class);
        map.put(2264, SzConfigurationException.class);
        map.put(2266, SzConfigurationException.class);
        map.put(2267, SzConfigurationException.class);
        map.put(2268, SzConfigurationException.class);
        map.put(2269, SzConfigurationException.class);
        map.put(2270, SzConfigurationException.class);
        map.put(2271, SzConfigurationException.class);
        map.put(2272, SzConfigurationException.class);
        map.put(2273, SzConfigurationException.class);
        map.put(2274, SzConfigurationException.class);
        map.put(2275, SzConfigurationException.class);
        map.put(2276, SzConfigurationException.class);
        map.put(2277, SzConfigurationException.class);
        map.put(2278, SzConfigurationException.class);
        map.put(2279, SzConfigurationException.class);
        map.put(2280, SzConfigurationException.class);
        map.put(2281, SzConfigurationException.class);
        map.put(2282, SzConfigurationException.class);
        map.put(2283, SzConfigurationException.class);
        map.put(2285, SzException.class);
        map.put(2286, SzException.class);
        map.put(2287, SzException.class);
        map.put(2288, SzException.class);
        map.put(2289, SzConfigurationException.class);
        map.put(2290, SzConfigurationException.class);
        map.put(2291, SzConfigurationException.class);
        map.put(2292, SzException.class);
        map.put(2293, SzException.class);
        map.put(2294, SzException.class);
        map.put(3011, SzException.class);
        map.put(3101, SzException.class);
        map.put(3102, SzException.class);
        map.put(3103, SzException.class);
        map.put(3104, SzException.class);
        map.put(3110, SzException.class);
        map.put(3111, SzException.class);
        map.put(3112, SzException.class);
        map.put(3121, SzBadInputException.class);
        map.put(3122, SzBadInputException.class);
        map.put(3123, SzBadInputException.class);
        map.put(3131, SzBadInputException.class);
        map.put(7209, SzConfigurationException.class);
        map.put(7211, SzConfigurationException.class);
        map.put(7212, SzConfigurationException.class);
        map.put(7216, SzConfigurationException.class);
        map.put(7217, SzConfigurationException.class);
        map.put(7218, SzConfigurationException.class);
        map.put(7220, SzConfigurationException.class);
        map.put(7221, SzConfigurationException.class);
        map.put(7222, SzException.class);
        map.put(7223, SzConfigurationException.class);
        map.put(7224, SzConfigurationException.class);
        map.put(7226, SzConfigurationException.class);
        map.put(7227, SzConfigurationException.class);
        map.put(7228, SzConfigurationException.class);
        map.put(7230, SzConfigurationException.class);
        map.put(7232, SzConfigurationException.class);
        map.put(7233, SzConfigurationException.class);
        map.put(7234, SzConfigurationException.class);
        map.put(7235, SzConfigurationException.class);
        map.put(7236, SzConfigurationException.class);
        map.put(7237, SzConfigurationException.class);
        map.put(7238, SzException.class);
        map.put(7239, SzConfigurationException.class);
        map.put(7240, SzConfigurationException.class);
        map.put(7241, SzConfigurationException.class);
        map.put(7242, SzConfigurationException.class);
        map.put(7243, SzConfigurationException.class);
        map.put(7244, SzConfigurationException.class);
        map.put(7245, SzReplaceConflictException.class);
        map.put(7246, SzConfigurationException.class);
        map.put(7247, SzConfigurationException.class);
        map.put(7303, SzBadInputException.class);
        map.put(7305, SzBadInputException.class);
        map.put(7313, SzBadInputException.class);
        map.put(7314, SzBadInputException.class);
        map.put(7317, SzConfigurationException.class);
        map.put(7344, SzConfigurationException.class);
        map.put(7426, SzBadInputException.class);
        map.put(7511, SzException.class);
        map.put(8000, SzBadInputException.class);
        map.put(8410, SzException.class);
        map.put(8501, SzConfigurationException.class);
        map.put(8502, SzException.class);
        map.put(8503, SzException.class);
        map.put(8504, SzException.class);
        map.put(8505, SzException.class);
        map.put(8508, SzException.class);
        map.put(8509, SzException.class);
        map.put(8514, SzException.class);
        map.put(8516, SzConfigurationException.class);
        map.put(8517, SzConfigurationException.class);
        map.put(8520, SzException.class);
        map.put(8521, SzException.class);
        map.put(8522, SzConfigurationException.class);
        map.put(8524, SzException.class);
        map.put(8525, SzConfigurationException.class);
        map.put(8526, SzConfigurationException.class);
        map.put(8527, SzConfigurationException.class);
        map.put(8528, SzConfigurationException.class);
        map.put(8529, SzConfigurationException.class);
        map.put(8530, SzException.class);
        map.put(8536, SzConfigurationException.class);
        map.put(8538, SzConfigurationException.class);
        map.put(8539, SzException.class);
        map.put(8540, SzConfigurationException.class);
        map.put(8541, SzException.class);
        map.put(8542, SzException.class);
        map.put(8543, SzConfigurationException.class);
        map.put(8544, SzConfigurationException.class);
        map.put(8545, SzConfigurationException.class);
        map.put(8556, SzConfigurationException.class);
        map.put(8557, SzConfigurationException.class);
        map.put(8593, SzException.class);
        map.put(8594, SzException.class);
        map.put(8595, SzException.class);
        map.put(8598, SzException.class);
        map.put(8599, SzConfigurationException.class);
        map.put(8601, SzConfigurationException.class);
        map.put(8602, SzConfigurationException.class);
        map.put(8603, SzException.class);
        map.put(8604, SzConfigurationException.class);
        map.put(8605, SzConfigurationException.class);
        map.put(8606, SzConfigurationException.class);
        map.put(8607, SzConfigurationException.class);
        map.put(8608, SzConfigurationException.class);
        map.put(8701, SzConfigurationException.class);
        map.put(8702, SzConfigurationException.class);
        map.put(9000, SzLicenseException.class);
        map.put(9107, SzConfigurationException.class);
        map.put(9110, SzConfigurationException.class);
        map.put(9111, SzConfigurationException.class);
        map.put(9112, SzConfigurationException.class);
        map.put(9113, SzConfigurationException.class);
        map.put(9115, SzBadInputException.class);
        map.put(9116, SzConfigurationException.class);
        map.put(9117, SzConfigurationException.class);
        map.put(9118, SzConfigurationException.class);
        map.put(9119, SzConfigurationException.class);
        map.put(9120, SzConfigurationException.class);
        map.put(9210, SzConfigurationException.class);
        map.put(9220, SzConfigurationException.class);
        map.put(9222, SzConfigurationException.class);
        map.put(9224, SzConfigurationException.class);
        map.put(9228, SzConfigurationException.class);
        map.put(9240, SzConfigurationException.class);
        map.put(9241, SzConfigurationException.class);
        map.put(9250, SzConfigurationException.class);
        map.put(9251, SzConfigurationException.class);
        map.put(9252, SzConfigurationException.class);
        map.put(9253, SzConfigurationException.class);
        map.put(9254, SzConfigurationException.class);
        map.put(9255, SzConfigurationException.class);
        map.put(9256, SzConfigurationException.class);
        map.put(9257, SzConfigurationException.class);
        map.put(9258, SzConfigurationException.class);
        map.put(9259, SzConfigurationException.class);
        map.put(9260, SzConfigurationException.class);
        map.put(9261, SzConfigurationException.class);
        map.put(9264, SzConfigurationException.class);
        map.put(9265, SzConfigurationException.class);
        map.put(9266, SzConfigurationException.class);
        map.put(9267, SzException.class);
        map.put(9268, SzException.class);
        map.put(9269, SzConfigurationException.class);
        map.put(9270, SzConfigurationException.class);
        map.put(9271, SzException.class);
        map.put(9272, SzException.class);
        map.put(9273, SzException.class);
        map.put(9274, SzException.class);
        map.put(9275, SzException.class);
        map.put(9276, SzException.class);
        map.put(9277, SzException.class);
        map.put(9278, SzException.class);
        map.put(9279, SzException.class);
        map.put(9280, SzException.class);
        map.put(9281, SzException.class);
        map.put(9282, SzException.class);
        map.put(9283, SzException.class);
        map.put(9284, SzConfigurationException.class);
        map.put(9285, SzConfigurationException.class);
        map.put(9286, SzConfigurationException.class);
        map.put(9287, SzException.class);
        map.put(9288, SzException.class);
        map.put(9289, SzException.class);
        map.put(9290, SzException.class);
        map.put(9292, SzConfigurationException.class);
        map.put(9293, SzConfigurationException.class);
        map.put(9295, SzConfigurationException.class);
        map.put(9296, SzConfigurationException.class);
        map.put(9297, SzConfigurationException.class);
        map.put(9298, SzConfigurationException.class);
        map.put(9299, SzException.class);
        map.put(9300, SzConfigurationException.class);
        map.put(9301, SzConfigurationException.class);
        map.put(9305, SzException.class);
        map.put(9308, SzConfigurationException.class);
        map.put(9309, SzConfigurationException.class);
        map.put(9310, SzConfigurationException.class);
        map.put(9311, SzException.class);
        map.put(9406, SzException.class);
        map.put(9408, SzConfigurationException.class);
        map.put(9409, SzConfigurationException.class);
        map.put(9410, SzException.class);
        map.put(9411, SzException.class);
        map.put(9413, SzConfigurationException.class);
        map.put(9414, SzBadInputException.class);
        map.put(9500, SzConfigurationException.class);
        map.put(9501, SzException.class);
        map.put(9701, SzException.class);
        map.put(9802, SzConfigurationException.class);
        map.put(9803, SzConfigurationException.class);
    }
}
