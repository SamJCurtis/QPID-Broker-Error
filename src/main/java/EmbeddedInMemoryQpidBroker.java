/*
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO IBM CORP AND MAY NOT
 * BE REPRODUCED PUBLISHED OR DISCLOSED TO OTHERS WITHOUT ITS AUTHORIZATION.
 *
 *
 * Copyright © 2019. IBM Corporation.
 *
 *
 * All Rights Reserved. IBM is a trademark or registered trademarks of IBM Corporation or
 * its affiliates in the U.S. and other countries. Other names may be trademarks of their
 * respective owners.
 */


import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.SystemConfig;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class EmbeddedInMemoryQpidBroker implements AutoCloseable {
    public static final Logger logger = getLogger(EmbeddedInMemoryQpidBroker.class);

    private static String DEFAULT_INITIAL_CONFIGURATION_LOCATION = "qpid-embedded-inmemory-configuration.json";

    private boolean startupLoggedToSystemOut = true;

    private String initialConfigurationLocation;

    private URL initialConfigurationUrl;

    private SystemLauncher systemLauncher;

    public EmbeddedInMemoryQpidBroker() throws IOException {
        this.systemLauncher = new SystemLauncher();
        initialConfigurationLocation = DEFAULT_INITIAL_CONFIGURATION_LOCATION;
    }

    /**
     * Starts the embedded broker with configuration values returned by createSystemConfig method
     * @throws Exception
     */
    public void start() throws Exception {
        this.systemLauncher.startup(createSystemConfig());
    }

    public void shutdown() {
        this.systemLauncher.shutdown();
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    /**
     * This method configures the broker according to the configuration file specified by DEFAULT_INITIAL_CONFIGURATION_LOCATION
     *
     * @return
     * @throws IllegalConfigurationException
     */
    private Map<String, Object> createSystemConfig() throws IllegalConfigurationException {
        Map<String, Object> attributes = new HashMap<>();
        URL initialConfigUrl = this.initialConfigurationUrl;
        if (initialConfigUrl == null) {
            logger.debug("Will attempt to load config from CLASSPATH {}", this.initialConfigurationLocation);
            initialConfigUrl = EmbeddedInMemoryQpidBroker.class.getClassLoader().getResource(this.initialConfigurationLocation);
        }
        if (initialConfigUrl == null) {
            throw new IllegalConfigurationException("Configuration location '" + this.initialConfigurationLocation + "' not found");
        }
        attributes.put(SystemConfig.TYPE, "Memory");
        attributes.put(SystemConfig.INITIAL_CONFIGURATION_LOCATION, initialConfigUrl.toExternalForm());
        attributes.put(SystemConfig.STARTUP_LOGGED_TO_SYSTEM_OUT, this.startupLoggedToSystemOut);
        return attributes;
    }

    public void setInitialConfigurationLocation(String initialConfigurationLocation) {
        this.initialConfigurationLocation = initialConfigurationLocation;
    }

    public void setStartupLoggedToSystemOut(boolean startupLoggedToSystemOut) {
        this.startupLoggedToSystemOut = startupLoggedToSystemOut;
    }

    public EmbeddedInMemoryQpidBroker withInitialConfigurationLocation(String initialConfigurationLocation) {
        setInitialConfigurationLocation(initialConfigurationLocation);
        return this;
    }

    public EmbeddedInMemoryQpidBroker withStartupLoggedToSystemOut(boolean enabled) {
        setStartupLoggedToSystemOut(enabled);
        return this;
    }

    public void setInitialConfigurationLocation(URL initialConfigurationUrl) {
        this.initialConfigurationUrl = initialConfigurationUrl;
    }


}