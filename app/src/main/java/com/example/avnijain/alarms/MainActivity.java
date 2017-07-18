package com.example.avnijain.alarms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import android.app.DatePickerDialog;

import android.widget.DatePicker;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements RecyclerAdapter.NotesClickListener{

    public final static int SORT_BY_TITLE_ASC = 1;
    public final static int SORT_BY_TITLE_DESC = 1;
    public final static int SORT_BY_DATE_ASC = 1;
    public final static int SORT_BY_DATE_DESC = 1;
    // ListView listView;
    RecyclerView recyclerView;
  //  ArrayList<String> expenseList;
    ArrayList<Expense> expenseList;
 //   ArrayAdapter<String> listAdapter;
  //  ExpenseListAdapter expenseListAdapter;
    RecyclerAdapter recyclerAdapter;

     int n=20;
    String arr[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main) ;
//       toolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar);

/*
        SharedPreferences s = getSharedPreferences("A",MODE_PRIVATE);
        String st = s.getString("Exp",null);
        arr = st.split(";");
*/
      //  listView = (ListView) findViewById(R.id.exp);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        expenseList = new ArrayList<>();

        /*for (int i = 0; i < n; i++) {
           // expenseList.add("Exp" + (i + 1));
            Expense e = new Expense();
            e.title = "Exp" + (i+1);
            e.category = "Food";
            e.price =  100+ 10*i;
            expenseList.add(e);
        }*/

        // listAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.expenseNametextView, expenseList);
        // listView.setAdapter(listAdapter);

       // expenseListAdapter = new ExpenseListAdapter(this, expenseList);
        recyclerAdapter = new RecyclerAdapter(this,expenseList,this);

        recyclerView.setAdapter(recyclerAdapter);

         recyclerView.setLayoutManager(new LinearLayoutManager(this));
      //  recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(expenseList,from,to);
                recyclerAdapter.notifyItemMoved(from,to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                deleteExpenseList(expenseList.get(position).id);
                expenseList.remove(position);
                Snackbar.make(recyclerView, "Deleted", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                recyclerAdapter.notifyItemRemoved(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        updateExpenseList(-1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, ExpenseDetail.class);
                startActivityForResult(intent,1);
                updateExpenseList(-1);
            }
        });

    }
public  void onItemClick(View view,int position){

                 Intent i = new Intent(MainActivity.this, ExpenseDetail.class);
                i.putExtra("id", expenseList.get(position).id);
                i.putExtra(IntentConstants.ExpenseTitle, expenseList.get(position).title);
                i.putExtra(IntentConstants.ExpenseCategory, expenseList.get(position).category);
                i.putExtra(IntentConstants.TimeStamp,expenseList.get(position).timeStamp);
            //    i.putExtra(IntentConstants.ExpenseDate, expenseList.get(position).date);
             //   i.putExtra(IntentConstants.ExpenseTime, expenseList.get(position).time);

                  startActivityForResult(i, 1);

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int pos = expenseList.size();
                recyclerAdapter.notifyItemInserted(pos);
                updateExpenseList(-1);
                recyclerAdapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED)
                return;
        }
    }



    private void updateExpenseList(int typeOfSort) {

//    //    long timestampLong = Long.parseLong(time)*1000;
//        Date d = new Date(timestampLong);
//        Calendar c = Calendar.getInstance();
//        c.setTime(d);
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH) + 1 ;
//        int date = c.get(Calendar.DATE);
//        System.out.println(year +"-" + month  + "-"+date);

      // ExpenseOpenHelper expenseOpenHelper = ExpenseOpenHelper.getOpenHelperInstance (this);
        ExpenseOpenHelper expenseOpenHelper = new ExpenseOpenHelper(this);
        expenseList.clear();
        SQLiteDatabase db = expenseOpenHelper.getReadableDatabase();
        Cursor cursor;
        if(typeOfSort==SORT_BY_TITLE_ASC)
         cursor = db.query(ExpenseOpenHelper.EXPENSE_TABLE_NAME,null,null,null,null,null,ExpenseOpenHelper.EXPENSEE_TITLE + " ASC ");
        else if(typeOfSort==SORT_BY_DATE_DESC)
            cursor = db.query(ExpenseOpenHelper.EXPENSE_TABLE_NAME,null,null,null,null,null,ExpenseOpenHelper.TIMESTAMP + " DESC ");
        else
            cursor = db.query(ExpenseOpenHelper.EXPENSE_TABLE_NAME,null,null,null,null,null,null);


        //  DETAILS CAN BE PASSED TO CURSOR LIKE COLUMNS ...NULL REPRESENTS ALL COLUMNS

