package co.parameta.technical.test.soap.service;

/**
 * Service interface responsible for retrieving PDF documents from Amazon S3.
 * <p>
 * This service abstracts the access to stored employee PDF reports,
 * allowing SOAP services to obtain the binary content using the
 * storage key associated with the employee.
 */
public interface IGetPdfS3Service {

    /**
     * Retrieves a PDF file from Amazon S3 using its storage key.
     *
     * @param key the S3 object key where the PDF is stored
     * @return the PDF content as a byte array
     */
    byte[] getPdf(String key);

}
