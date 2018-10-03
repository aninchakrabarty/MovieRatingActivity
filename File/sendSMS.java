package com.example.movierating;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class sendSMS extends Activity {
	private String firstName;
	private String lastName;
	private EditText smsText;
	private EditText smsNo;
	private int id;
	private String number;
	private String moviename;
	private String genre;
	private String year;
	private String duration;
	private String review;
	private String starcast;
	private String director;
	private double rating;
	private List<String> mReviewData = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      Log.i("AStatus","1");
	      setContentView(R.layout.sendsms);
	      smsText = (EditText)findViewById(R.id.seditText1);
	      smsNo = (EditText)findViewById(R.id.seditText2);
	      Intent i=getIntent();
		Bundle bundle=i.getExtras();
		String name=bundle.getString("data1");	
		id=bundle.getInt("id");
		try{ ContentResolver cr = getContentResolver();
	   Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
	           "DISPLAY_NAME = '" + name + "'", null, null);
	       if (cursor.moveToFirst()) {	        	
	            String contactId =
	                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
		String contactName =
		                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); 
		int index = contactName.indexOf(" ");
		firstName = contactName.substring(0, index);
		lastName = contactName.substring(index+1, contactName.length());
		smsNo.setText(firstName+", "+lastName);
		Cursor phones = cr.query(Phone.CONTENT_URI, null,
		Phone.CONTACT_ID + " = " + contactId, null, null);
		while (phones.moveToNext()) {
		number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
	}
	phones.close();	          
	}
	cursor.close();

	/* Fetch data for the movie with particular id and populate it in 
	    	   a List variable */
	String columns[] = new String[] { "name" , "genre" , "year" , "duration" , 
	    	       "rating" , "review" , "starcast" , "director" };
	    	    	Uri movie = Uri.parse(
	    	        "content://com.movierating.provider.Movie/reviews/"+(id+1));
	    	    	//ContentResolver cr1 = getContentResolver();


	    	    	
	Cursor c = cr.query(movie, columns, // Which columns to return
	null, // WHERE clause; which rows to return(all rows)
		null, // WHERE clause selection arguments (none)
		    	null // Order-by clause (ascending by name)
	    );	
	   if (c.moveToFirst()) {
	    do {
	    	// Get the field values
	    	mReviewData.add(c.getString(c
	    	.getColumnIndex("name"))+";"+c.getString(c
		    	.getColumnIndex("genre"))+";"+c.getString(c
		    	.getColumnIndex("year"))+";"+c.getString(c
		    	.getColumnIndex("duration"))+";"+c.getDouble(c
		    	.getColumnIndex("rating"))+";"+c.getString(c
		    	.getColumnIndex("review"))+";"+c.getString(c
		    	.getColumnIndex("starcast"))+";"+c.getString(c
		    	.getColumnIndex("director")));
		    } while (c.moveToNext());
	    	}
	    	//Closes the cursor object
	    	if (c != null && !c.isClosed()) {
	    	            c.close();}

	/* Separate the various fields from the fetched record by calling a 
		                user-defined function, breakString(). Populate the widgets in the 
		                Display Review activity with the values of the various fields. */

		    breakString(mReviewData.toString());
		            smsText.setText("Hi, I had just reviewed "+moviename+" .My opinion about this movie is: "+review+". This movie is directed by "+director+", starring "+starcast +".");	  
	}catch(Exception e){Log.e("error", e.toString());}	
		}
		/* The breakString method – extracts the values of the various fields from a 
		   Record sent to it as a String parameter */

		public void breakString(String str){
	moviename=str.substring(1,str.indexOf(";"));
	str= str.substring(moviename.length()+2, str.length());
	genre = str.substring(0,str.indexOf(";"));
	str= str.substring(genre.length()+1, str.length());
	year = str.substring(0,str.indexOf(";"));
	str= str.substring(year.length()+1, str.length());
	duration = str.substring(0,str.indexOf(";"));
	str= str.substring(duration.length()+1, str.length());	
	String str1 = str.substring(0,str.indexOf(";"));	
	rating=Double.valueOf(str1);
	str= str.substring(str1.length()+1, str.length());
	review = str.substring(0,str.indexOf(";"));
	str= str.substring(review.length()+1, str.length());
	starcast = str.substring(0,str.indexOf(";"));
	str= str.substring(starcast.length()+1, str.length()-1);
	director = str;	
		}
		
		public void myClickHandler(View view){
	switch(view.getId()){
	case R.id.sbutton1:
		sendSMS(number, smsText.getText().toString());	
		break;
	case R.id.sbutton2:
		finish();
		break;
	}
	finish();
		}
		
		private void sendSMS(String phoneNumber, String message) {

	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";

	PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
	SENT), 0);

	PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
	new Intent(DELIVERED), 0);
                    PendingIntent.getService(this,0,new Intent(DELIVERED) , 0);
	// ---when the SMS has been sent---
	registerReceiver(new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
	switch (getResultCode()) {
	case Activity.RESULT_OK:
		Toast.makeText(getBaseContext(), "SMS sent",
		Toast.LENGTH_SHORT).show();
		break;
	case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
		Toast.makeText(getBaseContext(), "Generic failure",
		Toast.LENGTH_SHORT).show();
		break;
	case SmsManager.RESULT_ERROR_NO_SERVICE:
		Toast.makeText(getBaseContext(), "No service",
		Toast.LENGTH_SHORT).show();
		break;
	case SmsManager.RESULT_ERROR_NULL_PDU:
		Toast.makeText(getBaseContext(), "Null PDU",
		Toast.LENGTH_SHORT).show();
		break;
	case SmsManager.RESULT_ERROR_RADIO_OFF:
		Toast.makeText(getBaseContext(), "Radio off",
		Toast.LENGTH_SHORT).show();
		break;
	}
		}
	}, new IntentFilter(SENT));

	// ---when the SMS has been delivered---
	registerReceiver(new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			
	switch (getResultCode()) { 
	case Activity.RESULT_OK:
		Toast.makeText(getBaseContext(), "SMS delivered",
		Toast.LENGTH_SHORT).show();
		break;
	case Activity.RESULT_CANCELED:
		Toast.makeText(getBaseContext(), "SMS not delivered",
		Toast.LENGTH_SHORT).show();
		break;
	}
		}
	}, new IntentFilter(DELIVERED));

	SmsManager sms = SmsManager.getDefault();
	sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	
	} 


}
