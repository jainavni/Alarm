package com.example.avnijain.alarms;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Avni Jain on 6/22/2017.
 */

 public class ExpenseListAdapter extends ArrayAdapter<Expense> {

    ArrayList<Expense> expenseArrayList;
    Context context;
    //MainActivity mainActivity;
   // OnListButtonClickedInterfce listener;

    public ExpenseListAdapter(@NonNull Context context, ArrayList<Expense> expenseArrayList) {
        super(context, 0);  // , expenseArrayList); // we have to pass list here if we will not make getCount func.
        this.context = context;
        this.expenseArrayList = expenseArrayList;
    }

    public int getCount() {
        return expenseArrayList.size();
    }

    static class ExpenseViewHolder {
        // **** ExpenseViewHolder class is inner class to ExpenseListAdapter.Now if func of outer class needs to make obj of inner then no need to create obj of outer..****

        TextView name;
        TextView category;
        TextView date;
        TextView time;
       // TextView price;

        ExpenseViewHolder(TextView name, TextView category,TextView date, TextView time) {
            this.name = name;
            this.category = category;
            this.date = date;
            this.time = time;
          //  this.price = price;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            TextView name =  convertView.findViewById(R.id.expenseNametextView);
           // TextView price = (TextView) convertView.findViewById(R.id.expensePricetextView);
            TextView category =  convertView.findViewById(R.id.editCategory);
            TextView date =  convertView.findViewById(R.id.expenseDatetextView);
            TextView time =  convertView.findViewById(R.id.expenseTimetextView);
            ExpenseViewHolder e = new ExpenseViewHolder(name,category, date, time);
            convertView.setTag(e);
        }

        Expense expense = expenseArrayList.get(position);
        ExpenseViewHolder expenseViewHolder = (ExpenseViewHolder) convertView.getTag();
        expenseViewHolder.name.setText(expense.title);
//        if(expense.category!= null)
//            expenseViewHolder.category.setText(""+expense.category);
        Date d = new Date(expense.timeStamp);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1 ;
        int day = c.get(Calendar.DATE);
        String setDate = day + "/" + month + "/"  + year ;
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        String setTime = hour + ":" + min;


            expenseViewHolder.date.setText("" + setDate);

            expenseViewHolder.time.setText("" + setTime);



       // e.price.setText(String.valueOf(ex.price));

       /* e.listButton.setOnClickListener(new View.OnClickListener() {   // handling listButton
            @Override
            public void onClick(View view) {
                mainActivity.listButtonClicked(pos,view);              // mainActivity is object of MainActivity
            }                                                          // create a method in MainActivity
            //  DRAWBACK : it would be used only by mainActivity
        });*/


       /*e.listButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(listener!=null)
                   listener.listButtonClicked(view,position);

           }
*/

        return convertView;
    }
}

/*interface  OnListButtonClickedInterface(){
          void onListButton(v,pos){}
        }*/

/*



        View v = LayoutInflater.from(context).inflate(R.layout.list_item,null);
        TextView nameTextView = (TextView) v.findViewById(R.id.expenseNametextView);
        TextView CategoryTextView = (TextView) v.findViewById(R.id.expenseCategorytextView);
        TextView PriceTextView = (TextView) v.findViewById(R.id.expensePricetextView);

        Expense e = expenseArrayList.get(position);
        nameTextView.setText(e.title);
        CategoryTextView.setText(e.category);
        PriceTextView.setText(e.price);

        return v;
    }
}
*/
