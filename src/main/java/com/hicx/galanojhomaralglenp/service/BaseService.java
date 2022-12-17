package com.hicx.galanojhomaralglenp.service;

import java.nio.file.FileSystems;

public interface BaseService {
    String PROCESSED_FILE_DIRECTORY = "processed";
    String REJECTED_FILE_DIRECTORY = "rejected";
    String[] VALID_FILE_TYPES = {"txt"};
    String[] SPECIAL_CHARACTERS_TO_COUNT = {"."};

    String PATH_SEPARATOR = FileSystems.getDefault().getSeparator();
}
