package org.bedrock.log.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.log.feign.ILogSaveFeign;
import org.bedrock.common.log.model.LogOperation;
import org.bedrock.log.service.ILogOperationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Hidden
@RestController
@RequiredArgsConstructor
public class LogSaveClient implements ILogSaveFeign {

    private final ILogOperationService logOperationService;

    @Override
    public void save(@RequestBody LogOperation logOperation) {
        logOperationService.save(logOperation);
    }
}
