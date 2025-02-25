import { useState, useEffect } from "react";

/**
 * 지정한 일수(days)만큼 오늘부터 날짜 배열을 생성하는 커스텀 훅
 * @param days 생성할 날짜의 수 (기본값: 6)
 * @returns Date 객체 배열
 */
export const useDateList = (days: number = 6): Date[] => {
  const [dateList, setDateList] = useState<Date[]>([]);

  useEffect(() => {
    const today = new Date();
    const dates: Date[] = [];
    for (let i = 0; i < days; i++) {
      const newDate = new Date(today);
      newDate.setDate(today.getDate() + i); // 월 경계도 자동 처리됨
      dates.push(newDate);
    }
    setDateList(dates);
  }, [days]);

  return dateList;
};

/**
 * Date 객체를 "일" 형식의 문자열로 변환 (예: "28일")
 * @param date 변환할 Date 객체
 * @returns 포맷된 문자열
 */
export const formatDate = (date: Date): string => `${date.getDate()}일`;
