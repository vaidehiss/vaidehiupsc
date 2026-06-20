package in.nic.upscora.graphql.form4.cache;

/**
 * Enterprise constants for Master Data Cache buckets.
 */
public final class MasterDataConstants {
    
    private MasterDataConstants() {}

    public static final String STATES = "states";
    public static final String DISTRICTS = "districts";
    public static final String NATIONALITIES = "nationalities";
    public static final String MINORITY_GROUPS = "minorityGroups";
    public static final String MARITAL_STATUSES = "maritalStatuses";
    public static final String FOREIGN_COUNTRY = "foreignCountry";
    public static final String QUALIFICATION_LEVELS = "qualificationLevels";
    public static final String BOARDS_UNIVERSITIES = "boardsUniversities";
    public static final String MSR_EDUCATION_BOARDS = "msrEducationBoards";
    public static final String GRADING_SYSTEMS = "gradingSystems";
    public static final String GRADE_TYPES = "gradeTypes";
    public static final String GENDERS = "genders";
    public static final String EMPLOYMENT_TYPES = "employmentTypes";
    public static final String WORKED_AT_TYPES = "workedAtTypes";
    public static final String PARENT_OCCUPATIONS = "parentOccupations";
    public static final String BIRTH_PLACES = "birthPlaces";
    
    // Disabilities and specialized nested caches
    public static final String DISABILITY_TYPES = "disabilityTypes";
    public static final String DISABILITIES = "disabilities";
    public static final String SID_CACHE = "sidcache";
    public static final String WID_CACHE = "widcache";
    public static final String COMMUNITIES = "communities";
    
    // Fallback constants
    public static final String UNKNOWN = "Unknown";
}
