package com.hicx.galanojhomaralglenp.service;

import java.io.IOException;
import java.util.Set;

public interface FileService extends BaseService {

    void processFiles(String sourcePath, Set<String> fileList);
    boolean checkIfValid(String filePath);
    void getStatistics(String filePath, String content);
    void moveFile(String sourcePath, String targetPath) throws IOException;
}
