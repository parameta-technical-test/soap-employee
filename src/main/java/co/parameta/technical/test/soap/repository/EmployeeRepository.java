package co.parameta.technical.test.soap.repository;

import co.parameta.technical.test.commons.dto.EmployeeDTO;
import co.parameta.technical.test.commons.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository responsible for database operations related to {@link EmployeeEntity}.
 * <p>
 * This repository provides both standard CRUD operations and
 * custom queries for employee validation, update, and information retrieval.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    /**
     * Updates employee information only when at least one field value has changed.
     * <p>
     * This native query prevents unnecessary updates by comparing current
     * database values with the incoming {@link EmployeeDTO}.
     *
     * @param emp        employee data transfer object containing updated values
     * @param idEmployee employee identifier
     */
    @Modifying
    @Query(value = """
        UPDATE technical_test.employee e
        SET
            e.names = :#{#emp.names},
            e.last_names = :#{#emp.lastNames},
            e.code_type_document = :#{#emp.typeDocument.code},
            e.document_number = :#{#emp.documentNumber},
            e.date_of_birth = :#{#emp.dateOfBirth},
            e.date_affiliation_company = :#{#emp.dateAffiliationCompany},
            e.code_position = :#{#emp.position.code},
            e.salary = :#{#emp.salary},
            e.code_administrator_user = :#{#emp.administratorUser.code}
        WHERE e.id = :idEmployee
          AND (
                IFNULL(e.names, '') <> IFNULL(:#{#emp.names}, '')
             OR IFNULL(e.last_names, '') <> IFNULL(:#{#emp.lastNames}, '')
             OR e.code_type_document <> :#{#emp.typeDocument.code}
             OR e.document_number <> :#{#emp.documentNumber}
             OR e.date_of_birth <> :#{#emp.dateOfBirth}
             OR e.date_affiliation_company <> :#{#emp.dateAffiliationCompany}
             OR e.code_position <> :#{#emp.position.code}
             OR e.salary <> :#{#emp.salary}
             OR e.code_administrator_user <> :#{#emp.administratorUser.code}
          )
        """, nativeQuery = true)
    void updateInformationEmployee(
            @Param("emp") EmployeeDTO emp,
            @Param("idEmployee") Integer idEmployee
    );

    /**
     * Checks if an employee exists based on document number and document type.
     * <p>
     * The document type can be matched either by code or description.
     *
     * @param documentNumber employee document number
     * @param typeDocument   document type code or description
     * @return {@code 1} if the employee exists, {@code 0} otherwise
     */
    @Query(value = """
            SELECT CASE
                     WHEN EXISTS (
                       SELECT 1
                       FROM technical_test.employee e
                       INNER JOIN technical_test.type_document td
                           ON e.code_type_document = td.code
                       WHERE e.document_number = :documentNumber
                         AND (td.code = :typeDocument
                              OR td.description = :typeDocument)
                     )
                     THEN 1
                     ELSE 0
                   END AS exists_flag;
            """, nativeQuery = true)
    int searchEmployeeExistence(
            @Param("documentNumber") String documentNumber,
            @Param("typeDocument") String typeDocument
    );

    /**
     * Retrieves the employee identifier based on document number and document type code.
     *
     * @param documentNumber employee document number
     * @param typeDocument   document type code
     * @return employee ID if found, otherwise {@code null}
     */
    @Query("""
            SELECT e.id
            FROM EmployeeEntity e
            WHERE e.documentNumber = :documentNumber
              AND e.typeDocument.code = :typeDocument
            """)
    Integer searchIdEmployee(
            @Param("documentNumber") String documentNumber,
            @Param("typeDocument") String typeDocument
    );

    /**
     * Retrieves complete employee information using either:
     * <ul>
     *   <li>Employee ID</li>
     *   <li>Document number and document type</li>
     * </ul>
     *
     * All parameters are optional, but at least one valid filter
     * must be provided to return a result.
     *
     * @param idEmployee     employee identifier (optional)
     * @param numberDocument employee document number (optional)
     * @param typeDocument   document type code or description (optional)
     * @return {@link EmployeeEntity} with full information or {@code null} if not found
     */
    @Query("""
        SELECT e
        FROM EmployeeEntity e
        WHERE
          (:idEmployee IS NULL OR e.id = :idEmployee)
        AND
          (
            (:numberDocument IS NULL AND :typeDocument IS NULL)
            OR
            (
              :numberDocument IS NOT NULL
              AND :typeDocument IS NOT NULL
              AND e.documentNumber = :numberDocument
              AND (
                e.typeDocument.code = :typeDocument
                OR e.typeDocument.description = :typeDocument
              )
            )
          )
        """)
    EmployeeEntity searchAllInformationEmployee(
            @Param("idEmployee") Integer idEmployee,
            @Param("numberDocument") String numberDocument,
            @Param("typeDocument") String typeDocument
    );

}