//        String columns[] = {ExpenseOpenHelper.EXPENSEE_TITLE};
//        String values[] = {"Food"};
//        Cursor cursor2 = d.query(ExpenseOpenHelper.EXPENSE_TABLE_NAME,columns,ExpenseOpenHelper.EXPENSE_CATEGORY +" = ?  AND " + ExpenseOpenHelper.EXPENSE_PRICE + " >=1000",values,null,null,null);

        while(cursor.moveToNext()){
            int id  = cursor.getInt(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_ID));
            String title = cursor.getString(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSEE_TITLE));
           // Double price = cursor.getDouble(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_PRICE));
            String category = cursor.getString(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_CATEGORY));
            Long timeStamp = cursor.getLong(cursor.getColumnIndex(ExpenseOpenHelper.TIMESTAMP));
          //   String date  = cursor.getString(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_DATE));
          //  String time = cursor.getString(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_TIME));
            String imageSource = cursor.getString(cursor.getColumnIndex(ExpenseOpenHelper.EXPENSE_IMAGESOURCE));
            Expense expense = new Expense(id,title,category,timeStamp,imageSource);
            expenseList.add(expense);
        }
        recyclerAdapter.notifyDataSetChanged();
    }


    private void deleteExpenseList(int id){
        ExpenseOpenHelper expenseOpenHelper = new ExpenseOpenHelper(this);
        SQLiteDatabase db = expenseOpenHelper.getReadableDatabase();

        db.delete(ExpenseOpenHelper.EXPENSE_TABLE_NAME,ExpenseOpenHelper.EXPENSE_ID + " = " + id ,null);
        recyclerAdapter.notifyDataSetChanged();
    }


    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem i) {
        int id = i.getItemId();


        if(id==R.id.menuSortTitle)
        {
            updateExpenseList(SORT_BY_TITLE_ASC);
        }
        else if(id==R.id.menuSortReminder)
        {
            updateExpenseList(SORT_BY_DATE_DESC);
        }
        if(id==R.id.Feedback) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"avni1699@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this,"There are no email clients installed." ,
                        Toast.LENGTH_SHORT).show();
            }
//            Intent i1 = new Intent();
//            i1.setAction(Intent.ACTION_SENDTO);
//            Uri u = Uri.parse("mailto:avni@gmail.com");
//            i1.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
//            i1.setData(u);
//            startActivity(i1);
        }
        return  true;
    }
}
  //if (id == R.id.add) {
//
//            Intent intent = new Intent(this, ExpenseDetail.class);
//            startActivityForResult(intent,1);
//            updateExpenseList();
//            // expenseList.add("Expense" + n);
//            //listAdapter.notifyDataSetChanged();
//           // n++;
//        } else if(id==R.id.remove) {
//      }
//        if(id==R.id.AboutUs){
//            Intent i1 = new Intent();
//            i1.setAction(Intent.ACTION_VIEW);
//            Uri u = Uri.parse("https:www.codingninjas.in");
//            i1.setData(u);
//            startActivity(i1);
//        }

       //        else if(id==R.id.ContactUs){
//            Intent i1 = new Intent();
//            i1.setAction(Intent.ACTION_DIAL);
//            Uri u = Uri.parse("tel:7042206");
//            i1.setData(u);
//            startActivity(i1);
//        }
//


//  listView.setAdapter((expenseListAdapter));

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                //Toast.makeText(MainActivity.this, expenseList.get(position) + "clicked", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(MainActivity.this, ExpenseDetail.class);
//                i.putExtra("id", expenseList.get(position).id);
//                i.putExtra(IntentConstants.ExpenseTitle, expenseList.get(position).title);
//                i.putExtra(IntentConstants.ExpenseCategory, expenseList.get(position).category);
//                i.putExtra(IntentConstants.ExpenseDate, expenseList.get(position).date);
//                i.putExtra(IntentConstants.ExpenseTime, expenseList.get(position).time);
//
//                // i.putExtra(IntentConstants.ExpensePrice, expenseList.get(position).price);
//                startActivityForResult(i, 1);
//                // updateExpenseList();
//            }
//        });

//        mAdapter = new RecyclerAdapter(this, mNotes, new RecyclerAdapter.NotesClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Note note = mNotes.get(position);
//                Snackbar.make(mRecyclerView,note.getTitle(),Snackbar.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onRemoveClicked(int position) {
//                mNotes.remove(position);
//                mAdapter.notifyItemRemoved(position);
//            }
//        });


/*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int id = expenseList.get(position).id;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete");
                builder.setCancelable(false);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  expenseList.remove(expenseList.size()-1);
                        // listAdapter.notifyDataSetChanged();
                        //  n--;
                        deleteExpenseList(id);
                        updateExpenseList();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return false;
            }
        });
*/
/*        for(int i=0;i<n;i++) {
            st = st + expenseList.get(i) + ";";
            SharedPreferences.Editor e = s.edit();
            e.putString("Exp", st);
            e.commit();
        }
*/
