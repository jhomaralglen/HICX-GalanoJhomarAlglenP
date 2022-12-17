package com.hicx.galanojhomaralglenp.service.impl;

import com.hicx.galanojhomaralglenp.service.FileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private static Map<String, Object> fileStatistics = new LinkedHashMap<>();

    private static Set<String> validFileTypes = null;

    // accept list of special character and file types
    public FileServiceImpl(){
        setValidFileTypes();
        setFileStatistics();
    }

    private void setFileStatistics(){
        fileStatistics.put("MostUsedWord", null);
        fileStatistics.put("WordCount", 0);
        fileStatistics.put("WhiteSpaceCount", 0);
    }

    private void setValidFileTypes(){
        validFileTypes = new HashSet<>(Arrays.asList(VALID_FILE_TYPES));
    }

    @Override
    public void processFiles(String sourcePath, Set<String> fileList) {

        Long epochSecond = null;

        for (String fileName: fileList){
            String filePath = sourcePath + PATH_SEPARATOR + fileName;
            String targetFilePath = null;
            epochSecond = Instant.now().getEpochSecond();
            String fileNameWithTimeStamp =FilenameUtils.getBaseName(fileName) + "_"
                    + epochSecond + "." + FilenameUtils.getExtension(fileName);

            if(checkIfValid(filePath)){
                String content = getContent(filePath);
                getStatistics(filePath, content);

                System.out.println("+++++++++++START++++++++++++");
                System.out.println("Statistics of file: " + fileName);
                System.out.println(fileStatistics);
                System.out.println("++++++++++++END+++++++++++++");

                targetFilePath = sourcePath + PATH_SEPARATOR + PROCESSED_FILE_DIRECTORY
                        + PATH_SEPARATOR + fileNameWithTimeStamp;
            } else {
                targetFilePath = sourcePath + PATH_SEPARATOR + REJECTED_FILE_DIRECTORY
                        + PATH_SEPARATOR + fileNameWithTimeStamp;
            }

            moveFile(filePath, targetFilePath);
        }

    }

    @Override
    public boolean checkIfValid(String filePath) {
        logger.info("Checking the validity of file: {}", filePath);

        return validFileTypes.stream()
                .anyMatch(validFileType ->
                        validFileType.equals(FilenameUtils.getExtension(filePath))
                );
    }

    @Override
    public void getStatistics(String filePath, String content) {
        logger.info("Getting statistic for file: {}", filePath);

        if(content != null && !content.isEmpty()){
            fileStatistics.put("MostUsedWord", getMostUsedWords(content));
            fileStatistics.put("WordCount", countWord(content));
            fileStatistics.put("WhiteSpaceCount", whiteSpaceCount(content));

            for(String specialCharacter : SPECIAL_CHARACTERS_TO_COUNT) {
                String key = "'" +  specialCharacter + "'Count";
                if(specialCharacter != null && !specialCharacter.isEmpty()) {
                    fileStatistics.put(key, countStringOccurrence(content, specialCharacter));
                }
            }
        }
    }

    @Override
    public void moveFile(String sourcePath, String targetPath) {
        try {
            logger.info("Moving the file to: {}", targetPath);
            Files.move(Paths.get(sourcePath), Paths.get(targetPath));
        } catch (IOException e) {
            logger.error("Failed to move the file: {}", targetPath);
            logger.error(e.getMessage());
        }

    }


    private String getContent(String filePath) {
        String content = null;
        logger.info("Getting the content of file: {}", filePath);
        try {
            File file = new File(filePath);
            content = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e){
            logger.error("Failed get the content of file: {}", filePath);
            logger.error(e.getMessage());
        }
        return content;
    }

    private int countWord(String content){
        logger.info("Counting the words");

        return getWords(content).length;
    }

    private List<String> getMostUsedWords(String content){

        List<String> mostUsedWords = new ArrayList<>();

        // Remove all special characters except for hypens (-) and apostrophes(') and the lower case
        content = content.replaceAll("[^a-zA-Z0-9\\s-']", "").toLowerCase();

        String[] wordArray = getWords(content);
        Arrays.sort(wordArray);
        Set<String> uniqueWordSet = new LinkedHashSet<>(Arrays.asList(wordArray));
        Map<String, Integer> countPerWord = new LinkedHashMap();
        Map<String, Integer> reverseSortedCountPerWord = new LinkedHashMap<>();

        for(String uniqueWord: uniqueWordSet){
            // TODO: Check why the library doesn't work
            // int counter = countStringOccurrence(content, uniqueWord);

            int counter = 0;
            for(String word : wordArray){
                if(uniqueWord.equals(word)){
                    counter++;
                }
            }
            countPerWord.put(uniqueWord, counter);
        }


        // Sorting the Map by value from highest to lowest
        countPerWord.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(x -> reverseSortedCountPerWord.put(x.getKey(), x.getValue()));


        if(!reverseSortedCountPerWord.isEmpty() && reverseSortedCountPerWord != null){
            Map.Entry<String, Integer> firstEntry = reverseSortedCountPerWord.entrySet().iterator().next();

            if(firstEntry.getValue() != null) {
                int largestWordCount = firstEntry.getValue();

                for (Map.Entry<String, Integer> entry : reverseSortedCountPerWord.entrySet()) {

                    if(entry.getValue() == largestWordCount){
                        mostUsedWords.add(entry.getKey());
                    } else {
                        // This will end the loop if the value is not the largest count anymore.
                        // This will work because the map values is already sorted in decending order.
                        break;
                    }
                }
            }
        }

        return mostUsedWords;
    }

    private String[] getWords(String content){
        logger.info("Getting the words");

        return content.split("\\s+");
    }

    private int whiteSpaceCount(String content){
        logger.info("Counting the white spaces");
        char[] charContentArray = content.toCharArray();
        int whiteSpaceCtr = 0;

        for (char letter : charContentArray){
            if(Character.isWhitespace(letter)){
                whiteSpaceCtr++;
            }
        }
        return whiteSpaceCtr;
    }

    // This uses the StringUtil class of Apache Commons Lang 3 library
    private int countStringOccurrence(String content, String stringToCount){

        return StringUtils.countMatches(content, stringToCount);
    }
}
