package org.duckdns.ahamike.rollbook.config.runInitialization;

import java.util.List;
import java.util.Set;

import org.duckdns.ahamike.rollbook.config.logging.ServiceLoggingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RunInitialization implements ApplicationRunner {
    @Value("#{environment['variable.server.name']}")
    private String nameServer;
    @Value("#{environment['variable.db.name']}")
    private String nameDb;
    @Value("#{environment['variable.log.name']}")
    private String nameLog;
    @Value("#{environment['variable.doc.name']}")
    private String nameDoc;
    @Value("#{environment['variable.file.name']}")
    private String nameFile;
    @Value("#{environment['variable.auth.name']}")
    private String nameAuth;
    @Value("#{environment['variable.actuator.name']}")
    private String nameActuator;

    private final ServiceLoggingConfig serviceLoggingConfig;
    @Autowired

    @Override
    public void run(ApplicationArguments args) throws Exception {
        printArgs(args);
        printLogTest();
        printEnvYaml();
        setAndGetLoggingAop();
    }

    private void printArgs(ApplicationArguments args) {
        List<String> nonOptionArgs = args.getNonOptionArgs();
        log.info("Size of nonOptionArgs: {}", nonOptionArgs.size());
        for (int ii = 0; ii < nonOptionArgs.size(); ++ii) {
            log.info("nonOptionArgs[{}]: {}", ii, nonOptionArgs.get(ii));
        }

        Set<String> optionNames = args.getOptionNames();
        log.info("Size of optionNames: {}", optionNames.size());
        for (int ii = 0; ii < optionNames.size(); ++ii) {
            log.info("optionNames[{}]: {}", ii, optionNames.toArray()[ii]);
        }

        String[] sourceArgs = args.getSourceArgs();
        log.info("Length of sourceArgs: {}", sourceArgs.length);
        for (int ii = 0; ii < sourceArgs.length; ++ii) {
            log.info("sourceArgs[{}]: {}", ii, sourceArgs[ii]);
        }
    }

    private void printLogTest() {
        log.trace("Logging Test: TRACE Level");
        log.debug("Logging Test: DEBUG Level");
        log.info("Logging Test: INFO Level");
        log.warn("Logging Test: Warn Level");
        log.error("Logging Test: ERROR Level");
    }

    private void printEnvYaml() {
        log.info("Name of Server: {}", nameServer);
        log.info("Name of Db: {}", nameDb);
        log.info("Name of Log: {}", nameLog);
        log.info("Name of Doc: {}", nameDoc);
        log.info("Name of File: {}", nameFile);
        log.info("Name of Auth: {}", nameAuth);
        log.info("Name of Actuator: {}", nameActuator);
    }

    private void setAndGetLoggingAop() {
        serviceLoggingConfig.enableRequest();
        serviceLoggingConfig.enableResponse();
        serviceLoggingConfig.enableException();
        serviceLoggingConfig.enableDatabase();
        log.info("Logging Request: {}", serviceLoggingConfig.isEnabledRequest());
        log.info("Logging Response: {}", serviceLoggingConfig.isEnabledResponse());
        log.info("Logging Exception: {}", serviceLoggingConfig.isEnabledException());
        log.info("Logging Database: {}", serviceLoggingConfig.isEnabledDatabase());
    }
}
