package co.parameta.technical.test.soap.service;

import co.parameta.technical.test.soap.service.impl.GetPdfS3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPdfS3ServiceTest {

    @InjectMocks
    private GetPdfS3Service service;

    @Mock
    private S3Client s3Client;

    private static final String BUCKET = "test-bucket";

    @BeforeEach
    void setUp() throws Exception {
        var field = GetPdfS3Service.class.getDeclaredField("bucket");
        field.setAccessible(true);
        field.set(service, BUCKET);
    }

    @Test
    void getPdfReturnsBytes() throws Exception {
        byte[] expectedBytes = "PDF_CONTENT".getBytes();

        GetObjectResponse response = GetObjectResponse.builder().build();
        ResponseInputStream<GetObjectResponse> inputStream =
                new ResponseInputStream<>(
                        response,
                        new ByteArrayInputStream(expectedBytes)
                );

        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenReturn(inputStream);

        byte[] result = service.getPdf("pdf/test.pdf");

        assertNotNull(result);
        assertArrayEquals(expectedBytes, result);

        ArgumentCaptor<GetObjectRequest> captor =
                ArgumentCaptor.forClass(GetObjectRequest.class);

        verify(s3Client, times(1)).getObject(captor.capture());

        GetObjectRequest req = captor.getValue();
        assertEquals(BUCKET, req.bucket());
        assertEquals("pdf/test.pdf", req.key());
    }

    @Test
    void getPdfIOExceptionThrowsRuntimeException() throws Exception {
        ResponseInputStream<GetObjectResponse> stream =
                mock(ResponseInputStream.class);

        when(stream.readAllBytes())
                .thenThrow(new IOException("S3 error"));

        when(s3Client.getObject(any(GetObjectRequest.class)))
                .thenReturn(stream);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.getPdf("pdf/error.pdf")
        );

        assertTrue(ex.getMessage().contains("Error reading PDF from S3"));

        verify(s3Client, times(1))
                .getObject(any(GetObjectRequest.class));
    }
}