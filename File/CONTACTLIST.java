package com.example.movierating;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter; 



public class CONTACTLIST extends Activity 
{
	private ListView list;
	private String[] fields;
	private Cursor cursor;
	private int id;	
	
	@Override
	public void onCreate(Bundle s){
super.onCreate(s);
setContentView(R.layout.contactlist);
list = (ListView) findViewById(R.id.list1);
show_contacts();
Intent myIntent=getIntent();
	    Bundle myBundle=myIntent.getExtras();
	    id = myBundle.getInt("id");	    
	}


private void show_contacts() {
cursor = fetchcontact();
	        fields = new String[] {
	                ContactsContract.Data.DISPLAY_NAME
	        };
	        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contactlistitem, cursor, fields, new int[] {R.id.contacttext},0);
	        list.setAdapter(adapter);
	        list.setOnItemClickListener(new OnItemClickListener(){
@Override
public void onItemClick(AdapterView<?> arg0, View arg1,
int arg2, long arg3) {
	// TODO Auto-generated method stub
    Intent intent = new Intent(getApplicationContext(), sendSMS.class);
	Log.v("Status",String.valueOf(arg2));
	Bundle myBundle= new Bundle();
	myBundle.putString("data1", cursor.getString(cursor.getColumnIndex(fields[0])));
	myBundle.putString("data2",String.valueOf(ContactsContract.Contacts.CONTENT_URI));
	myBundle.putInt("id", id);	
	intent.putExtras(myBundle);	
	try{Log.v("Status",cursor.getString(cursor.getColumnIndex(fields[0])));}catch(Exception e){Log.e("Error", e.toString());}

startActivity(intent);
}	        	
        });
}

private Cursor fetchcontact() {
String[] projection = new String[] { ContactsContract.Contacts._ID,
ContactsContract.Contacts.DISPLAY_NAME};
return getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, projection, "1", null, null);
}
}
