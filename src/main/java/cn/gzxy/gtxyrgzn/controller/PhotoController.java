package cn.gzxy.gtxyrgzn.controller;

import cn.gzxy.gtxyrgzn.annotation.PermissionCheck;
import cn.gzxy.gtxyrgzn.config.AppConfig;
import cn.gzxy.gtxyrgzn.service.RunningService;
import cn.gzxy.gtxyrgzn.utils.RandomUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@RestController
@RequiredArgsConstructor
public class PhotoController {
    final AppConfig appConfig;
    final RunningService runningService;

    @GetMapping("/processed/{id}")
    public Object processed(HttpServletResponse response, @PathVariable String id) {
        File file = new File(String.format("processed/%s.jpg", id));
        if (!file.exists()) {
            throw new IllegalArgumentException("Not found: " + path);
        }

        response.reset();
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");

        try (InputStream is = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());

            byte[] buffer = new byte[1024];
            int len;

            while ((len = is.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @PermissionCheck
    @PostMapping("/upload")
    public Object upload(
            HttpServletRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {
        String uuid = RandomUtils.uuid();
        File local = new File(appConfig.getStorage() + "/" + uuid + ".jpg");
        file.transferTo(local);

        runningService.running(uuid);

        return Map.of("id", uuid);
    }
}
