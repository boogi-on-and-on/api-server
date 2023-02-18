package boogi.apiserver.domain.report.api;

import boogi.apiserver.domain.report.application.ReportService;
import boogi.apiserver.domain.report.dto.request.CreateReportRequest;
import boogi.apiserver.global.argument_resolver.session.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reports")
public class ReportApiController {

    private final ReportService reportService;

    @PostMapping("/")
    public void createReport(@Validated @RequestBody CreateReportRequest request, @Session Long userId) {
        reportService.createReport(request, userId);
    }
}
