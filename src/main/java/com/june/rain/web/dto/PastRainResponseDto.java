package com.june.rain.web.dto;

import lombok.*;

@Data
public class PastRainResponseDto {
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

}
