package com.example.movierating;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.content.Intent;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class MovieRatingActivity extends Activity implements RatingBar.OnRatingBarChangeListener
{
	private EditText movieName;
	private EditText userReview;
	private EditText releaseYear;
	private EditText movieDuration;
	private RatingBar yourRating;
	private EditText movieStarring;
	private EditText movieDirector;
	private Button mSaveButton;
	private Spinner mGenres;
	private ArrayAdapter<String> mAdapter;
	
	/*Array containing the genres to which a movie can belong*/
	private String[] mGenresData = 
		{
		"-Select Genre-",
		"Action","Adventure","Animation","Biography",
		"Comedy","Crime","Documentary","Drama",
		"Family","Fantasy","Film-Noir","Game-Show",
		"History","Horror","Music","Musical",
		"Mystery","News","Reality-TV","Romance",
		"Sci-Fi","Sport","Talk-Show","Thriller",
		"War","Western"
	};


	@Override
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	/* Initialize variables */
	movieName = (EditText)findViewById(R.id.editText2);
	userReview = (EditText)findViewById(R.id.editText1);
	releaseYear = (EditText)findViewById(R.id.editText3);
	movieDuration = (EditText)findViewById(R.id.editText4);
	yourRating = (RatingBar)findViewById(R.id.ratingBar1);
	movieStarring = (EditText)findViewById(R.id.editText5);
	movieDirector = (EditText)findViewById(R.id.editText6);
	mSaveButton = (Button)findViewById(R.id.button1);    
	yourRating.setOnRatingBarChangeListener (this);
	mSaveButton.setOnClickListener(new OnButtonClick());
	mGenres = (Spinner)findViewById(R.id.spinner1);
	
	mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mGenresData);    
	mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
	mGenres.setAdapter(mAdapter);
	movieDirector.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent mAutoCompleteIntent = new 
	            Intent(MovieRatingActivity.this,DirectorName.class);
			startActivityForResult(mAutoCompleteIntent, 2);
		}
	});

	/*Spinner cRaceSpinner = (Spinner) findViewById(R.id.spinnerOfCharacterRaces);
	cRaceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	   public void onItemSelected(AdapterView<?> arg0, View arg1,int  arg2, long arg3) {
	      String strChosenRace =(String)arg0.getItemAtPosition(arg2);
	     
	   }
	   public void onNothingSelected(AdapterView<?> arg0) {}
	
	});*/
	}
	public class OnButtonClick implements OnClickListener{
		@Override
		public void onClick(View arg0) {
	/* Obtain values filled by the user in String 
	   variables */
	  	    	String str1 = releaseYear.getText().toString();
			String str2= movieDuration.getText().toString();
			String str3 = movieStarring.getText().toString();
			String str4 = movieDirector.getText().toString();
			String str5 = movieName.getText().toString();			
			String str = userReview.getText().toString();
			if(mGenres.getSelectedItem().toString().equalsIgnoreCase("-Select Genre-")){
				Toast.makeText(MovieRatingActivity.this,"Please Select valid Genre", 
			Toast.LENGTH_SHORT).show();
				return;
			}

			
			/* Check if any of the values is blank. Display a message 
	               accordingly */
		
			if(str1.equalsIgnoreCase("") || str2.equalsIgnoreCase("")  
			|| str3.equalsIgnoreCase("") || str5.equalsIgnoreCase("") 
			|| str4.equalsIgnoreCase("") || str.equalsIgnoreCase(""))
	{
				Toast.makeText(MovieRatingActivity.this,"Please fill all the fields.", Toast.LENGTH_SHORT).show(); 
			}
			else{
				String name = movieName.getText().toString();
				String year = releaseYear.getText().toString();
				String duration = movieDuration.getText().toString();
				String review = userReview.getText().toString();
				String director = movieDirector.getText().toString();
				String starcast = movieStarring.getText().toString();
				double rating = yourRating.getRating();
				String genre = mGenres.getSelectedItem().toString();
				//Creates a contentvalues object to put the form values 
				ContentValues values = new ContentValues();
				//adding values to the contentvalues
				values.put("name", name);
				values.put("genre", genre);  
				values.put("year", year);
				values.put("duration", duration);
				values.put("rating", rating);
				values.put("review", review);
				values.put("starcast", starcast);
				values.put("director", director);
	
				//inserting values into database using the Uri of the content provider
				Uri uri = getContentResolver().insert(
				Uri.parse(
				      "content://com.movierating.provider.Movie/reviews"), 
				      values);
				Intent mReviewListIntent = new Intent(MovieRatingActivity.this, 	ReviewListActivity.class);
				startActivity(mReviewListIntent);

			}
	   	   }
	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try{
			if ((requestCode == 2 ) && (resultCode == 
	              Activity.RESULT_OK)){
				Bundle myResults = data.getExtras();
				String vresult = myResults.getString("key");
				movieDirector.setText(vresult);
			}
		}
		catch (Exception e) {
			movieDirector.setText("Oops! - " + requestCode + " " + 
			resultCode);
		}
	}

	public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
		// Set the rating as specified by the user
		yourRating.setRating(arg1);		
}


}
