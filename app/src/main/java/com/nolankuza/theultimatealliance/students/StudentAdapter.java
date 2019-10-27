package com.nolankuza.theultimatealliance.students;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Student;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> students;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public StudentAdapter(Context context, List<Student> students) {
        this.inflater = LayoutInflater.from(context);
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);
        View itemView = holder.itemView;
        ((TextView)itemView.findViewById(R.id.item_student_first_name)).setText(student.firstName);
        ((TextView)itemView.findViewById(R.id.item_student_last_name)).setText(student.lastName);
        ((TextView)itemView.findViewById(R.id.item_student_grade)).setText(String.format(Locale.US, "%d", student.grade));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.findViewById(R.id.item_student_delete).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onRemoveClick(view, getAdapterPosition());
        }
    }

    public void addItem(Student student) {
        students.add(student);
        notifyItemInserted(getItemCount() - 1);
        Collections.sort(students, new Student.Compare());
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        students.remove(position);
        notifyItemRemoved(position);
    }

    public Student getItem(int id) {
        return students.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onRemoveClick(View view, int position);
    }
}
