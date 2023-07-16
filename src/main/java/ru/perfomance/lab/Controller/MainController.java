package ru.perfomance.lab.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.perfomance.lab.Model.RequestDTO;
import ru.perfomance.lab.Model.ResponseDTO;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class MainController {

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String currency;

            if (firstDigit == '8') {
                maxLimit = new BigDecimal("2000.00");
                currency = "US";
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal("1000.00");
                currency = "EU";
            } else {
                maxLimit = new BigDecimal("10000.00");
                currency = "RUB";
            }

            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setRqUID(requestDTO.getRqUID());
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);
            responseDTO.setBalance(generateRandomBalance(maxLimit));
            responseDTO.setMaxLimit(maxLimit);

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private String generateRandomBalance(BigDecimal maxLimit) {
        double randomBalance = ThreadLocalRandom.current().nextDouble(0, maxLimit.doubleValue());
        return String.format("%.2f", randomBalance);
    }
}
