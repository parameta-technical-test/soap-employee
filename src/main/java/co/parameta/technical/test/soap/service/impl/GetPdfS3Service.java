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

@Service
@RequiredArgsConstructor
public class GetPdfS3Service implements IGetPdfS3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

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
