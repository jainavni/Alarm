package com.example.avnijain.alarms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Avni Jain on 6/27/2017.
 */

public class ExpenseOpenHelper extends SQLiteOpenHelper {

    public final static String EXPENSE_TABLE_NAME = "Expense";
    public final static String TRASH_TABLE_NAME = "Trash";
    public final static String EXPENSEE_TITLE = "title";
    public final static String EXPENSE_ID = "id";
    public final static String EXPENSE_CATEGORY = "category";
    public final static String TIMESTAMP = "timestamp";
  //  public final static String EXPENSE_DATE = "date";
   // public final static String EXPENSE_TIME = "time";

    public final static String EXPENSE_IMAGESOURCE = "imagesource";
    // public final static String EXPENSE_PRICE = "price";

/*
    private static ExpenseOpenHelper getOpenHelperInstance(Context context){
        if(expenseOpenHelper==null)
            ExpenseOpenHelper(context);
    }*/

    public ExpenseOpenHelper(Context context){ // String name, SQLiteDatabase.CursorFactory factory, int version) {
       // super(context, name, factory, version);
        super(context,"Expenses.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =

                "create table " + EXPENSE_TABLE_NAME + " ( "
                + EXPENSE_ID + " integer primary key autoincrement,"
                + EXPENSEE_TITLE + " text,"
                + EXPENSE_CATEGORY + " text,"
                + TIMESTAMP + " integer, "
              //  + EXPENSE_DATE + " text,"
              //  + EXPENSE_TIME + " text, "
                + EXPENSE_IMAGESOURCE + " text "  +  ");" ;

        db.execSQL(query);

        db.execSQL( "create table " + TRASH_TABLE_NAME + " ( "
                + EXPENSE_ID + " integer primary key autoincrement,"
                + EXPENSEE_TITLE + " text,"
                + EXPENSE_CATEGORY + " text"
            //    + EXPENSE_DATE + " text,"
            //    + EXPENSE_TIME + " text"
                +  ");"  );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Column insert
        //drop column
        //create   etc etc etc
        //if  db version is changed


//         if (oldVersion==1 && newVersion==2){}
//        else if(oldVersion==1 && newVersion==3){}
    }


}
