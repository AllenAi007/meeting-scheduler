package com.ai.scheduler.factory;

import com.ai.scheduler.exception.InitializationException;
import com.ai.scheduler.model.Talks;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Talk factory
 */
public final class TalksFactory {

    private static final String INPUT_FILE_NAME = "talks.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TalksFactory(){}

    /**
     * By default, will load talk from class path with file name INPUT_FILE_NAME
     *
     * @return
     * @throws InitializationException
     */
    public static Talks createTalks() {
        try (InputStream in = TalksFactory.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME)) {
            return OBJECT_MAPPER.readValue(in, Talks.class);
        } catch (IOException e) {
            throw new InitializationException("Error caused while initialization with classpath input file " + INPUT_FILE_NAME, e);
        }
    }

    /**
     * Create talks from an absolute file path
     *
     * @param absolutePath absolute path
     * @return Talks
     * @throws InitializationException
     */
    public static Talks createTalks(String absolutePath) {
        try (InputStream in = new FileInputStream(new File(absolutePath))) {
            return OBJECT_MAPPER.readValue(in, Talks.class);
        } catch (IOException e) {
            throw new InitializationException("Error caused while initialization with absolute path input file" + INPUT_FILE_NAME, e);
        }
    }
}
