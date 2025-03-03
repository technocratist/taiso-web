package com.taiso.bike_api.util;

import java.util.Random;

public class RandomNickNameGenerator {
    
    private static final String[] FIRST_WORDS = {
        "행복한", "즐거운", "신나는", "열정적인", "귀여운", 
        "용감한", "똑똑한", "친절한", "날쌘", "현명한",
        "활기찬", "상냥한"
    };
    
    private static final String[] SECOND_WORDS = {
        "라이더", "바이커", "자전거", 
        "여행자", "모험가", "탐험가",  "챔피언",
        "주행자", "질주자"
    };
    
    private static final Random random = new Random();
    
    /**
     * 랜덤 닉네임을 생성합니다.
     * @return 생성된 랜덤 닉네임
     */
    public static String generate() {
        String firstWord = FIRST_WORDS[random.nextInt(FIRST_WORDS.length)];
        String secondWord = SECOND_WORDS[random.nextInt(SECOND_WORDS.length)];
        int number = random.nextInt(99) + 1; // 1~99 사이의 숫자
        
        return firstWord + " " + secondWord + " " + number;
    }
}
