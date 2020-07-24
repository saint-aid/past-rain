package com.june.rain.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Rain {
    private String searchDay;
    private String rainHm;
    private String rainYn;
    private String rain15m;
    private String rain60m;
    private String rain3h;
    private String rain6h;
    private String rain12h;
    private String rain24h;
    private String temperature;
}
