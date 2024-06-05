package br.com.cairu.projeto.integrador.brecho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.cairu.projeto.integrador.brecho.R;
import br.com.cairu.projeto.integrador.brecho.dtos.home.HomeResponseDTO;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeResponseDTO> list;

    public HomeAdapter(List<HomeResponseDTO> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameProduct;

        public TextView countClick;

        public ImageView images;

        public ViewHolder(View view) {
            super(view);
            nameProduct = itemView.findViewById(R.id.nameProductHome);
            countClick = itemView.findViewById(R.id.countClickView);
            images = itemView.findViewById(R.id.imageProductHome);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_recyclerview_home, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeResponseDTO item = list.get(position);

        System.out.println(holder.images);
        holder.nameProduct.setText(item.getName().toUpperCase());
        holder.countClick.setText(String.valueOf(item.getCountClick() + " clicks"));
        Picasso.get().load("http://10.0.2.2:8080/" + item.getFile().getUrl()).into(holder.images);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
