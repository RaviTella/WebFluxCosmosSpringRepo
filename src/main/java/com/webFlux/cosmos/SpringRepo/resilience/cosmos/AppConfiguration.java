package com.webFlux.cosmos.SpringRepo.resilience.cosmos;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.UsernamePasswordCredential;
import com.azure.identity.UsernamePasswordCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.core.mapping.EnableCosmosAuditing;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableReactiveCosmosRepositories(basePackages = "com.webFlux.cosmos.SpringRepo.resilience.model")
@EnableCosmosAuditing

public class AppConfiguration extends AbstractCosmosConfiguration {


    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.database}")
    private String dbName;

    private AzureKeyCredential azureKeyCredential;

    @Value("#{'${azure.cosmos.locations}'.split(',')}")
    private List<String> locations;

    @Value("${azure.cosmos.queryMetricsEnabled}")
    private boolean queryMetricsEnabled;

    @Bean
    public CosmosClientBuilder getCosmosClientBuilder() {
        this.azureKeyCredential = new AzureKeyCredential(key);
        UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
                .clientId("")    //application id of the app "rtcosmos100"
                .username("") //User Principal Name
                .password("")
                .build();

        TokenCredential servicePrincipal = new ClientSecretCredentialBuilder()
                .authorityHost("")
                .tenantId("")
                .clientId("")  //Application ID
                .clientSecret("")
                .build();
        return new CosmosClientBuilder()
                .endpoint(uri)
               .credential(usernamePasswordCredential);
    }


    @Override
    public CosmosConfig cosmosConfig() {
        return CosmosConfig
                .builder()
                .enableQueryMetrics(queryMetricsEnabled)
                .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                .build();
    }

    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.info("Response Diagnostics {}", responseDiagnostics);
        }
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }
}