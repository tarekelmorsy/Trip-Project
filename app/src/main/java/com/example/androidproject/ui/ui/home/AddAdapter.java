package com.example.androidproject.ui.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddAdapter extends FirebaseRecyclerAdapter<Trip, AddAdapter.MyViewHolder> {
    int i = 0;
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

        holder.txvTripNam.setText(trip.getTripName());
        holder.txvEndPoint.setText(trip.getEndPoint());
        holder.txvStartPoint.setText(trip.getStartPoint());
        holder.tvTime.setText(trip.getAlarm());
        holder.tvDate.setText(trip.getDat());
        holder.setNotes(trip.getNotes());


        if (trip.getStatus().equals("1")) {
            holder.tvStatus.setText(R.string.upComing);
        } else if (trip.getStatus().equals("2")) {
            holder.tvStatus.setText(R.string.canceled);


        }
        holder.btCancel.setOnClickListener(v -> {

            Map<String, Object> map = new HashMap<>();
            map.put("alarm", trip.getAlarm());
            map.put("dat", trip.getDat());
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

                FirebaseDatabase.getInstance().getReference("trips").child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(holder.tvTime.getContext(), "Trip Cancel", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(holder.tvTime.getContext(), "Error", Toast.LENGTH_SHORT).show();


                    }
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
            Toast.makeText(holder.tvTime.getContext(), "hhhh", Toast.LENGTH_SHORT).show();

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
            tvDate.setText(trip.getDat());
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
                    Toast.makeText(holder.ivDelete.getContext(), "ggggggggg", Toast.LENGTH_SHORT).show();
                }
            };
            btUpdate.setOnClickListener(v1 -> {
                Map<String, Object> map = new HashMap<>();
                map.put("alarm", tvTime.getText().toString());
                map.put("dat", tvDate.getText().toString());
                map.put("endPoint", edEndPoint.getText().toString());
                map.put("startPoint", edStartPoint.getText().toString());
                map.put("tripName", edTripName.getText().toString());
                map.put("repeat", repeat.getText().toString());
                map.put("way", way.getText().toString());
                FirebaseDatabase.getInstance().getReference("trips").child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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

            });


        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.txvEndPoint.getContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("Delete data cant be Undo.");
                builder.setPositiveButton("Delete", (dialog, which) -> {

                    FirebaseDatabase.getInstance().getReference().child("trips").child(getRef(position).getKey()).removeValue();

                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(holder.tvTime.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                });
                builder.show();
            }

        });


    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txvStartPoint, txvEndPoint, txvTripNam, tvDate, tvTime, tvStatus, btStart, btCancel;
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
