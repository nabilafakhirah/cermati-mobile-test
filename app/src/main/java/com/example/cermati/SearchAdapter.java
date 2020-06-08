package com.example.cermati;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cermati.GithubUsers;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private List<GithubUsers> usersList;

    public SearchAdapter(Context ct, List<GithubUsers> githubUsersList) {
        context = ct;
        usersList = githubUsersList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_search, viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        searchViewHolder.tvName.setText(usersList.get(i).getLoginId());
        Glide.with(context).load(usersList.get(i).getAvatar()).into(searchViewHolder.ivUser);
    }
    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivUser;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivUser = itemView.findViewById(R.id.ivUser);
        }
    }

    public void addUsers(List<GithubUsers> users) {

        for(GithubUsers user : users) {
            usersList.add(user);
        }

        notifyDataSetChanged();
    }
}
