package com.hicx.galanojhomaralglenp.service.impl;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import com.hicx.galanojhomaralglenp.service.DirectoryService;
import com.hicx.galanojhomaralglenp.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DirectoryServiceImpl implements DirectoryService {
    private final Logger logger = LoggerFactory.getLogger(DirectoryServiceImpl.class);

    @Override
    public boolean checkDirectoryIfExist(Path path) {
        return Files.exists(path);
    }

    @Override
    public void checkRequiredDirectoryIfExist(String sourcePath) {
        logger.info("Check if the required directories exists");

        Path processedPath = Paths.get(sourcePath + PATH_SEPARATOR + PROCESSED_FILE_DIRECTORY);
        Path rejectedPath = Paths.get(sourcePath + PATH_SEPARATOR + REJECTED_FILE_DIRECTORY);

        // Create "processed" directory if not exist
        if(!checkDirectoryIfExist(processedPath)) {
            create(processedPath);
        }

        // Create "rejected" directory if not exist
        if(!checkDirectoryIfExist(rejectedPath)) {
            create(rejectedPath);
        }
    }



    @Override
    public void create(Path path) {
        try {
            logger.info("Creating directory: {}", path);
            Files.createDirectories(path);
            logger.info("Success to create directory: {}", path);
        } catch (IOException e) {
            logger.info("Failed to create directory: {}", path);
            logger.error(e.getMessage());
        }
    }

    @Override
    public Set<String> getFiles(String sourcePath) {
        logger.info("Fetch files from directory: {}", sourcePath);

        return Stream.of(Objects.requireNonNull(new File(sourcePath).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }


    @Override
    public void watchDirectory(String sourcePath){
        try {
            // Creates a instance of WatchService.
            WatchService watcher = FileSystems.getDefault().newWatchService();

            // Registers the logDir below with a watch service.
            Path pathToWath = Paths.get(sourcePath);
            pathToWath.register(watcher, ENTRY_CREATE);

            DirectoryService directoryService = new DirectoryServiceImpl();
            FileService fileService =  new FileServiceImpl();

            // Monitor the logDir at listen for change notification.
            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (ENTRY_CREATE.equals(kind)) {
                        logger.info("There is a new file");

                        // Check if sub-directory "processed" and "rejected" still exist
                        directoryService.checkRequiredDirectoryIfExist(sourcePath);
                        Set<String> existingFiles = directoryService.getFiles(sourcePath);
                        fileService.processFiles(sourcePath, existingFiles);
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

}
