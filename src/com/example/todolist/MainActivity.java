package com.example.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, DialogInterface.OnClickListener{
	EditText textitem;
	Button savebtn;
	ListView listview;
	ArrayList<String> list;
	ArrayAdapter<String> dap;
	DBHelper mydb;
	Long rowid;
	
	String [] eating={"Fish", "Meat", "Grilled Chicken", "Potato", "Salad"};
	String [] travelling={"Spain", "France", "Austria", "UK", "USA", "Canada"};
	String [] games={"Chess", "Monopoly", "Backgammon", "Sudoku", "Uno", "Snakes and Ladders", "Dominoes"};
	String [] sports={"Play football", "Ping Pong", "Tennis", "Swimming", "Basketball", "Vollyball", "Squash"};
	String [] doing={"Watch TV", "Listen to Music", "Go to Gym", "Read a Book", "Cook", "Do Shopping"};
	
	String [] data={"Edit", "Delete"};
	String selectedmenuitem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textitem=(EditText) findViewById(R.id.textitem);
		savebtn =(Button) findViewById(R.id.savebtn);
		listview=(ListView) findViewById(R.id.listview);
		
		mydb = new DBHelper(this);
		
		list=new ArrayList<String>();
		
		dap=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		
		  Cursor cursor = mydb.getListItem();
			
			if(cursor!=null){
				
			   //Toast.makeText(this, "number of rows "+cursor.getCount(), Toast.LENGTH_LONG).show();
				
				 if (cursor.moveToFirst()) {
		                do {
		                	String data = cursor.getString(1);
		                    list.add(data);
		                } while (cursor.moveToNext());
		            } 
			}
			
		cursor.close();

		listview.setAdapter(dap);
		
		
		savebtn.setOnClickListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
			final	AlertDialog.Builder listbuilder=new AlertDialog.Builder(MainActivity.this);
				listbuilder.setItems(data, MainActivity.this);
				listbuilder.show();
				final int position=arg2;
				//Cursor adapter is specialized adapter for a list view, when the data comes from database
				
				//Toast.makeText(MainActivity.this, "# "+rowid, Toast.LENGTH_LONG).show();

				listbuilder.setItems(data,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//edit item
						if(which==0){
						final AlertDialog.Builder alertedit = new AlertDialog.Builder(MainActivity.this);
	                        final EditText input = new EditText(MainActivity.this); 
	                        alertedit.setTitle("Do you want to edit the item "+"''"+dap.getItem(position).toString()+"''");
						    alertedit.setView(input);
	                        input.setText(dap.getItem(position).toString());  
	                      
	                        alertedit.setNegativeButton("Discard", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
	                         
	                       alertedit.setPositiveButton("Save", new DialogInterface.OnClickListener () {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								if(input.getText()!=null){
								
								String editdata = input.getText().toString();
                                
								 Integer id =  mydb.updatedatabase(editdata, position);
									  
								 //Toast.makeText(MainActivity.this, "# "+id, Toast.LENGTH_LONG).show();
								 
									dap.clear();
			                    	
			                    	dap.notifyDataSetChanged();
			                    	
			                    	
			                    	if(id>0){
			                    		
			                    		  Cursor cursor = mydb.getListItem();
			                  			
			                  			if(cursor!=null){
			                  				
			                  			   if (cursor.moveToFirst()) {
			                  		                do {
			                  		                	String data = cursor.getString(1);
			                  		                    dap.add(data);
			                  		                } while (cursor.moveToNext());
			                  		            } 
			                  			}
			                  			
			                  			cursor.close();
			                    		
			                    	} 
								 								   
								}
							
							}
						});
	                       //these 2 lines to create and show the dialog inside the dialog with + and - button
	                       AlertDialog alertDialogedit = alertedit.create();
	                       alertDialogedit.show();  
							
					}
						
						if(which==1){
							//delete item
							AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
					        alert.setTitle("Do you want to delete the item "+"''"+dap.getItem(position).toString()+"''");
					        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int whichButton) {
		                        // Canceled.
		                        dialog.cancel();
		                    }
		                });
					 
					        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int whichButton) {
		                    	
		                    	int result = mydb.deleteItem(position);
		                    	
	                    		dap.clear();
		                    	
		                    	dap.notifyDataSetChanged();
		                    	
		                    	
		                    	if(result>0){
		                    		
		                    		  Cursor cursor = mydb.getListItem();
		                  			
		                  			if(cursor!=null){
		                  				
		                  			   if (cursor.moveToFirst()) {
		                  		                do {
		                  		                	String data = cursor.getString(1);
		                  		                    dap.add(data);
		                  		                } while (cursor.moveToNext());
		                  		            } 
		                  			}
		                  			
		                  			cursor.close();
		                    		
		                    	}
                                
		                    }
		                });
					        AlertDialog alertDialog = alert.create();
		                    alertDialog.show();
							
						}
					}
				} );
			}
		});
	}

	   
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			
			super.onCreateOptionsMenu(menu);
			menu.add("Do Something");
			menu.add("Eat Healthy");
			menu.add("Playing Games");
			menu.add("Playing Sports");
			menu.add("Travel");
            return true;	
        }
		
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			 super.onOptionsItemSelected(item);
			 
			 if(item.getTitle()=="Do Something"){
				 displayDialogData("Do Something", doing);  
			 }
			 
			 if(item.getTitle()=="Eat Healthy"){
				 displayDialogData("Eat Healthy", eating);  
			 }
			 
			 if(item.getTitle()=="Playing Games"){
				 displayDialogData("Playing Games", games);  
			 }
			 
			 if(item.getTitle()=="Playing Sports"){
				 displayDialogData("Playing Sports", sports);  
			 }
			 
			 if(item.getTitle()=="Travel"){
				 displayDialogData("Travel", travelling);  
			 }

			 selectedmenuitem=item.getTitle().toString();
			 return true;
		}
		
		public void displayDialogData(String title, String[] item) {
			
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle(title);
			builder.setItems(item, this);
			builder.show();
		}

	@Override
	public void onClick(View v) {
		if(v==savebtn){
			
		     String item=textitem.getText().toString().trim();
		     
		     if(!item.isEmpty() && item.length()>0){
		    	 additems(item);
				 mydb.addListItems(item);
				
		       }else {Toast.makeText(this, "please enter something", Toast.LENGTH_LONG).show();
		    	 
		     }
		}
}		


	public void additems(String item) {
	      
		     //this.list.add(item);
		      dap.add(item);
		      this.dap.notifyDataSetChanged();
		      textitem.setText("");     
    }
	
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
         
		if(selectedmenuitem=="Do Something"){
			additems(doing[which]);	
		    mydb.addListItems(doing[which]);
		}
		if(selectedmenuitem=="Eat Healthy"){
			additems(eating[which]);
			mydb.addListItems(eating[which]);
		}
		if(selectedmenuitem=="Playing Games"){
			additems(games[which]);	
			mydb.addListItems(games[which]);
		}
		if(selectedmenuitem=="Playing Sports"){
			additems(sports[which]);
			mydb.addListItems(sports[which]);
		}
		if(selectedmenuitem=="Travel"){
			additems(travelling[which]);
			mydb.addListItems(travelling[which]);
		}
		
	}

   public void addDataToDatabase() {
	 
	
}
	
}
