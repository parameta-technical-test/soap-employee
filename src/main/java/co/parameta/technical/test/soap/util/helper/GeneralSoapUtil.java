package co.parameta.technical.test.soap.util.helper;

import co.parameta.technical.test.commons.pojo.AdditionalEmployeeInformationPojo;
import co.parameta.technical.test.commons.pojo.ExtraInformationPojo;
import com.parameta.technical.test.soap.gen.ExtraInformation;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

import static co.parameta.technical.test.soap.util.constant.Constant.*;

/**
 * Utility helper class for SOAP-related operations.
 * <p>
 * Provides common conversion methods between SOAP, XML, and Java types,
 * as well as helpers for null validation and building additional
 * employee information structures.
 */
public final class GeneralSoapUtil {

    private GeneralSoapUtil() {
        // Utility class
    }

    /**
     * Converts an {@link XMLGregorianCalendar} into a {@link Date}.
     *
     * @param xmlGregorianCalendar XML calendar value
     * @return converted {@link Date} or {@code null} if input is null
     */
    public static Date toDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

    /**
     * Converts a {@link Date} into an {@link XMLGregorianCalendar}.
     *
     * @param date Java date
     * @return XMLGregorianCalendar representation or {@code null} if input is null
     * @throws IllegalStateException if conversion fails
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        if (date == null) {
            return null;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        try {
            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(
                    "Error converting Date to XMLGregorianCalendar", e
            );
        }
    }

    /**
     * Evaluates whether an object is considered null or empty.
     * <p>
     * Supports {@link String}, {@link Collection}, {@link Map},
     * and numeric identifiers.
     *
     * @param value value to evaluate
     * @return {@code true} if the value is null or empty
     */
    public static boolean isNullOrBlank(Object value) {
        if (value == null) {
            return true;
        }

        if (value instanceof String str) {
            return str.trim().isEmpty();
        }

        if (value instanceof Collection<?> col) {
            return col.isEmpty();
        }

        if (value instanceof Map<?, ?> map) {
            return map.isEmpty();
        }

        if (value instanceof Integer val) {
            return val <= 0;
        }

        return false;
    }

    /**
     * Builds the additional employee information block used in SOAP responses.
     *
     * @param timeAtCompany map containing years, months, and days at the company
     * @param ageEmployee map containing years, months, and days of age
     * @return populated {@link AdditionalEmployeeInformationPojo}
     */
    public static AdditionalEmployeeInformationPojo buildAdditionalInformation(
            Map<String, Integer> timeAtCompany,
            Map<String, Integer> ageEmployee
    ) {
        AdditionalEmployeeInformationPojo info =
                new AdditionalEmployeeInformationPojo();
        info.setTimeLinkedToCompany(toExtraInformationPojo(timeAtCompany));
        info.setCurrentAgeEmployee(toExtraInformationPojo(ageEmployee));
        return info;
    }

    /**
     * Converts a time-difference map into an {@link ExtraInformationPojo}.
     *
     * @param data map containing years, months, and days
     * @return populated {@link ExtraInformationPojo}
     */
    public static ExtraInformationPojo toExtraInformationPojo(
            Map<String, Integer> data
    ) {
        ExtraInformationPojo extra = new ExtraInformationPojo();
        extra.setYears(data.get(YEARS));
        extra.setMonths(data.get(MONTHS));
        extra.setDays(data.get(DAYS));
        return extra;
    }

    /**
     * Converts a time-difference map into a SOAP {@link ExtraInformation} object.
     *
     * @param data map containing years, months, and days
     * @return populated SOAP {@link ExtraInformation}
     */
    public static ExtraInformation toExtraInformation(
            Map<String, Integer> data
    ) {
        ExtraInformation extra = new ExtraInformation();
        extra.setYears(data.get(YEARS));
        extra.setMonths(data.get(MONTHS));
        extra.setDays(data.get(DAYS));
        return extra;
    }
}
