package com.example.movierating;

import android.app.ListActivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ContextMenu.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.database.Cursor;
import android.net.Uri;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.app.AlertDialog;


public class ReviewListActivity extends ListActivity {
	/* Variable declarations */
	private ListView mRatingList;
	private List<String> mReviewData = new ArrayList<String>();
	private final int DELETE=2;
	private final int SHARE=1;
	private ArrayAdapter<String>mReview;
	private int lRecId;
	

	/* Override the onCreate() method */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	super.onCreate(savedInstanceState);
	//setContentView(R.layout.reviewlist);

	/* Variable initialization */
	//mRatingList = (ListView)findViewById(android.R.id.list);
	String columns[] = new String[] { "name" };

	Uri movie = Uri.parse(

	        "content://com.movierating.provider.Movie/reviews/");

	ContentResolver cr = getContentResolver();

	Cursor c = cr.query(movie, columns, null, null, null );

	if (c.moveToFirst()) {

	do {

	// Get the field values

	mReviewData.add(c.getString(c

	.getColumnIndex("name")));

	} while (c.moveToNext());

	}

	if (c != null && !c.isClosed()) {

	c.close();}


	/* Create an array adapter to initialize the list view */
	//ArrayAdapter<String> mReviewList = new 
	  //     ArrayAdapter<String>(this,R.layout.listentry, mReviewData);
	//setListAdapter(mReviewList);

	/* Create OnItemClickListener for the list view */
	//mRatingList.setOnItemClickListener(new OnItemClickListener() {
		//@Override
		//public void onItemClick(AdapterView<?> arg0, View arg1, int 
	      //arg2, long arg3) {
		// TODO Auto-generated method stub
		/* Create an intent to invoke the Display Review activity */
		//Intent mReviewListIntent = new 
	      // Intent(ReviewListActivity.this, 
	      // DisplayReviewActivity.class);

		/* Create a Bundle object to pass the id of the selected 
	         record */
		//Bundle mBundle = new Bundle();
		//mBundle.putInt("id", arg2);
		//mReviewListIntent.putExtras(mBundle);
		/* Start an activity by using the details included in the 
	         intent */
		//startActivity(mReviewListIntent);
	}
	//});
	
	@Override
	public void onResume()
	{
		super.onResume();
		refreshList();
	}
	public void refreshList()
	{
		mReviewData.clear();
		String columns[]=new String []{"name"};
		Uri movie=Uri.parse("content://com.movierating.provider.Movie/reviews");
		ContentResolver cr=getContentResolver();
		Cursor c=cr.query(movie,columns,null,null,null);
		if(c.moveToFirst())
		{do
		{mReviewData.add(c.getString(c.getColumnIndex("name")));
		}
		while(c.moveToNext());
		}
		setLayout();
		if(c!=null&&!c.isClosed())
		{
			c.close();
			
		}
		}
	//Sets the layout when the activity is started or resumed
	private void setLayout() {
	// TODO Auto-generated method stub
	if(!mReviewData.isEmpty()){	
		setContentView(R.layout.reviewlist);
		mRatingList = (ListView)findViewById(android.R.id.list);
		mReview = new ArrayAdapter<String>(this,R.layout.list_item, mReviewData);	 
		setListAdapter(mReview);
		registerForContextMenu(getListView());
		mRatingList.setOnItemClickListener(new OnItemClickListener() {
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int 
	                           arg2,long arg3) {
		// TODO Auto-generated method stub
		Intent mReviewListIntent = new 
	                                 Intent(ReviewListActivity.this, 
		DisplayReviewActivity.class);

		Bundle mBundle = new Bundle();
		mBundle.putInt("id", arg2);
		mReviewListIntent.putExtras(mBundle);
		startActivity(mReviewListIntent);
	}
		});
	}else{

		setContentView(R.layout.noreview);
	}
	}
	private AlertDialog showDeleteDialog(int recId) {
		// TODO Auto-generated method stub
		lRecId=recId;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete this review?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new 			  
		       DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Uri movie = Uri.parse("content://com.movierating.provider.Movie/reviews/");

		                getApplicationContext().getContentResolver().delete 
		                (movie, " rowid = (select max(rowid) from (select rowid from Review limit "+(lRecId+1)+"));", null);
		        	    refreshList();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener()       
		       {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		                dialog.cancel();
		           }
		       });
		       AlertDialog alert = builder.create();	
		       return alert;
		}


	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.menu, menu);
		return true; 
	}

	//Functionality for the menu options

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.add:
		/* Create an intent to launch the Movie Rating activity */	
	Intent myIntent2 = new 
	Intent(getApplicationContext(),MovieRatingActivity.class);
	startActivity(myIntent2);
	return(true);
		case R.id.exit:
		/* Close the application */
	System.exit(0);
	return(true);	
		}	
		return(super.onOptionsItemSelected(item));
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		// TODO Auto-generated method stub
		menu.setHeaderTitle("-CONTEXT-");
		menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete");
		menu.add(Menu.NONE, SHARE, Menu.NONE, "Share");
	}
	

	// Add functionality to the context menu items 

	@Override
	public boolean onContextItemSelected(MenuItem item) {	 
	      AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	      int id = (int) getListAdapter().getItemId(info.position);

	/* Check which item was selected in ListView and take an action accordingly */
	       switch (item.getItemId()) {
	              case DELETE:
	            	   //Uri movie = Uri.parse("content://com.movierating.provider.Movie/reviews/");
	  //getApplicationContext().getContentResolver().delete(movie, " rowid = (select max(rowid) from (select rowid from Review limit "+(id+1)+"));", null);
	                  // refreshList();
	            	  AlertDialog ad=showDeleteDialog(id);
	            	  ad.show();
	                   return(true);

	              case SHARE:
	            	  
	            	  Intent in=new Intent(this,CONTACTLIST.class);
	            	  Bundle b=new Bundle();
	            	  b.putInt("id",id);
	            	  in.putExtras(b);
	            	  new ListActivity().startActivity(in);
	                   return(true);   
	      }
	  return(super.onOptionsItemSelected(item));
	}

	/*SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    @Override
       public void onClick(View v) {
          SharedPreferences editor = sharedpreferences.edit();

          editor.putString(Name, n);
          editor.putString(Phone, ph);
          editor.putString(Email, e);
          editor.commit();
          Toast.makeText(MainActivity.this,"Thanks",Toast.LENGTH_LONG).show();
       }
    });
 }*/





}	


