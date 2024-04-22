package pl.spoda.ks.euro;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EuroService {

    private final EuroClient euroClient;

}
