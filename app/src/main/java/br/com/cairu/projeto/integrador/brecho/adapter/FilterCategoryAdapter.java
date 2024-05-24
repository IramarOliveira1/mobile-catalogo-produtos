package br.com.cairu.projeto.integrador.brecho.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;

public class FilterCategoryAdapter extends RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder> {

    public interface onItemClickListener {
        void onItemClick(String category, int position);
    }

    private List<String> categoryList;
    private final onItemClickListener listener;

    private int selectedPosition = 0;

    public FilterCategoryAdapter(List<String> list, onItemClickListener deleteListener) {
        this.categoryList = list;
        this.listener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview_filter_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categoryList.get(position);

        holder.categoryName.setText(category.toUpperCase());

        if (position == selectedPosition) {
            holder.categoryName.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_btn_delete));
            holder.categoryName.setTextColor(Color.WHITE);
        } else {
            holder.categoryName.setBackgroundColor(Color.TRANSPARENT);
            holder.categoryName.setTextColor(Color.parseColor("#888888"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
                listener.onItemClick(category, selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public ViewHolder(View view) {
            super(view);
            categoryName = itemView.findViewById(R.id.categoryNameFilter);
        }
    }
}
