package com.example.administator.Worker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administator.Demo.User;
import com.example.administator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RequestPending extends Fragment {

    RecyclerView recyclerView;
    RequestPendingAdapter user_listAdaptor;
    List<User> user_list;
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_request, container, false );
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        recyclerView = view.findViewById( R.id.rcRequest );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( view.getContext() ) );
        recyclerView.getLayoutManager().setMeasurementCacheEnabled( false );
        user_list = new ArrayList<>();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference( "Maruti Daim" ).child( "User" ).child( "Pending" );

        getdata();

    }

    public void getdata() {
        myRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot studentsnapshot : dataSnapshot.getChildren()) {
                        User studentModel = studentsnapshot.getValue( User.class );
                        System.out.println( "------------" + studentsnapshot.getValue( User.class ) + "/   " + studentsnapshot );
                        user_list.add( studentModel );
                    }
                    user_listAdaptor.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );

        user_listAdaptor = new RequestPendingAdapter( getActivity(), user_list );
        recyclerView.setAdapter( user_listAdaptor );
    }

    public class RequestPendingAdapter extends RecyclerView.Adapter<RequestPendingAdapter.ViewHolder> {
        Context context;
        List<User> user_list;
        DatabaseReference myRef4, myRef1, myRef2, myRef3;

        public RequestPendingAdapter(Context context, List<User> user_list) {
            this.context = context;
            this.user_list = user_list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.rc_user_info, parent, false );
            return new ViewHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            User user = user_list.get( position );
            holder.userName.setText( user.getName() );
            holder.userMobile.setText( user.getMobile() );
            holder.userRole.setText( user.getRole() );
            Picasso.get().load( user.getPicture() ).into( holder.profileImage );

            ProgressDialog progressdialog;
            progressdialog = new ProgressDialog( getContext() );
            progressdialog.setMessage( "Please Wait...." );
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            holder.accepts.setOnClickListener( view -> {
                progressdialog.show();

                myRef4 = database.getReference( "Maruti Daim" ).child( "User" ).child( "Pass" ).child( user.getUid() );
                myRef4.child( "BirthDate" ).setValue( user.getBirthDate() );
                myRef4.child( "Mobile" ).setValue( user.getMobile() );
                myRef4.child( "Name" ).setValue( user.getName() );
                myRef4.child( "Picture" ).setValue( user.getPicture() );
                myRef4.child( "Role" ).setValue( user.getRole() );
                myRef4.child( "Uid" ).setValue( user.getUid() );

                myRef2 = FirebaseDatabase.getInstance().getReference( "Worker" ).child( user.getRole() ).child( user.getUid() );
                myRef2.child( "BirthDate" ).setValue( user.getBirthDate() );
                myRef2.child( "Mobile" ).setValue( user.getMobile() );
                myRef2.child( "Name" ).setValue( user.getName() );
                myRef2.child( "Picture" ).setValue( user.getPicture() );
                myRef2.child( "Role" ).setValue( user.getRole() );
                myRef2.child( "Uid" ).setValue( user.getUid() );

                myRef1 = FirebaseDatabase.getInstance().getReference( "Maruti Daim" ).child( "User" ).child( "Pending" ).child( user.getUid() );
                Query query = myRef1;
                query.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue().addOnCompleteListener( new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText( context, "New User added", Toast.LENGTH_SHORT ).show();
                                getdata();
                                progressdialog.dismiss();
                            }
                        } );
                        dataSnapshot.getRef().removeValue().addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( context, "Try again", Toast.LENGTH_SHORT ).show();
                                progressdialog.dismiss();
                            }
                        } );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                } );
            } );

            holder.reject.setOnClickListener( v -> {
                progressdialog.show();
                myRef3 = FirebaseDatabase.getInstance().getReference( "Maruti Daim" ).child( "User" ).child( "Rejected" ).child( user.getUid() );
                myRef3.child( "BirthDate" ).setValue( user.getBirthDate() );
                myRef3.child( "Mobile" ).setValue( user.getMobile() );
                myRef3.child( "Name" ).setValue( user.getName() );
                myRef3.child( "Picture" ).setValue( user.getPicture() );
                myRef3.child( "Role" ).setValue( user.getRole() );
                myRef3.child( "Uid" ).setValue( user.getUid() );

                myRef1 = FirebaseDatabase.getInstance().getReference( "Maruti Daim" ).child( "User" ).child( "Pending" ).child( user.getUid() );
                Query query = myRef1;
                query.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue().addOnCompleteListener( new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressdialog.dismiss();
                                Toast.makeText( context, "New User added", Toast.LENGTH_SHORT ).show();
                                getdata();
                            }
                        } );
                        dataSnapshot.getRef().removeValue().addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( context, "Try again", Toast.LENGTH_SHORT ).show();
                                progressdialog.dismiss();
                            }
                        } );
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                } );
            } );
        }

        @Override
        public int getItemCount() {
            return user_list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView accepts, reject, profileImage;
            TextView userName, userMobile, userRole;

            public ViewHolder(View itemView) {
                super( itemView );
                userRole = itemView.findViewById( R.id.userRole );
                profileImage = itemView.findViewById( R.id.profileImage );
                accepts = itemView.findViewById( R.id.accept );
                reject = itemView.findViewById( R.id.reject );
                userMobile = itemView.findViewById( R.id.userMobile );
                userName = itemView.findViewById( R.id.userName );
            }
        }
    }
}