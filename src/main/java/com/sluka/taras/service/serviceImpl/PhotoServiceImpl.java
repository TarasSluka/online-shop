package com.sluka.taras.service.serviceImpl;

import com.sluka.taras.common.model.Product;
import com.sluka.taras.service.PhotoService;
import com.sluka.taras.service.ProductService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final Logger logger = LogManager.getLogger(PhotoServiceImpl.class);
    private final String NO_IMAGE = "noImage.jpg";
    private final String PATH_TO_IMAGE_PRODUCT;
    private final String PATH_TO_IMAGE_USER;
    private final String BASE_URL_TO_IMAGE_PRODUCT;
    private final String BASE_URL_TO_IMAGE_USER;
    private final String IMAGE_TYPE[] = new String[]{".jpg", ".png"};
    private final Environment env;
    private final ProductService productService;

    @Autowired
    public PhotoServiceImpl(Environment env, @Lazy ProductService productService) {
        this.productService = productService;
        this.env = env;

        PATH_TO_IMAGE_USER = env.getProperty("localPathToUserImage");/*"D:/shop/images/user/";*/
        PATH_TO_IMAGE_PRODUCT = env.getProperty("localPathToProductImage");/*"D:/shop/images/product/";*/

        BASE_URL_TO_IMAGE_USER = "/photo/user/";
        BASE_URL_TO_IMAGE_PRODUCT = "/photo/product/";
    }


    public List<String> getAllURLToPhotoProduct(Long id) throws IOException {
        List<String> urlList = new ArrayList<>();
        Path path = getPathToFolderProduct(id);
        if (isExist(path)) {
            urlList.addAll(getUrlToImage(BASE_URL_TO_IMAGE_PRODUCT, id, getAllFileNameInFolder(path)));
        } else {
            urlList.add(noImageToProduct());
        }
        return urlList;
    }

    public List<String> getUrlToImage(String baseUrl, Long id, List<String> nameFile) {
        List<String> strings = new ArrayList<>();
        nameFile.forEach(item -> {
            strings.add(baseUrl + id + "/" + item);
        });
        return strings;
    }

    public String getUrlToImage(String baseUrl, Long id, String nameFile) {
        return baseUrl + id + "/" + nameFile;
    }


    public String getURLToPhotoUser(Long id) throws IOException {
        String url = null;
        String name1 = id + IMAGE_TYPE[0];
        String name2 = id + IMAGE_TYPE[1];
        Path path1 = getPath(PATH_TO_IMAGE_USER + name1);
        Path path2 = getPath(PATH_TO_IMAGE_USER + name2);
        if (Files.exists(path1) && Files.isRegularFile(path1)) {
            url = BASE_URL_TO_IMAGE_USER + name1;
        } else if (Files.exists(path2) && Files.isRegularFile(path2)) {
            url = BASE_URL_TO_IMAGE_USER + name2;
        } else {
            url = noImageToUser();
        }
        return url;
    }

    public String noImageToProduct() {
        return "/images/" + NO_IMAGE;
    }

    public String noImageToUser() {
        return "/images/users/anonymous/user-512x512.png";
    }

    public Path getPathToFolderProduct(Long id) {
        String path = PATH_TO_IMAGE_PRODUCT + id;
        return Paths.get(path);
    }

    public List<String> getAllFileNameInFolder(Path path) {
        List<String> strings = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            for (Path paths : directoryStream)
                strings.add(paths.getFileName().toString());
        } catch (IOException ex) {
        }
        return strings;
    }

    public boolean deletePhotoProduct(Long id, String name) {
        Path path = getPath(PATH_TO_IMAGE_PRODUCT + id + "/" + name);

        boolean result = false;
        try {
            result = Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void deleteDirectoryWithFileProduct(Long id) throws IOException {
        Path path = getPath(PATH_TO_IMAGE_PRODUCT + id);
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public String getURLToAvaProduct(Long id) throws IOException {
        Product product = productService.findById(id);
        String url = null;
        if (product != null && product.getAvaName() != null && !product.getAvaName().isEmpty()) {
            String path = PATH_TO_IMAGE_PRODUCT + String.valueOf(id) + "/" + product.getAvaName();
            if (Files.exists(getPath(path)) && Files.isRegularFile(getPath(path))) {
                List<String> strings = new ArrayList<String>();
                url = getUrlToImage(BASE_URL_TO_IMAGE_PRODUCT, id, product.getAvaName());
            } else if (Files.exists(getPath(PATH_TO_IMAGE_PRODUCT + String.valueOf(id)))) {
                List<String> nameList = getAllFileNameInFolder(getPath(PATH_TO_IMAGE_PRODUCT + String.valueOf(id)));
                if (nameList.size() > 0)
                    url = getUrlToImage(BASE_URL_TO_IMAGE_PRODUCT, id, nameList.get(0));
                url = noImageToProduct();
            }
        } else
            url = noImageToProduct();
        return url;
    }

    public boolean createDirectory(final Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                return true;
            } catch (FileAlreadyExistsException e) {
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void savePhotoProduct(Long id, List<MultipartFile> multipartFiles) throws IOException {
        String pathStr = PATH_TO_IMAGE_PRODUCT + id;
        if (!isExist(pathStr))
            createDirectory(getPath(pathStr));
        multipartFiles.forEach(file -> {
            try {
                Path path = Paths.get(pathStr + "\\" + file.getOriginalFilename());
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void savePhotoUser(Long id, MultipartFile multipartFile) throws IOException {
        String pathStr = PATH_TO_IMAGE_USER;
        if (multipartFile != null) {
            if (!isExist(pathStr))
                createDirectory(getPath(pathStr));
            String name = String.valueOf(id) + "." + multipartFile.getOriginalFilename().split("\\.")[1];
            Files.deleteIfExists(getPath(PATH_TO_IMAGE_USER + String.valueOf(id) + IMAGE_TYPE[0]));
            Files.deleteIfExists(getPath(PATH_TO_IMAGE_USER + String.valueOf(id) + IMAGE_TYPE[1]));
            try {
                Path path = Paths.get(pathStr + "\\" + name);
                Files.write(path, multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Path getPath(String path) {
        return Paths.get(path);
    }


    public boolean isExist(Path path) throws IOException {
        return Files.exists(path,
                new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    }

    public boolean isExist(String path) throws IOException {
        return Files.exists(getPath(path),
                new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
    }
}