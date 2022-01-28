package com.example.androidproject.ui.ui.upcoming;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.androidproject.R;
import com.example.androidproject.data.Data;
import com.example.androidproject.data.Trip;
import com.example.androidproject.ui.AddTripActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddAdapter extends FirebaseRecyclerAdapter<Trip, AddAdapter.MyViewHolder> {
    int i = 0;
 static ArrayList<String> tripDate= new ArrayList<>();
    static ArrayList<String> tripHour= new ArrayList<>();
    static ArrayList<String> tripStatus= new ArrayList<>();


    public static int screen=1;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AddAdapter(@NonNull FirebaseRecyclerOptions<Trip> options) {
        super(options);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Trip trip) {

            viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRefreshLayout, String.valueOf(trip.getTripName()));
        viewBinderHelper.closeLayout(trip.getTripName());
        user= firebaseAuth.getCurrentUser();

//        if(trip.getUID().equals(user.getUid())) {

        holder.txvTripNam.setText(trip.getTripName());
        holder.txvEndPoint.setText(trip.getEndPoint());
        holder.txvStartPoint.setText(trip.getStartPoint());
        holder.tvTime.setText(trip.getAlarm());
        holder.tvDate.setText(trip.getDate());
        holder.setNotes(trip.getNotes());
        holder.tvSeRepeat.setText((trip.getRepeat()));
        holder.tvSetWay.setText(trip.getWay());

        if (trip.getStatus().equals(Data.UPCOMING)) {
            holder.tvStatus.setText(R.string.upComing);
        } else if (trip.getStatus().equals(Data.CANCEL)) {
            holder.tvStatus.setText(R.string.canceled);
            holder.btCancel.setVisibility(View.GONE);
        } else if (trip.getStatus().equals(Data.DONE)) {
            holder.tvStatus.setText(R.string.done);
            holder.btCancel.setVisibility(View.GONE);


        }

        if (trip.getStatus().equals(1)){


       }

        tripDate.add(position,trip.getDate());
        tripHour.add(position,trip.getAlarm());
        tripStatus.add(position,trip.getStatus());


       // Toast.makeText(holder.tvTime.getContext(),""+tripDate.size(), Toast.LENGTH_SHORT).show();



        holder.btCancel.setOnClickListener(v -> {


            Map<String, Object> map = new HashMap<>();
//            if (trip.getUID() != null)
//                map.put("UID", trip.getUID());
//            if (trip.getToken() != null)
//                map.put("UID", trip.getToken());

            map.put("alarm", trip.getAlarm());
            map.put("date", trip.getDate());
            map.put("endPoint", trip.getEndPoint());
            map.put("startPoint", trip.getStartPoint());
            map.put("tripName", trip.getTripName());
            map.put("repeat", trip.getRepeat());
            map.put("way", trip.getWay());
            map.put("status", Data.CANCEL);

            AlertDialog.Builder builder = new AlertDialog.Builder(holder.txvEndPoint.getContext());
            builder.setTitle("Are You Sure?");
            builder.setMessage("Cancel this Trip.");
            builder.setPositiveButton(R.string.cancel, (dialog, which) -> {
                FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid()).child(getRef(position).getKey()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("history"+user.getUid()).push().setValue(map);

                FirebaseDatabase.getInstance().getReference().child("tripCancel"+user.getUid()).push().setValue(map)
                        .addOnSuccessListener(unused ->
                                Toast.makeText(holder.txvEndPoint.getContext(), "Trip Cancel is Successfully.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Toast.makeText(holder.txvEndPoint.getContext(), "Error while Cancel", Toast.LENGTH_SHORT).show();

                        });


            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                Toast.makeText(holder.tvTime.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
            });
            builder.show();


        });

        holder.ivNotes.setOnClickListener(v -> {
            // Toast.makeText(v.getContext(), "position: "+position, Toast.LENGTH_SHORT).show();


            if (i == 1) {
                holder.linearLayout.setVisibility(View.VISIBLE);
                i++;
            } else {
                holder.linearLayout.setVisibility(View.GONE);
                i = 1;
            }
        });


        holder.ivEdit.setOnClickListener(v -> {

            DialogPlus dialog = DialogPlus.newDialog(holder.ivDelete.getContext())
                    .setContentHolder(new ViewHolder(R.layout.activity_add_trip))
                    .setExpanded(true, 1800)
                    .create();
            dialog.show();
            Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int[] t1Hour = new int[1];
            final int[] t1Minut = new int[1];
            View view = dialog.getHolderView();
            EditText edEndPoint = view.findViewById(R.id.edEndPoint);
            EditText edTripName = view.findViewById(R.id.edTripName);
            EditText edStartPoint = view.findViewById(R.id.edStartPoint);
            ImageView imvDate = view.findViewById(R.id.imvDate);
            ImageView imvTime = view.findViewById(R.id.imvTime);
            TextView tevTitle = view.findViewById(R.id.tevTitle);
            TextView tvDate = view.findViewById(R.id.tvDate);
            TextView tvTime = view.findViewById(R.id.tvTime);

            AutoCompleteTextView repeat = view.findViewById(R.id.repeat);
            AutoCompleteTextView way = view.findViewById(R.id.way);
            Button btUpdate = view.findViewById(R.id.btAdd);
            edStartPoint.setText(trip.getStartPoint());
            edEndPoint.setText(trip.getEndPoint());
            edTripName.setText(trip.getTripName());
            tvDate.setText(trip.getDate());
            tvTime.setText(trip.getAlarm());
            repeat.setText(trip.getRepeat());
            way.setText(trip.getWay());
            ArrayAdapter<String> arrayAdapterRepeat;
            ArrayAdapter<String> arrayAdapterWay;
            // add list in repeat and way
            arrayAdapterRepeat = new ArrayAdapter(holder.txvEndPoint.getContext(), R.layout.tv_entity, AddTripActivity.repeatList);
            repeat.setAdapter(arrayAdapterRepeat);
            arrayAdapterWay = new ArrayAdapter(holder.tvTime.getContext(), R.layout.tv_entity, AddTripActivity.wayList);
            way.setAdapter(arrayAdapterWay);

            //update title and button text
            tevTitle.setText(R.string.update_trip);
            btUpdate.setText(R.string.update_trip);

            way.setFocusable(false);
            repeat.setFocusable(false);
            //show timeDialog
            imvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(holder.tvTime.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            t1Hour[0] = hourOfDay;
                            t1Minut[0] = minute;

                            calendar.set(0, 0, 0, t1Hour[0], t1Minut[0]);
                            tvTime.setText(DateFormat.format("hh:mm aa", calendar));

                        }
                    }, 12, 0, false);
                    timePickerDialog.updateTime(t1Hour[0], t1Minut[0]);
                    timePickerDialog.show();
                }
            });
            imvDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                    DatePickerDialog datePickerDialog = new DatePickerDialog(holder.tvTime.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth
                            , AddTripActivity.listenerDate, year, month, day);
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
            });
            AddTripActivity.listenerDate = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month + 1;
                    String date = dayOfMonth + "/" + month + "/" + year;
                    tvDate.setText(date);
                }
            };
            btUpdate.setOnClickListener(v1 -> {

                if (screen == 1) {
                    Map<String, Object> map = new HashMap<>();

                    map.put("alarm", tvTime.getText().toString());
                    map.put("date", tvDate.getText().toString());
                    map.put("endPoint", edEndPoint.getText().toString());
                    map.put("startPoint", edStartPoint.getText().toString());
                    map.put("tripName", edTripName.getText().toString());
                    map.put("repeat", repeat.getText().toString());
                    map.put("way", way.getText().toString());
                    map.put("status", Data.UPCOMING);

                    FirebaseDatabase.getInstance().getReference("trips"+user.getUid()).child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(holder.tvTime.getContext(), "data Updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(holder.tvTime.getContext(), "Error", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    });
                } else if (screen == 2) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("alarm", tvTime.getText().toString());
                    map.put("date", tvDate.getText().toString());
                    map.put("endPoint", edEndPoint.getText().toString());
                    map.put("startPoint", edStartPoint.getText().toString());
                    map.put("tripName", edTripName.getText().toString());
                    map.put("repeat", repeat.getText().toString());
                    map.put("way", way.getText().toString());
                    map.put("status", Data.CANCEL);

                    FirebaseDatabase.getInstance().getReference().child("history"+user.getUid()).push().setValue(map);


                    FirebaseDatabase.getInstance().getReference("tripCancel"+user.getUid()).child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(holder.tvTime.getContext(), "data Updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(holder.tvTime.getContext(), "Error", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    });
                }

            });


        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txvEndPoint.getContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("Delete data cant be Undo.");
                builder.setPositiveButton("Delete", (dialog, which) -> {

                    if (screen == 1) {
                        FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid()).child(getRef(position).getKey()).removeValue();
                    } else if (screen == 2) {
                        FirebaseDatabase.getInstance().getReference().child("tripCancel"+user.getUid()).child(getRef(position).getKey()).removeValue();


                    } else if (screen == 3) {
                        FirebaseDatabase.getInstance().getReference().child("history"+user.getUid()).child(getRef(position).getKey()).removeValue();


                    }

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(holder.tvTime.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                });
                builder.show();
            }

        });
        holder.btStart.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("alarm", trip.getAlarm());
            map.put("date", trip.getDate());
            map.put("endPoint", trip.getEndPoint());
            map.put("startPoint", trip.getStartPoint());
            map.put("tripName", trip.getTripName());
            map.put("repeat", trip.getRepeat());
            map.put("way", trip.getWay());
            map.put("status", Data.DONE);
            if (screen == 1) {
                FirebaseDatabase.getInstance().getReference().child("trips"+user.getUid()).child(getRef(position).getKey()).removeValue();
            } else if (screen == 2) {
                FirebaseDatabase.getInstance().getReference().child("tripCancel"+user.getUid()).child(getRef(position).getKey()).removeValue();


            }
            FirebaseDatabase.getInstance().getReference().child("history"+user.getUid()).push().setValue(map)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(holder.txvEndPoint.getContext(), "Trip Cancel is Successfully.", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(holder.txvEndPoint.getContext(), "Error while Cancel", Toast.LENGTH_SHORT).show();

                    });

        });

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txvStartPoint, txvEndPoint, txvTripNam, tvDate, tvTime, tvStatus, btStart, btCancel,tvSetWay,tvSeRepeat;
        ImageView ivNotes, ivDelete, ivEdit;
        SwipeRevealLayout swipeRefreshLayout;
        ChipGroup chipGroup;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            txvStartPoint = itemView.findViewById(R.id.txvStartPointt);
            txvTripNam = itemView.findViewById(R.id.txvTripName);
            txvEndPoint = itemView.findViewById(R.id.txvEndPoint);
            btStart = itemView.findViewById(R.id.btStart);
            ivNotes = itemView.findViewById(R.id.ivNotes);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            btCancel = itemView.findViewById(R.id.btCancel);
            chipGroup = itemView.findViewById(R.id.chipGroupp);
            tvSetWay=itemView.findViewById(R.id.tvSetWay);
            tvSeRepeat=itemView.findViewById(R.id.tvSetRepeat);


            swipeRefreshLayout = itemView.findViewById(R.id.main_container);


        }

        public void setNotes(String s) {


            if (s != null) {
                String[] arrOfStr = s.split("#");
                for (String a : arrOfStr) {
                    Chip chip = new Chip(txvEndPoint.getContext());

                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(chip.getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Choice);
                    chip.setChipDrawable(chipDrawable);
                    chip.setPadding(10, 10, 10, 10);
                    chip.setText(a);

                    chipGroup.addView(chip);
                }
            }
        }
    }
}
