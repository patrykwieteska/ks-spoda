package pl.spoda.ks.rating.model.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GamePlayerData {

    private BigDecimal rating;
    private String id;

}
