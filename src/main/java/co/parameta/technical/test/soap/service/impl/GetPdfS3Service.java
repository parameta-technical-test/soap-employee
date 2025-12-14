package co.parameta.technical.test.soap.service.impl;

import co.parameta.technical.test.soap.service.IGetPdfS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

/**
 * Service responsible for retrieving PDF files stored in Amazon S3.
 * <p>
 * This implementation uses the AWS SDK v2 {@link S3Client} to download
 * PDF documents as byte arrays, typically used for employee reports.
 */
@Service
@RequiredArgsConstructor
public class GetPdfS3Service implements IGetPdfS3Service {

    /**
     * AWS S3 client used to access the bucket.
     */
    private final S3Client s3Client;

    /**
     * Name of the S3 bucket where PDF files are stored.
     */
    @Value("${aws.s3.bucket}")
    private String bucket;

    /**
     * Retrieves a PDF file from Amazon S3 using its object key.
     *
     * @param key S3 object key that identifies the PDF file
     * @return byte array containing the PDF content
     * @throws RuntimeException if the file cannot be read from S3
     */
    @Override
    public byte[] getPdf(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object =
                     s3Client.getObject(request)) {

            return s3Object.readAllBytes();

        } catch (IOException e) {
            throw new RuntimeException("Error reading PDF from S3", e);
        }
    }
}
