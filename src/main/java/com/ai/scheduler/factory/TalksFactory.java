package com.ai.scheduler.factory;

import com.ai.scheduler.exception.InitializationException;
import com.ai.scheduler.model.Talks;
import com.ai.scheduler.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Talk factory
 */
@Slf4j
public final class TalksFactory {

    private static final String INPUT_FILE_NAME = "talks.json";

    private TalksFactory() {
    }

    /**
     * By default, will load talk from class path with file name INPUT_FILE_NAME
     *
     * @return
     * @throws InitializationException
     */
    public static Talks createTalks() {
        log.info("Loading talks from classpath {}", INPUT_FILE_NAME);
        try (InputStream in = TalksFactory.class.getClassLoader().getResourceAsStream(INPUT_FILE_NAME)) {
            return Utils.OBJECT_MAPPER.readValue(in, Talks.class);
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
        log.info("Loading talks file from absolute path {}", absolutePath);
        try (InputStream in = new FileInputStream(new File(absolutePath))) {
            return Utils.OBJECT_MAPPER.readValue(in, Talks.class);
        } catch (IOException e) {
            throw new InitializationException("Error caused while initialization with absolute path input file" + INPUT_FILE_NAME, e);
        }
    }
}
