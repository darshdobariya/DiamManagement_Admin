package com.example.administator.ProcessDaim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administator.Demo.User;
import com.example.administator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProcessDaim extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] department = { "Galaxy", "Machine", "Polish" };
    TextView date;
    String kapan1, ruffWeight1, daimNo1, expWeight1;
    int position = 0, size = 0;
    DatabaseReference mRef;
    Spinner areaSpinner;
    Button addDAta;
    EditText kapan, ruffWeight, daimNo, expWeight;
    List<String> name, uid, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_process_daim );

        kapan = findViewById( R.id.kapan );
        ruffWeight = findViewById( R.id.ruffWeight );
        daimNo = findViewById( R.id.daimNo );
        expWeight = findViewById( R.id.expWeight );
        date = findViewById( R.id.date );
        addDAta = findViewById( R.id.addData );

        Spinner spino = findViewById(R.id.coursesspinner);
        


        spino.setOnItemSelectedListener(this);

        // Role Select
        ArrayAdapter ad = new ArrayAdapter( ProcessDaim.this, android.R.layout.simple_spinner_item, department);
        ad.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spino.setAdapter(ad);

        getDateTime();

        date.setOnClickListener( v->{
            DatePick();
        } );

        addDAta.setOnClickListener( v->{
            kapan1 = kapan.getText().toString();
            ruffWeight1 = ruffWeight.getText().toString();
            daimNo1 = daimNo.getText().toString();
            expWeight1 = expWeight.getText().toString();

            if (spino.getSelectedItemPosition() == 0 && kapan1.length() >= 1 && ruffWeight1.length() >= 1 && daimNo1.length() >= 1 && expWeight1.length() >= 1 ) {
                System.out.println(" -----" + kapan1 + ruffWeight1 + daimNo1 + expWeight1);
                addData(kapan1, ruffWeight1, daimNo1, expWeight1);
            }else Toast.makeText( this, "Select working position", Toast.LENGTH_SHORT ).show();
        } );
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Maruti Daim").child("User").child( "Pass" );
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = new ArrayList<String>();
                uid = new ArrayList<String>();
                mobile = new ArrayList<String>();
                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    User studentModel = areaSnapshot.getValue( User.class );

                    if(department[i].equals(studentModel.getRole())){
                        String areaName = studentModel.getName();
                        name.add(areaName);
                        uid.add(studentModel.getUid());
                        mobile.add(studentModel.getMobile());
                    }
                }

                areaSpinner = (Spinner) findViewById(R.id.spinner2);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(ProcessDaim.this, android.R.layout.simple_spinner_item, name);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.getSelectedItemPosition();
                areaSpinner.setAdapter(areasAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = new Date();
        date.setText( dateFormat.format(date1) );
        return dateFormat.format(date1);
    }

    private void DatePick(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog( ProcessDaim.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void addData(String kapan, String RuffWeight, String daimNo, String expWeight){

        mRef = FirebaseDatabase.getInstance().getReference("Maruti Daim").child( "Kapan" ).child( kapan );
        mRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) { size++;
                    if (daimNo.toString().equals( snapshot1.getKey() )){
                        position += 1;
                    }
                }
                if (position == 0){
                    size = 0;

                    ProgressDialog progressdialog = new ProgressDialog(ProcessDaim.this);
                    progressdialog.setMessage("Please Wait....");
                    progressdialog.show();

                    mRef.child( daimNo ).child( "Kapan" ).setValue( kapan );
                    mRef.child( daimNo ).child( "DaimNo" ).setValue( daimNo );
                    mRef.child( daimNo ).child( "ExpectedWeight" ).setValue( expWeight );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "EndDate" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "Less" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "ReadyWeight" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "SendTo" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "Ready" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "StartDate" ).setValue( date.getText().toString() );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "Status" ).setValue( "Pending" );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "User" ).child( "Mobile" ).setValue( mobile.get( areaSpinner.getSelectedItemPosition() ) );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "User" ).child( "Name" ).setValue( name.get( areaSpinner.getSelectedItemPosition() ) );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "User" ).child( "Uid" ).setValue( uid.get( areaSpinner.getSelectedItemPosition() ) );

                    mRef.child( daimNo ).child( "Manufacture" ).child( "Machine" ).child( "EndDate" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Machine" ).child( "Less" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Machine" ).child( "ReadyWeight" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "SendTo" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "Ready" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Machine" ).child( "StartDate" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Machine" ).child( "Status" ).setValue( "Pending" );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Machine" ).child( "User" ).child( "Mobile" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Machine" ).child( "User" ).child( "Name" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Machine" ).child( "User" ).child( "Uid" ).setValue( " " );

                    mRef.child( daimNo ).child( "Manufacture" ).child( "Polish" ).child( "EndDate" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Polish" ).child( "Less" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Polish" ).child( "ReadyWeight" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "SendTo" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Galaxy" ).child( "Ready" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Polish" ).child( "StartDate" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Polish" ).child( "Status" ).setValue( "Pending" );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Polish" ).child( "User" ).child( "Mobile" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Polish" ).child( "User" ).child( "Name" ).setValue( " " );
                    mRef.child( daimNo ).child( "Manufacture" ).child( "Polish" ).child( "User" ).child( "Uid" ).setValue( " " );

                    mRef.child( daimNo ).child( "EndDate" ).setValue( " " );
                    mRef.child( daimNo ).child( "TotalPic" ).setValue( " " );
                    mRef.child( daimNo ).child( "ReadyPic" ).setValue( " " );
                    mRef.child( daimNo ).child( "ReadyWeight" ).setValue( " " );
                    mRef.child( daimNo ).child( "RuffWeight" ).setValue( RuffWeight );
                    mRef.child( daimNo ).child( "StartDate" ).setValue( date.getText().toString() );
                    mRef.child( daimNo ).child( "Status" ).setValue( "Pending" );
                    mRef.child( daimNo ).child( "Total" ).setValue( " " );

                    Toast.makeText( ProcessDaim.this, "New ", Toast.LENGTH_SHORT ).show();

                    progressdialog.dismiss();
                }else {
                    position = 0; size = 0;
                    Toast.makeText( ProcessDaim.this, "Daim Already Exist", Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        } );
    }
}