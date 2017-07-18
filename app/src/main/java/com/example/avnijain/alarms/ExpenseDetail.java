package com.example.avnijain.alarms;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.onClick;

public class ExpenseDetail extends AppCompatActivity implements LoadPic.onImageLoaded {

    //Double price;
    //EditText pricetextView;
    TextView newTaskToolbar;
    String title, category;
    String prevDate, prevTime;
    int id;
    Long timeStamp;
    EditText titletextView;
    EditText categorytextView;
    ImageView calenderImage,clockImage;
    TextView dateEditText, timeEditText;
    long date;
    Calendar calendar;
    String mCurrentPhotoPath;
    Boolean datePicked,timePicked;
    private static final int TAKE_PHOTO = 100;
    private static final int CHOOSE_PHOTO = 200;
    private static final int LOAD_IMAGE_RESULTS = 300;


    String imageSource = null;
    ImageView imageView;
    int type;
    Bitmap mBitmap;
    Uri mCapturedImageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
       bar.setBackgroundColor(getResources().getColor(R.color.bar_light));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if(getWindow()!=null)
                getWindow().setStatusBarColor(getResources().getColor(R.color.bar_dark));
        }

     //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

         datePicked =false ;
         timePicked = false;

        titletextView = (EditText) findViewById(R.id.editTitle);
        categorytextView = (EditText) findViewById(R.id.editCategory);
        dateEditText = (TextView) findViewById(R.id.expenseDateEditText);
        timeEditText = (TextView) findViewById(R.id.expenseTimeEditText);
        //pricetextView = (EditText) findViewById(R.id.editPrice);
        calenderImage = (ImageView) findViewById(R.id.calender) ;
        clockImage = (ImageView) findViewById(R.id.clock);
        newTaskToolbar = (TextView) findViewById(R.id.newTaskToolbar);

        Intent i = getIntent();

        id = i.getIntExtra("id", -1);
        title = i.getStringExtra(IntentConstants.ExpenseTitle);
        category = i.getStringExtra(IntentConstants.ExpenseCategory);
        timeStamp = i.getLongExtra(IntentConstants.TimeStamp,0);
     //   prevDate = i.getStringExtra(IntentConstants.ExpenseDate);
     //   prevTime = i.getStringExtra(IntentConstants.ExpenseTime);
        //price = i.getDoubleExtra(IntentConstants.ExpensePrice, 0);


//        long timestampLong = Long.parseLong(time)*1000;
        Date d = new Date(timeStamp);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1 ;
        int day = c.get(Calendar.DATE);
        prevDate = day + "/" + month + "/"  + year ;
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        prevTime = hour + ":" + min;
//        System.out.println(year +"-" + month  + "-"+date);


        if (id != -1) {
            titletextView.setText("" + title);
            if(category!=null)      categorytextView.setText("" + category);
            if(prevDate!=null)   dateEditText.setText("" + prevDate);
            if(prevTime!=null)   timeEditText.setText("" + prevTime);
            datePicked=true;
            timePicked=true;
            if(imageSource!=null) {
                LoadPic loadPics = new LoadPic(this);
                loadPics.execute(imageSource);
                loadPics.delegate = this;
                imageView.setVisibility(View.VISIBLE);

            }
            //  pricetextView.setText("" + price);
            newTaskToolbar.setText("");
        }

        ImageView backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView cameraButton = (ImageView) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        ImageView submitButton = (ImageView) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {
                    if (!datePicked) {
                        Toast.makeText(ExpenseDetail.this, "Set Date", Toast.LENGTH_SHORT).show();
                        showDateDialogBox();
                    }

                    if (datePicked && !timePicked) {
                        Toast.makeText(ExpenseDetail.this, "Set Time", Toast.LENGTH_SHORT).show();
                        showTimeDialogBox();
                    }
                }

                String newTitle = titletextView.getText().toString();
                String newCategory = categorytextView.getText().toString();
                String newDate = dateEditText.getText().toString();
                String newTime = timeEditText.getText().toString();
                // String priceText = pricetextView.getText().toString();

                if (newTitle.trim().isEmpty()) {
                    titletextView.setError("Enter Title");
                    return;
                }
