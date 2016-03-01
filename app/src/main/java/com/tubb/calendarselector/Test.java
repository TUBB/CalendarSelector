package com.tubb.calendarselector;

import com.tubb.calendarselector.library.DateUtils;
import com.tubb.calendarselector.library.SSMonth;

/**
 * Created by tubingbing on 16/1/22.
 */
public class Test {
    public static void main(String[] args){
//        int day = DateUtils.getDayOfWeekInMonth(2016, 2);
//        System.out.print(DateUtils.mapDayOfWeekInMonth(day, SSMonth.SATURDAY_OF_WEEK));
        StringBuffer a = new StringBuffer("a");
        StringBuffer b = new StringBuffer("b");
        c(a, b);
        System.out.println(a);
        System.out.println(b);

        Test test = new Test();
        User user1 = new User(1);
        User user2 = new User(2);
        test.u(user1, user2);
        System.out.println(user1.id+"-"+user2.id);
    }
    private void u(User user1, User user2){
        user1.id = 100;
        user2 = new User(7);
        System.out.println(user2.id);
    }

    private static void c(StringBuffer a, StringBuffer b){

        a = new StringBuffer("1000");
        b = new StringBuffer("100");

//        a = b;
//        a = a.append(b);
//        b = a.append(b);
//        b.append(a);
    }


    public static class User{
        public int id;
        public User(int id){
            this.id = id;
        }
    }
}
