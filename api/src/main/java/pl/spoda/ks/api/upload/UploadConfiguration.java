package pl.spoda.ks.api.upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "match-import.enabled", havingValue = "true")
public class UploadConfiguration  {

    private final UploadService uploadService;

    @Bean(initMethod="init")
    public void init() {

        uploadService.uploadMatches();

    }
}
