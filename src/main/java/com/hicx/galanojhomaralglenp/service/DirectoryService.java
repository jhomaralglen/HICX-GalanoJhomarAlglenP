package com.hicx.galanojhomaralglenp.service;

import java.nio.file.Path;
import java.util.Set;

public interface DirectoryService extends BaseService {

    boolean checkDirectoryIfExist(Path sourcePath);

    void checkRequiredDirectoryIfExist(String sourcePath);

    void create(Path path);

    Set<String> getFiles(String sourcePath);

    void watchDirectory(String sourcePath);

}
