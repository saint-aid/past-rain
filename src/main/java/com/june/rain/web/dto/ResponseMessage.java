package com.june.rain.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ResponseMessage {

	private int code = 200; //상태코드

	private String message; //메시지

	private boolean result = true; //성공여부

	private Object data; // 데이터


}
