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
package com.drazzib.confluence.importer.binary;

import com.drazzib.confluence.importer.ConfluenceItemWriter;
import com.drazzib.confluence.importer.model.BinaryItem;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple JSoup parser which will create {@link com.drazzib.confluence.importer.model.ConfluencePage}.
 *
 * @author Damien Raude-Morvan
 */
public class BinaryTransformer {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryTransformer.class);

    private final Path inPath;

    private final Path outPath;

    private final ConfluenceItemWriter<BinaryItem> writer;

    public BinaryTransformer(final Path inPath, final Path outPath) {
        this.inPath = inPath;
        this.outPath = outPath;
        this.writer = new BinaryItemWriter();
    }

    public void transform(Path file) {
        try (InputStream is = Files.newInputStream(file)) {
            BinaryItem page = this.parse(is);

            // 3- Compute new path for Confluence file storage.
            Path newPath = transformPath(inPath, outPath, file);
            page.setNewLocation(newPath);

            // 4- Write result to its new location.
            writer.write(page);
        } catch (IOException e) {
            LOGGER.error("Unable to parse file {}", file, e);
        }
    }

    public BinaryItem parse(final InputStream is) throws IOException {
        BinaryItem item = new BinaryItem();
        item.setData(ByteStreams.toByteArray(is));
        return item;
    }

    /**
     * @param inPath    Input root directory.
     * @param outPath   New output root directory
     * @param gSiteFile Current {@link com.drazzib.confluence.importer.model.ConfluencePage} read from gSiteFile.
     */
    private Path transformPath(final Path inPath, final Path outPath, final Path gSiteFile) {
        LOGGER.trace("Input file {}", gSiteFile);
        Path relative = inPath.relativize(gSiteFile);
        Path parentDir = relative.getParent();
        String fileName = gSiteFile.getFileName().toString();
        Path newPath = Paths.get(outPath.toString(), parentDir.toString(), fileName);
        LOGGER.trace("Output file {}", newPath);
        return newPath;
    }

}