//                double price = 0;
//                if (!priceText.isEmpty()) {
//                    price = Double.parseDouble(priceText);
//                }
                ExpenseOpenHelper e = new ExpenseOpenHelper(ExpenseDetail.this);
                SQLiteDatabase db = e.getWritableDatabase();

                ContentValues cv = new ContentValues();        // key-value pairs
                cv.put(ExpenseOpenHelper.EXPENSEE_TITLE, newTitle);
                cv.put(ExpenseOpenHelper.EXPENSE_CATEGORY, newCategory);
                cv.put(ExpenseOpenHelper.TIMESTAMP, date);
               // cv.put(ExpenseOpenHelper.EXPENSE_DATE, newDate);
               // cv.put(ExpenseOpenHelper.EXPENSE_TIME, newTime);
                cv.put(ExpenseOpenHelper.EXPENSE_IMAGESOURCE, imageSource);

                // cv.put(ExpenseOpenHelper.EXPENSE_PRICE, price);

                if (id == -1)
                    db.insert(ExpenseOpenHelper.EXPENSE_TABLE_NAME, null, cv);

                else
                    db.update(ExpenseOpenHelper.EXPENSE_TABLE_NAME, cv, ExpenseOpenHelper.EXPENSE_ID + "=" + id, null);

                setAlarm(id,newTitle,newCategory);

//
//                AlarmManager am = (AlarmManager) ExpenseDetail.this.getSystemService(Context.ALARM_SERVICE);
//                Intent i = new Intent(ExpenseDetail.this,AlarmReceiver.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(ExpenseDetail.this,1,i,0);
//                //requestCode in above PendingIntent.getBroadcast is used to distinguish different alarms, otherwise it will be overWrite.
//                am.set(AlarmManager.RTC,System.currentTimeMillis()+2000,pendingIntent);

                Intent intent = new Intent();
//                Calendar calendar = Calendar.getInstance();
//                date = calendar.getTimeInMillis();
                setResult(RESULT_OK, intent);
                finish();

            }
        });
        clockImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialogBox();
            }
        });
        calenderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialogBox();
            }
        });
//        timeEditText = (EditText) findViewById(R.id.expenseTimeEditText);
        timeEditText.setInputType(InputType.TYPE_NULL);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showTimeDialogBox();

            }
        });

        // Steps for Date picker
        // Will show Date picker dialog on clicking edit text
