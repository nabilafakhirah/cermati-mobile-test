package com.example.cermati;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cermati.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText searchBar;
    private RecyclerView rvSearch;

    private int pageNo;
    private InterfaceApi interfaceApi;
    private SearchAdapter searchAdapter;
    private LinearLayoutManager layoutManager;

    private boolean loading = true;
    private int pastVisUsers, visUsersCount, totalUsersCount, prevTotal = 0;
    private int view_threshold = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        interfaceApi = ApiClient.getRetrofitInstance().create(InterfaceApi.class);
        searchBarAction();
    }

    private void searchBarAction() {
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String query = searchBar.getText().toString();
                    performSearch(query);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void performSearch(final String query) {
        if (!query.isEmpty()) {
            pageNo = 1;
            rvSearch = findViewById(R.id.rvSearch);
            layoutManager = new LinearLayoutManager(this);
            rvSearch.setLayoutManager(layoutManager);

            Call<ApiResponse> call = interfaceApi.getGithubUsers(query, pageNo);

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if(response.body()!=null) {
                        List<GithubUsers> githubUsers = response.body().getGithubUsers();
                        if (!githubUsers.isEmpty()) {
                            searchAdapter = new SearchAdapter(MainActivity.this, githubUsers);
                            rvSearch.setAdapter(searchAdapter);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "No matching results", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Only showing first 1000 results", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    System.out.println(t);
                }
            });
        }

        rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visUsersCount = layoutManager.getChildCount();
                totalUsersCount = layoutManager.getItemCount();
                pastVisUsers = layoutManager.findFirstVisibleItemPosition();

                if(dy>0) {
                    if(loading) {
                        if(totalUsersCount>prevTotal) {
                            loading = false;
                            prevTotal = totalUsersCount;
                        }
                    } if(!loading&&totalUsersCount-visUsersCount<=pastVisUsers+view_threshold) {
                        pageNo++;
                        searchPagination(query);
                        loading = true;
                    }
                }
            }
        });
    }

    private void searchPagination(String query) {
        Call<ApiResponse> call = interfaceApi.getGithubUsers(query, pageNo);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.body()!=null) {
                    List<GithubUsers> githubUsers = response.body().getGithubUsers();
                    if (!githubUsers.isEmpty()) {
                        searchAdapter.addUsers(githubUsers);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "End of results", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Only showing first 1000 results", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}
