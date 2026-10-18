/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.common.utils;

import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

    private TimeZone originalTimeZone;

    @BeforeEach
    void setUp() {
        originalTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
    }

    @AfterEach
    void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }

    @Test
    void testParseDaylightSavingDate() {
        // June in New York is EDT (UTC-4), so the wall-clock time must round trip
        // unchanged instead of shifting one hour late
        Date date = DateUtils.parse("2023-06-15 10:30:45");
        Assertions.assertEquals(Instant.parse("2023-06-15T14:30:45Z"), date.toInstant());
        Assertions.assertEquals("2023-06-15 10:30:45", DateUtils.format(date));
    }

    @Test
    void testParseNonDaylightSavingDate() {
        // January in New York is EST (UTC-5); this already worked before
        Date date = DateUtils.parse("2023-01-15 10:30:45");
        Assertions.assertEquals(Instant.parse("2023-01-15T15:30:45Z"), date.toInstant());
        Assertions.assertEquals("2023-01-15 10:30:45", DateUtils.format(date));
    }

    @Test
    void testParseWithExplicitOffset() {
        // a string carrying its own offset never goes through the default zone
        Date date = DateUtils.parse("2023-06-15T10:30:45+05:00");
        Assertions.assertEquals(Instant.parse("2023-06-15T05:30:45Z"), date.toInstant());
    }
}
