package com.june.rain.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Rain {
    //==조회날짜==//
    private String searchDay;
    //==시 분==//
    private String rainHm;
    //==강수 유무==//
    private String rainYn;
    //==측정 15분==//
    private String rain15m;
    //==측정 60분==//
    private String rain60m;
    //==측정 3시간==//
    private String rain3h;
    //==측정 6시간==//
    private String rain6h;
    //==측정 12시간==//
    private String rain12h;
    //==강수일==//
    private String rain24h;
    //==기온==//
    private String temperature;
    //==풍향1==//
    //==풍향1-1==//
    //==풍속==//
    //==풍향10==//
    //==풍속10-1==//
    //==습도==//
    //==해면기압==//

    @Builder
    public Rain(String searchDay, String rainHm, String rainYn,
                               String rain15m, String rain60m, String rain3h,
                               String rain6h, String rain12h, String rain24h,
                               String temperature) {
        this.searchDay = searchDay;
        this.rainHm = rainHm;
        this.rainYn = rainYn;
        this.rain15m = rain15m;
        this.rain60m = rain60m;
        this.rain3h = rain3h;
        this.rain6h = rain6h;
        this.rain12h = rain12h;
        this.rain24h = rain24h;
        this.temperature = temperature;
    }
}
