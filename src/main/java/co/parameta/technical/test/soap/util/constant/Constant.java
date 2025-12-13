package co.parameta.technical.test.soap.util.constant;

public final class Constant {

    private Constant() {
    }

    public static final String CONTEXT_PATH = "com.parameta.technical.test.soap.gen";
    public static final String URL_MAPPINGS = "/ws/*";
    public static final String LOCATION_URI = "/ws";
    public static final String PORT_TYPE_NAME = "EmployeePort";
    public static final String NAME_BEAN_SOAP = "employee";
    public static final String XSD_FILE = "employee.xsd";
    public static final String NAMESPACE_URI =
            "http://parameta.co/technical/test/employee";
    public static final String STATUS_SUCCESS = "200";
    public static final String EMPLOYEE_SAVED_SUCCESSFULLY = "Employee saved successfully";

    public static final String YEARS = "years";
    public static final String MONTHS = "months";
    public static final String DAYS = "days";
    public static final String STATUS_INTERNAL_ERROR = "500";

    public static final String EMPLOYEE_UPDATED_SUCCESSFULLY =
            "Employee information was updated successfully";

    public static final String EMPLOYEE_ALREADY_EXISTS =
            "An employee with the same document already exists in the system";

    public static final String UPDATE_NOT_ALLOWED =
            "Employee already exists and update is not allowed";
}
