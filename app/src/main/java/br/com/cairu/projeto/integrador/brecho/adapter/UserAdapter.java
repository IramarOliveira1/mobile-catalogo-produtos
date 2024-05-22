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
import br.com.cairu.projeto.integrador.brecho.dtos.user.UserResponseDTO;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public interface OnItemDeleteListener {
        void onUpdateItem(int position);
        void onItemDelete(int position);
    }

    private List<UserResponseDTO> list;
    private final OnItemDeleteListener deleteListener;

    public UserAdapter(List<UserResponseDTO> list, OnItemDeleteListener deleteListener) {
        this.list = list;
        this.deleteListener = deleteListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public TextView email;

        public ImageButton btnDelete;

        public ImageButton btnUpdate;

        public ViewHolder(View view) {
            super(view);

            name = itemView.findViewById(R.id.nameUser);
            email = itemView.findViewById(R.id.emailUser);
            btnUpdate = itemView.findViewById(R.id.btnUpdateUser);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserResponseDTO item = list.get(position);

        holder.name.setText(item.getName().toUpperCase());
        holder.email.setText(item.getEmail().toLowerCase());
        holder.btnUpdate.setId(item.getId().intValue());
        holder.btnDelete.setId(item.getId().intValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
