package pl.czajkowski.devconnect.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final String BUCKET_NAME = "devconnect-bucket";
    private final S3Client client;

    public S3Service(S3Client client) {
        this.client = client;
    }

    public void uploadFile(String key, byte[] file) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        client.putObject(request, RequestBody.fromBytes(file));
    }

    public byte[] downloadFile(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        try {
            return client.getObject(request).readAllBytes();
        } catch (IOException e) {
            // TODO: custom exception
            throw new RuntimeException(e);
        }
    }
}
