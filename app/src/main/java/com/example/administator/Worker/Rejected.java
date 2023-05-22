package com.example.administator.Worker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administator.Demo.User;
import com.example.administator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Rejected extends Fragment {

    RecyclerView recyclerView;
    RejectedAdapter user_listAdaptor;
    List<User> user_list;
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_rejected, container, false );
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        recyclerView = view.findViewById( R.id.rcRejected );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( view.getContext() ) );
        recyclerView.getLayoutManager().setMeasurementCacheEnabled( false );
        user_list = new ArrayList<>();

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference( "Maruti Daim" ).child( "User" ).child( "Rejected" );
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
        user_listAdaptor = new RejectedAdapter( getActivity(), user_list );
        recyclerView.setAdapter( user_listAdaptor );
    }

    public class RejectedAdapter extends RecyclerView.Adapter<ViewHolder> {
        Context context;
        List<User> user_list;

        public RejectedAdapter(Context context, List<User> user_list) {
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
            Picasso.get().load( user.getPicture() ).into( holder.profileImage ); }

        @Override
        public int getItemCount() {
            return user_list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView accepts, reject, profileImage;
        TextView userName, userMobile, userRole;

        public ViewHolder(View itemView) {
            super( itemView );
            userRole = itemView.findViewById( R.id.userRole );
            profileImage = itemView.findViewById( R.id.profileImage );
            userMobile = itemView.findViewById( R.id.userMobile );
            userName = itemView.findViewById( R.id.userName );

            accepts = itemView.findViewById( R.id.accept );
            reject = itemView.findViewById( R.id.reject );
            reject.getLayoutParams().height = 0;
            reject.getLayoutParams().width = 0;
            accepts.getLayoutParams().height = 0;
            accepts.getLayoutParams().width = 0;
        }
    }
}