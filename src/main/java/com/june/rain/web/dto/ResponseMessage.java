package com.june.rain.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ResponseMessage {
	//상태코드
	private int code = 200;
	//메시지
	private String message;
	//성공여부
	private boolean result = true;
	//데이터
	private Object data;


}