//        dateEditText = (EditText) findViewById(R.id.expenseDateEditText);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDateDialogBox();
            }
        });
    }
    @Override
    public void setImage(Drawable d) {
        imageView.setImageDrawable(d);

    }

    private void showTimeDialogBox() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ExpenseDetail.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY,selectedHour);
                calendar.set(Calendar.MINUTE,selectedMinute);
                timePicked = true;
                timeEditText.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void showDateDialogBox() {
        Calendar newCalendar = Calendar.getInstance();
        int month = newCalendar.get(Calendar.MONTH);  // Current month
        int year = newCalendar.get(Calendar.YEAR);   // Current year
        showDatePicker(ExpenseDetail.this, year, month, 1);
    }


    public void showDatePicker(final Context context, int initialYear, int initialMonth, int initialDay) {
        // Creating datePicker dialog object
        // It requires context and listener that is used when a date is selected by the user.
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    //This method is called when the user has finished selecting a date.
                    // Arguments passed are selected year, month and day
                    @Override
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        // To get epoch, You can store this date(in epoch) in database
                         calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        date = calendar.getTime().getTime();
                        datePicked=true;
                        // Setting date selected in the edit text
                        dateEditText.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, initialYear, initialMonth, initialDay);

        //Call show() to simply show the dialog
        datePickerDialog.show();

    }
    private void setAlarm(int id,String title,String category) {

        AlarmManager am = (AlarmManager) ExpenseDetail.this.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ExpenseDetail.this,AlarmReceiver.class);
        i.putExtra("id",id);
        i.putExtra(IntentConstants.ExpenseTitle,title);
        i.putExtra(IntentConstants.ExpenseCategory,category);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ExpenseDetail.this,id,i,0);
        //requestCode in above PendingIntent.getBroadcast is used to distinguish different alarms, otherwise it will be overWrite.
        am.set(AlarmManager.RTC,date,pendingIntent);

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }



    private void selectImage() {
        final CharSequence[] items = {getString(R.string.Take_Photo),
                getString(R.string.Choose_from_Gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.Add_Photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.Take_Photo))) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go.
                        // If you don't do this, you may get a crash in some devices.
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    getString(R.string.problem_saving_photo), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri fileUri = Uri.fromFile(photoFile);
                            mCapturedImageUrl = fileUri;
                            mCurrentPhotoPath = fileUri.getPath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    fileUri);
                            startActivityForResult(takePictureIntent, TAKE_PHOTO);
                        }
                    }
                    dialog.dismiss();
                } else if (items[item].equals(getString(R.string.Choose_from_Gallery))) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, CHOOSE_PHOTO);
                        dialog.dismiss();
                    }
                    else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                            && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {

                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore
                                .Images.Media.EXTERNAL_CONTENT_URI);

                        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the
                        // results when image is picked from the Image Gallery.
                        startActivityForResult(i, LOAD_IMAGE_RESULTS);

                    }
                }
            }
        });
        builder.show();
    }


    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", getResources()
                .getConfiguration().locale).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            if (!storageDir.mkdirs()) {
                Toast.makeText(this, getString(R.string.cannot_create_file), Toast.LENGTH_SHORT)
                        .show();
            }
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PHOTO && data != null && data.getData() != null) {
            Uri _uri = data.getData();
            imageSource = tools.getPath(this, _uri);
            mBitmap = tools.decodeSampledBitmapFromSource(
                    imageSource,
                    (int) tools.getWidth(getApplicationContext()),
                    (int) tools.convertDpToPixel(200, getApplicationContext()));
            //mPreviousKnot.imageSource = imageSource;
            imageView.setVisibility(View.VISIBLE);
            if (mBitmap != null)
                imageView.setImageBitmap(mBitmap);
            else {
                Toast.makeText(this, getString(R.string.problem_setting_image),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == TAKE_PHOTO && resultCode == ActionBarActivity.RESULT_OK) {
            if (data != null) {
                //For Devices where camera does not store image to given path and
                // and only stores in gallery or storage framework.
                //Cut image from gallery and move to our directory
                Uri _uri = data.getData();
                ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
                pDialog.show();

                imageSource = tools.getPath(this, _uri);
                mBitmap = tools.getBitmapFromSource(imageSource);
                File newFile = new File(mCurrentPhotoPath);
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(newFile);

                    // Use the compress method on the BitMap object to write image
                    // to the OutputStream
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    File del = new File(imageSource);
                    del.delete();
                    imageSource = newFile.getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();
            } else {
                imageSource = mCurrentPhotoPath;
             //   mPreviousKnot.imageSource = mCurrentPhotoPath;
                mBitmap = tools.decodeSampledBitmapFromSource(imageSource, (int)
                                tools.getWidth(getApplicationContext()),
                        (int) tools.convertDpToPixel(200, getApplicationContext()));
            }
            imageView.setVisibility(View.VISIBLE);
            if (mBitmap != null)
                imageView.setImageBitmap(mBitmap);
            else {
                Toast.makeText(this, getString(R.string.problem_setting_image),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imageSource = cursor.getString(cursor.getColumnIndex(filePath[0]));
         //   mPreviousKnot.imageSource = imageSource;
            mBitmap = tools.decodeSampledBitmapFromSource(imageSource,
                    (int) tools.getWidth(getApplicationContext()),
                    (int) tools.convertDpToPixel(200, getApplicationContext()));
            imageView.setVisibility(View.VISIBLE);
            if (mBitmap != null)
                imageView.setImageBitmap(mBitmap);
            else {
                Toast.makeText(this, getString(R.string.problem_setting_image),
                        Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.image_capture_fail),
                    Toast.LENGTH_SHORT)
                    .show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}

