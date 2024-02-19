package pl.czajkowski.devconnect.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.czajkowski.devconnect.exception.ResourceNotFoundException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    private final String BUCKET_NAME = "devconnect-bucket";
    @Mock
    private S3Client client;
    private S3Service underTest;

    @BeforeEach
    void setUp() {
        underTest = new S3Service(client);
    }

    @Test
    void shouldUploadFile() throws Exception {
        String key = "user";
        byte[] file = "Hello".getBytes();

        underTest.uploadFile(key, file);

        ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor =
                ArgumentCaptor.forClass(PutObjectRequest.class);

        ArgumentCaptor<RequestBody> requestBodyArgumentCaptor =
                ArgumentCaptor.forClass(RequestBody.class);

        verify(client).putObject(
                putObjectRequestArgumentCaptor.capture(),
                requestBodyArgumentCaptor.capture()
        );

        PutObjectRequest putObjectRequest = putObjectRequestArgumentCaptor.getValue();
        RequestBody requestBody = requestBodyArgumentCaptor.getValue();

        assertThat(putObjectRequest.key()).isEqualTo(key);
        assertThat(requestBody.contentStreamProvider().newStream().readAllBytes())
                .isEqualTo(RequestBody.fromBytes(file).contentStreamProvider().newStream().readAllBytes());
    }

    @Test
    void shouldDownloadFile() throws Exception {
        String key = "key";
        byte[] file = "Hello".getBytes();

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> response = mock(ResponseInputStream.class);
        when(response.readAllBytes()).thenReturn(file);

        when(client.getObject(eq(request))).thenReturn(response);

        byte[] bytes = underTest.downloadFile(key);

        assertThat(bytes).isEqualTo(file);
    }

    @Test
    void shouldThrowWhenFileIsNotFound() throws Exception {
        String key = "key";
        byte[] file = "Hello".getBytes();

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> response = mock(ResponseInputStream.class);
        when(response.readAllBytes()).thenThrow(IOException.class);

        when(client.getObject(eq(request))).thenReturn(response);

        assertThatThrownBy(() -> underTest.downloadFile(key))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Image not found");
    }
}