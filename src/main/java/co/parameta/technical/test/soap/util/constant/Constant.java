package co.parameta.technical.test.soap.util.constant;

/**
 * Centralized constants used by the SOAP layer of the application.
 * <p>
 * This class defines SOAP configuration values, WSDL metadata,
 * response codes, messages, and common calculation keys.
 */
public final class Constant {

    private Constant() {
    }

    /** JAXB context path for generated SOAP classes. */
    public static final String CONTEXT_PATH = "com.parameta.technical.test.soap.gen";

    /** URL mapping used to expose SOAP services. */
    public static final String URL_MAPPINGS = "/ws/*";

    /** Base URI where the SOAP service is published. */
    public static final String LOCATION_URI = "/ws";

    /** SOAP port type name defined in the WSDL. */
    public static final String PORT_TYPE_NAME = "EmployeePort";

    /** Bean name used to expose the SOAP service definition. */
    public static final String NAME_BEAN_SOAP = "employee";

    /** XSD file name used to generate the WSDL. */
    public static final String XSD_FILE = "employee.xsd";

    /** XML namespace used across SOAP requests and responses. */
    public static final String NAMESPACE_URI =
            "http://parameta.co/technical/test/employee";

    /** Successful operation status code. */
    public static final String STATUS_SUCCESS = "200";

    /** Message returned when an employee is successfully created. */
    public static final String EMPLOYEE_SAVED_SUCCESSFULLY =
            "Employee saved successfully";

    /** Key representing years in time difference calculations. */
    public static final String YEARS = "years";

    /** Key representing months in time difference calculations. */
    public static final String MONTHS = "months";

    /** Key representing days in time difference calculations. */
    public static final String DAYS = "days";

    /** Internal error status code. */
    public static final String STATUS_INTERNAL_ERROR = "500";

    /** Message returned when employee information is successfully updated. */
    public static final String EMPLOYEE_UPDATED_SUCCESSFULLY =
            "Employee information was updated successfully";

    /** Message returned when updates are disabled for existing employees. */
    public static final String UPDATE_NOT_ALLOWED =
            "Employee already exists and update is not allowed";
}
