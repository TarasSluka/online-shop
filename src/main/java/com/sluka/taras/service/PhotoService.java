package com.sluka.taras.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface PhotoService {

    List<String> getAllURLToPhotoProduct(Long id) throws IOException;

    String getURLToPhotoUser(Long id) throws IOException;

    String noImageToUser();

    Path getPathToFolderProduct(Long id);

    String getURLToAvaProduct(Long id) throws IOException;

    public void savePhotoUser(Long id, MultipartFile multipartFile) throws IOException;

    String noImageToProduct();

    List<String> getAllFileNameInFolder(Path path);

    void deleteDirectoryWithFileProduct(Long id) throws IOException;

    boolean createDirectory(Path directory);

    boolean deletePhotoProduct(Long id, String name);

    void savePhotoProduct(Long id, List<MultipartFile> multipartFiles) throws IOException;

    Path getPath(String path);

    boolean isExist(String path) throws IOException;
}
