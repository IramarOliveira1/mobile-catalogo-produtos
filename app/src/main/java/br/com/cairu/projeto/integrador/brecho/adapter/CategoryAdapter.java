package br.com.cairu.projeto.integrador.brecho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.dtos.category.CategoryResponseDTO;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnItemDeleteListener {
        void onUpdateItem(int position);
        void onItemDelete(int position);
    }

    private List<CategoryResponseDTO> list;
    private final OnItemDeleteListener deleteListener;

    public CategoryAdapter(List<CategoryResponseDTO> list, OnItemDeleteListener deleteListener) {
        this.list = list;
        this.deleteListener = deleteListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameCategory;

        public ImageButton btnDelete;

        public ImageButton btnUpdate;

        public ViewHolder(View view) {
            super(view);
            nameCategory = itemView.findViewById(R.id.nameCategory);
            btnUpdate = itemView.findViewById(R.id.btnUpdateCategory);
            btnDelete = itemView.findViewById(R.id.btnDeleteCategory);

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onUpdateItem(getAdapterPosition());
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onItemDelete(getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview_category, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryResponseDTO item = list.get(position);

        holder.nameCategory.setText(item.getName().toUpperCase());
        holder.btnUpdate.setId(item.getId().intValue());
        holder.btnDelete.setId(item.getId().intValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
