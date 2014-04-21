/*
 Copyright 2014 Damien Raude-Morvan
 All rights reserved.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.drazzib.confluence.importer;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * For each visited file, check for {@link #GLOB_HTML_FILENAME} on filename.
 * and accumulate matching {@link java.nio.file.Path} into matches.
 *
 * @author Damien Raude-Morvan
 */
public class ConverterFileVisitor extends SimpleFileVisitor<Path> {

    /**
     * Glob pattern for *.html files.
     */
    public static final String GLOB_HTML_FILENAME = "glob:*.html";
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterFileVisitor.class);
    /**
     * Simple matcher which apply {@link #GLOB_HTML_FILENAME}.
     */
    private final PathMatcher matcher;

    /**
     * Collectors for all matching files.
     */
    private final List<Path> matches;

    ConverterFileVisitor() {
        this.matcher = FileSystems.getDefault().getPathMatcher(GLOB_HTML_FILENAME);
        this.matches = Lists.newArrayList();
    }

    void find(Path file) {
        Path name = file.getFileName();
        if (name != null && matcher.matches(name)) {
            this.matches.add(file);
        }
    }

    List<Path> done() {
        return matches;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
}
