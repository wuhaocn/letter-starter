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
package org.letter.common.config.configcenter;

import org.letter.common.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.letter.common.config.configcenter.AbstractDynamicConfiguration.*;
import static org.letter.common.config.configcenter.DynamicConfiguration.DEFAULT_GROUP;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AbstractDynamicConfiguration} Test
 *
 * @since 2.7.5
 */
public class AbstractDynamicConfigurationTest {

    private AbstractDynamicConfiguration configuration;

    @BeforeEach
    public void init() {
        configuration = new AbstractDynamicConfiguration(null) {
            @Override
            protected String doGetConfig(String key, String group) throws Exception {
                return null;
            }

            @Override
            protected void doClose() throws Exception {

            }

            @Override
            protected boolean doRemoveConfig(String key, String group) throws Exception {
                return false;
            }
        };
    }

    @Test
    public void testConstants() {
        assertEquals("letter.config-center.", PARAM_NAME_PREFIX);
        assertEquals("letter.config-center.workers", DEFAULT_THREAD_POOL_PREFIX);
        assertEquals("letter.config-center.thread-pool.prefix", THREAD_POOL_PREFIX_PARAM_NAME);
        assertEquals("letter.config-center.thread-pool.size", THREAD_POOL_SIZE_PARAM_NAME);
        assertEquals("letter.config-center.thread-pool.keep-alive-time", THREAD_POOL_KEEP_ALIVE_TIME_PARAM_NAME);
        assertEquals(1, DEFAULT_THREAD_POOL_SIZE);
        assertEquals(60 * 1000, DEFAULT_THREAD_POOL_KEEP_ALIVE_TIME);

        // @since 2.7.8
        assertEquals("letter.config-center.group", GROUP_PARAM_NAME);
        assertEquals("letter.config-center.timeout", TIMEOUT_PARAM_NAME);
    }

    @Test
    public void testConstructor() {
        URL url = URL.valueOf("default://")
                .addParameter(THREAD_POOL_PREFIX_PARAM_NAME, "test")
                .addParameter(THREAD_POOL_SIZE_PARAM_NAME, 10)
                .addParameter(THREAD_POOL_KEEP_ALIVE_TIME_PARAM_NAME, 100);

        AbstractDynamicConfiguration configuration = new AbstractDynamicConfiguration(url) {

            @Override
            protected String doGetConfig(String key, String group) throws Exception {
                return null;
            }

            @Override
            protected void doClose() throws Exception {

            }

            @Override
            protected boolean doRemoveConfig(String key, String group) throws Exception {
                return false;
            }
        };

        ThreadPoolExecutor threadPoolExecutor = configuration.getWorkersThreadPool();
        ThreadFactory threadFactory = threadPoolExecutor.getThreadFactory();

        Thread thread = threadFactory.newThread(() -> {
        });

        assertEquals(10, threadPoolExecutor.getCorePoolSize());
        assertEquals(10, threadPoolExecutor.getMaximumPoolSize());
        assertEquals(100, threadPoolExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS));
        assertEquals("test-thread-1", thread.getName());
    }

    @Test
    public void testPublishConfig() {
        assertFalse(configuration.publishConfig(null, null));
        assertFalse(configuration.publishConfig(null, null, null));
    }

    @Test
    public void testGetConfigKeys() {
        assertTrue(configuration.getConfigKeys(null).isEmpty());
    }

    @Test
    public void testGetConfig() {
        assertNull(configuration.getConfig(null, null));
        assertNull(configuration.getConfig(null, null, 200));
    }

    @Test
    public void testGetInternalProperty() {
        assertNull(configuration.getInternalProperty(null));
    }

    @Test
    public void testGetProperties() {
        assertNull(configuration.getProperties(null, null));
        assertNull(configuration.getProperties(null, null, 100L));
    }

    @Test
    public void testAddListener() {
        configuration.addListener(null, null);
        configuration.addListener(null, null, null);
    }

    @Test
    public void testRemoveListener() {
        configuration.removeListener(null, null);
        configuration.removeListener(null, null, null);
    }

    @Test
    public void testClose() throws Exception {
        configuration.close();
    }

    /**
     * Test {@link AbstractDynamicConfiguration#getGroup()} and
     * {@link AbstractDynamicConfiguration#getDefaultGroup()} methods
     *
     * @since 2.7.8
     */
    @Test
    public void testGetGroupAndGetDefaultGroup() {
        assertEquals(configuration.getGroup(), configuration.getDefaultGroup());
        assertEquals(DEFAULT_GROUP, configuration.getDefaultGroup());
    }

    /**
     * Test {@link AbstractDynamicConfiguration#getTimeout()} and
     * {@link AbstractDynamicConfiguration#getDefaultTimeout()} methods
     *
     * @since 2.7.8
     */
    @Test
    public void testGetTimeoutAndGetDefaultTimeout() {
        assertEquals(configuration.getTimeout(), configuration.getDefaultTimeout());
        assertEquals(-1L, configuration.getDefaultTimeout());
    }

    /**
     * Test {@link AbstractDynamicConfiguration#removeConfig(String, String)} and
     * {@link AbstractDynamicConfiguration#doRemoveConfig(String, String)} methods
     *
     * @since 2.7.8
     */
    @Test
    public void testRemoveConfigAndDoRemoveConfig() throws Exception {
        String key = null;
        String group = null;
        assertEquals(configuration.removeConfig(key, group), configuration.doRemoveConfig(key, group));
        assertFalse(configuration.removeConfig(key, group));
    }
}
