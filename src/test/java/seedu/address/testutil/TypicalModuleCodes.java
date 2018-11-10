package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * A utility class containing a list of {@code moduleCodes} objects to be used in tests.
 */
public class TypicalModuleCodes {
    public static final String CS2100_MODULE_CODE = "CS2100";
    public static final String CS2101_MODULE_CODE = "CS2101";
    public static final String CS2102_MODULE_CODE = "CS2102";
    public static final String CS2103_MODULE_CODE = "CS2103";
    public static final String CS2104_MODULE_CODE = "CS2104";
    public static final String CS2105_MODULE_CODE = "CS2105";
    public static final String CS2106_MODULE_CODE = "CS2106";
    public static final String CS2107_MODULE_CODE = "CS2107";

    // Manually added
    public static final String CS3100_MODULE_CODE = "CS3100";
    public static final String CS3235_MODULE_CODE = "CS3235";
    public static final String CS5240_MODULE_CODE = "CS5240";

    // Module Prefixes
    public static final String CS_LEVEL_2000_MODULE_CODE = "CS2";

    private TypicalModuleCodes() {} // prevents instantiation

    /**
     * Returns a {@code Set<String>} with all the typical moduleCodes.
     */
    public static Set<String> getAllTypicalModuleCodes() {
        Set<String> mc = new TreeSet<>();
        for (String notes : getAllTypicalCodes()) {
            mc.add(notes);
        }
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with all the moduleCodes not typically in the set
     * returned by {@code getAllTypicalCodes}.
     */
    public static Set<String> getAllDifferentModuleCodes() {
        Set<String> mc = new TreeSet<>();
        for (String notes : getAllDifferentCodes()) {
            mc.add(notes);
        }
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with no moduleCode.
     */
    public static Set<String> getZeroModuleCodes() {
        Set<String> mc = new TreeSet<>();
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with one typical moduleCode.
     */
    public static Set<String> getOneTypicalModuleCode() {
        Set<String> mc = new TreeSet<>();
        mc.add(CS2100_MODULE_CODE);
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with one typical module prefix.
     */
    public static Set<String> getOneTypicalModulePrefix() {
        Set<String> mc = new TreeSet<>();
        mc.add(CS_LEVEL_2000_MODULE_CODE);
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with one moduleCode not typically in the set
     * returned by {@code getAllTypicalCodes}.
     */
    public static Set<String> getOneDifferentModuleCode() {
        Set<String> mc = new TreeSet<>();
        mc.add(CS3100_MODULE_CODE);
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with three typical moduleCodes.
     */
    public static Set<String> getMultipleTypicalModuleCodes() {
        Set<String> mc = new TreeSet<>();
        mc.add(CS2100_MODULE_CODE);
        mc.add(CS2101_MODULE_CODE);
        mc.add(CS2102_MODULE_CODE);
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with three moduleCodes not typically in the set
     * returned by {@code getAllTypicalCodes}.
     */
    public static Set<String> getMultipleDifferentModuleCodes() {
        Set<String> mc = new TreeSet<>();
        for (String notes : getAllDifferentCodes()) {
            mc.add(notes);
        }
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with one typical moduleCode, and one moduleCode not typically in the set
     * returned by {@code getAllTypicalCodes}.
     */
    public static Set<String> getSimpleMixedValidityModuleCodes() {
        Set<String> mc = new TreeSet<>();
        mc.add(CS2100_MODULE_CODE);
        mc.add(CS3100_MODULE_CODE);
        return mc;
    }

    /**
     * Returns a {@code Set<String>} with multiple typical moduleCode, and multiple moduleCodes not typically in the
     * set returned by {@code getAllTypicalCodes}.
     */
    public static Set<String> getAdvancedMixedValidityModuleCodes() {
        Set<String> mc = new TreeSet<>();
        for (String notes : getAllTypicalCodes()) {
            mc.add(notes);
        }
        for (String notes : getAllDifferentCodes()) {
            mc.add(notes);
        }
        return mc;
    }

    public static List<String> getAllTypicalCodes() {
        return new ArrayList<>(Arrays.asList(CS2100_MODULE_CODE, CS2101_MODULE_CODE, CS2102_MODULE_CODE,
                                                CS2103_MODULE_CODE, CS2104_MODULE_CODE, CS2105_MODULE_CODE,
                                                CS2106_MODULE_CODE, CS2107_MODULE_CODE));
    }

    /**
     * Returns a {@code List<String>} with all moduleCodes not typically in the
     * set returned by {@code getAllTypicalCodes}.
     */
    public static List<String> getAllDifferentCodes() {
        return new ArrayList<>(Arrays.asList(CS3100_MODULE_CODE, CS3235_MODULE_CODE, CS5240_MODULE_CODE));
    }


}
