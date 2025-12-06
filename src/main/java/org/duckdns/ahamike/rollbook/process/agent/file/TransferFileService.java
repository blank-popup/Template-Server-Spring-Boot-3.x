package org.duckdns.ahamike.rollbook.process.agent.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.tika.Tika;
import org.duckdns.ahamike.rollbook.config.response.BusinessException;
import org.duckdns.ahamike.rollbook.config.response.GlobalResponse;
import org.duckdns.ahamike.rollbook.config.response.ReturnCode;
import org.duckdns.ahamike.rollbook.table.TransferFileEntity;
import org.duckdns.ahamike.rollbook.util.mime.MimeTypeInfo;
import org.duckdns.ahamike.rollbook.util.mime.MimeTypeParser;
import org.duckdns.ahamike.rollbook.util.unique.UID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TransferFileService {
    private final TransferFileRepository transferFileRepository;

    @Value("#{environment['directory.attatchment.root']}")
    private String fileRootDirectory;

    public GlobalResponse<TransferFileEntity> upload(String directory0, String directory1, String directory2, String description, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String filename = null;

        MimeTypeInfo mimeTypeInfo = null;
        Path subDirectory = null;
        Path targetPath = null;

        if (directory0 == null || directory0.isBlank() == true) {
            subDirectory = Paths.get("./");
        }
        else if (directory1 == null || directory1.isBlank() == true) {
            subDirectory = Paths.get(directory0);
        }
        else if (directory2 == null || directory2.isBlank() == true) {
            subDirectory = Paths.get(directory0, directory1);
        }
        else {
            subDirectory = Paths.get(directory0, directory1, directory2);
        }

        try {
            Path directory = Paths.get(fileRootDirectory, subDirectory.toString());
            Files.createDirectories(directory);
            while (true) {
                filename = UID.generateType4UUID().toString();
                targetPath = directory.resolve(filename).normalize();
                if (targetPath.toFile().exists() == false) {
                    break;
                }
            }
            log.info("targetPath : {}", targetPath);

            // file.transferTo(targetPath);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String mimeType = new Tika().detect(targetPath);
            mimeTypeInfo = MimeTypeParser.parse(mimeType);

        } catch (Exception e) {
            log.warn("Exception in save file: {}", e.getMessage());
            throw new BusinessException(ReturnCode.FAIL_TO_SAVE_FILE, e.getMessage());
        }

        TransferFileEntity transferFile = new TransferFileEntity(
            subDirectory.toString(),
            filename,
            originalFilename,
            description,
            directory0,
            directory1,
            directory2,
            mimeTypeInfo.getType(),
            mimeTypeInfo.getSubType(),
            mimeTypeInfo.getParameters().toString()
        );

        try {
            TransferFileEntity response = transferFileRepository.save(transferFile);

            ReturnCode code = ReturnCode.OK;

            return new GlobalResponse<>(code, response);
        } catch (Exception e) {
            log.warn("Exception in save file info to DB: {}", e.getMessage());

            if (targetPath != null) {
                File deleting = targetPath.toFile();
                if (deleting.exists() == true && deleting.isFile() == true) {
                    if (deleting.delete() == true) {
                        log.warn("Succeed to delete file: {}", targetPath.toString());
                    }
                    else {
                        log.warn("Failed to delete file: {}", targetPath.toString());
                    }
                }
            }

            throw new BusinessException(ReturnCode.FAIL_TO_SAVE_FILE, e.getMessage());
        }
    }

    public void download(HttpServletResponse response, Long transferFileId) {
        TransferFileEntity transferFile = transferFileRepository.findById(transferFileId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "File does not exist in database"));

        Path path = Paths.get(fileRootDirectory, transferFile.getSubDirectory(), transferFile.getFilename());
        File file = path.toFile();
        if (file.exists() == false || file.isDirectory() == true) {
            throw new BusinessException(ReturnCode.NO_SUCH_DATA, "File does not exist in storage");
        }

        String originalFilename = transferFile.getOriginalFilename();
        if (originalFilename == null || "".equals(originalFilename) == true) {
            throw new BusinessException(ReturnCode.NO_ESSENTIAL_DATA, "Original filename is not valid");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + originalFilename);

        try {
            FileInputStream fileInputStream = new FileInputStream(path.toString());
            OutputStream out = response.getOutputStream();

            int read = 0;
            byte[] buffer = new byte[4096];
            while ((read = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            fileInputStream.close();

        } catch (Exception e) {
            log.error("Exception: {}", e.toString());
            throw new BusinessException(ReturnCode.FILE_IO_ERROR, e.toString());
        }
    }
}
