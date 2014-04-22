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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Damien Raude-Morvan
 */
public class BinaryItemWriter implements ConfluenceItemWriter<BinaryItem> {

    @Override
    public void write(final BinaryItem item) throws IOException {
        Path newLocation = item.getNewLocation();
        createAllDirectories(newLocation);
        Files.write(newLocation, item.getData());
    }

    private void createAllDirectories(final Path newLocation) throws IOException {
        Files.createDirectories(newLocation.getParent());
    }

}
