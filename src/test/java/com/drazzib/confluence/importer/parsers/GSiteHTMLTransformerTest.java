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
package com.drazzib.confluence.importer.parsers;

import com.drazzib.confluence.importer.html.GSiteHTMLTransformer;
import com.drazzib.confluence.importer.model.ConfluencePage;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.InputStream;

import static com.google.common.io.Resources.asByteSource;
import static com.google.common.io.Resources.getResource;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by drazzib on 16/04/14.
 */
public class GSiteHTMLTransformerTest {

    @Test
    public void shouldParseGSiteFullPage() throws Exception {
        ByteSource fullRes = asByteSource(getResource("sample_gsite_full.html"));
        ByteSource expectedContentRes = asByteSource(getResource("expect_sample_gsite_content.html"));

        try (InputStream is = fullRes.openBufferedStream()) {
            GSiteHTMLTransformer parser = new GSiteHTMLTransformer();
            ConfluencePage page = parser.parse(is, Charsets.UTF_8);

            assertThat(page.getTitle()).isEqualTo("Home");
            assertThat(page.getContent().outerHtml()).isEqualTo(Jsoup.parseBodyFragment(expectedContentRes.asCharSource(Charsets.UTF_8).read()).body().html());
        }
    }

}
