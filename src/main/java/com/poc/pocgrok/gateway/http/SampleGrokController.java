package com.poc.pocgrok.gateway.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SampleGrokController {

    private final ObjectMapper objectMapper;

    @PostMapping("/samplelog")
    public ResponseEntity<Void> doSomething(@PathVariable String desiredName, @PathVariable("desiredAge") String desiredAge) throws JsonProcessingException {

        /* Create a new grokCompiler instance */
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();

        /* Grok pattern to compile, here httpd logs */
        final Grok grok = grokCompiler.compile("%{COMBINEDAPACHELOG}");

        /* Line of log to match */
        String sample = "112.169.19.192 - - [06/Mar/2013:01:36:30 +0900] \"GET / HTTP/1.1\" 200 44346 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.152 Safari/537.22\"";

        Match gm = grok.match(sample);

        /* Get the map with matches */
        final Map<String, Object> capture = gm.capture();

        log.info("sample {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(capture));
        log.info("sample {}", objectMapper.writeValueAsString(capture));

        return ResponseEntity.ok().build();

    }
}
