package ca.bazlur.fileuploddemo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${aws.s3.access-key-id}")
    private String s3AccessKeyId;

    @Value("${aws.s3.secret-access-key}")
    private String secretAccessKey;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final DocumentRepository documentRepository;

    private final Path rootPath = Path.of("uploads"); //replace with your own path

    public void saveFileInFileSystem(MultipartFile file) throws IOException {
        log.info("Uploading file to local file system: {}", file.getOriginalFilename());

        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            String filenameWithExtension = Paths.get(file.getOriginalFilename()).getFileName().toString();
            Path path = rootPath.resolve(filenameWithExtension);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void saveFileInDatabase(MultipartFile file) throws IOException {
        Document doc = new Document(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        documentRepository.save(doc);
    }

    //save file in aws
    public void saveFileToAmazonS3(MultipartFile multipartFile) throws IOException {
        log.info("Uploading file to s3: {}", multipartFile.getOriginalFilename());
        var s3Client = getS3Client();

        var metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        var keyName = buildKeyName(multipartFile);
        var results = s3Client.putObject(bucketName, keyName, multipartFile.getInputStream(), metadata);
        if (results != null && StringUtils.isNotBlank(results.getContentMd5())) {
            log.info("File uploaded successfully to s3: {}", multipartFile.getOriginalFilename());
        } else {
            log.error("Failed to upload file to s3: {}", multipartFile.getOriginalFilename());
            throw new RuntimeException("Failed to upload file to s3");
        }
    }

    // build key based on your own logic
    private String buildKeyName(MultipartFile multipartFile) {
        return UUID.randomUUID() + "/" + multipartFile.getOriginalFilename();
    }

    private AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(s3AccessKeyId, secretAccessKey)))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

}
