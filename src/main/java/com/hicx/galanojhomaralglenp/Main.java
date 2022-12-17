package com.hicx.galanojhomaralglenp;

import com.hicx.galanojhomaralglenp.service.DirectoryService;
import com.hicx.galanojhomaralglenp.service.FileService;
import com.hicx.galanojhomaralglenp.service.impl.DirectoryServiceImpl;
import com.hicx.galanojhomaralglenp.service.impl.FileServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Set;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("App started...");

        if (args != null && args.length>0 && args[0] != null && !args[0].isEmpty()) {
            String sourcePath = args[0];
            DirectoryService directoryService = new DirectoryServiceImpl();
            if(directoryService.checkDirectoryIfExist(Paths.get(sourcePath))) {
                directoryService.checkRequiredDirectoryIfExist(sourcePath);

                Set<String> existingFiles = directoryService.getFiles(sourcePath);

                FileService fileService = new FileServiceImpl();
                fileService.processFiles(sourcePath, existingFiles);


                // Poll or watch if there is new file
                directoryService.watchDirectory(sourcePath);
            } else {
                logger.error("Please enter existing or valid path. Then re-run the app again.");
            }

        } else {
            logger.error("Please enter existing or valid path. Then re-run the app again.");
        }

    }
}