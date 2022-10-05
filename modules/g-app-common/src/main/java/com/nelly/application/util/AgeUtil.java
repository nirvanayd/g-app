package com.nelly.application.util;

import java.util.Calendar;

public class AgeUtil {

    public static int getYear(String birthDay) {
        return Integer.parseInt(birthDay.substring(0, 4));
    }

    public static int getMonth(String birthDay) {
        return Integer.parseInt(birthDay.substring(4, 6));
    }

    public static int getDate(String birthDay) {
        return Integer.parseInt(birthDay.substring(6, 8));
    }

    public static int getAge(int birthYear, int birthMonth, int birthDay)
    {
        Calendar current = Calendar.getInstance();

        int currentYear  = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);
        // 만 나이 구하기 2022-1995=27 (현재년-태어난년)
        int age = currentYear - birthYear;
        // 만약 생일이 지나지 않았으면 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
            age--;
        // 5월 26일 생은 526
        // 현재날짜 5월 25일은 525
        // 두 수를 비교 했을 때 생일이 더 클 경우 생일이 지나지 않은 것이다.
        return age;
    }
}
