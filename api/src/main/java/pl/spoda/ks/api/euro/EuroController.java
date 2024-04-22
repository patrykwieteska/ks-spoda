package pl.spoda.ks.api.euro;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.spoda.ks.euro.EuroService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/euro")
public class EuroController {

    private final EuroService euroService;
}
