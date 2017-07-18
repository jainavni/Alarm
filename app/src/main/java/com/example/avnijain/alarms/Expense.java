package com.example.avnijain.alarms;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Avni Jain on 6/22/2017.
 */

public class Expense implements Serializable{
    // String imageSource;

   // String date;

   // String time;
    int id;
    String title;
    String category;
    Long timeStamp;
    String imageSource;
    // Double price;

    public Expense( int id,String title, String category, Long timeStamp ,String imageSource ) {
        this.id = id;
        this.title = title;
        this.imageSource = imageSource;
        this.timeStamp = timeStamp;
       // this.date = date;
      //  this.time = time;
        this.category = category;
      //  this.price = price;

    }


}
