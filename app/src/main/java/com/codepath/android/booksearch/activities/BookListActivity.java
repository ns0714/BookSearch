package com.codepath.android.booksearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.adapters.BookAdapter;
import com.codepath.android.booksearch.models.Book;
import com.codepath.android.booksearch.net.BookClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class BookListActivity extends AppCompatActivity {
    private ListView lvBooks;
    private Toolbar toolbar;
    private BookAdapter bookAdapter;
    private BookClient client;
    private MenuItem actionProgress;
    private ProgressBar progressBar;
    private ShareActionProvider miShareAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lvBooks = (ListView) findViewById(R.id.lvBooks);
        ArrayList<Book> aBooks = new ArrayList<>();
        View footer = getLayoutInflater().inflate(
                R.layout.footer_progress, null);
        // initialize the adapter
        bookAdapter = new BookAdapter(this, aBooks);
        // attach the adapter to the ListView
        progressBar = (ProgressBar)
                footer.findViewById(R.id.pbFooterLoading);
        // Add footer to ListView before setting adapter
        lvBooks.addFooterView(footer);

        lvBooks.setAdapter(bookAdapter);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(BookListActivity.this, BookDetailActivity.class);
                intent.putExtra("book", bookAdapter.getItem(position));
                startActivity(intent);
            }
        });
        // Fetch the data remotely
        //fetchBooks("Oscar Wilde");
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks(String query) {
        client = new BookClient();
        client.getBooks(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs;
                    if(response != null) {
                        // Get the docs json array
                        docs = response.getJSONArray("docs");
                        // Parse json array into array of model objects
                        final ArrayList<Book> books = Book.fromJson(docs);
                        // Remove all books from the adapter
                        bookAdapter.clear();
                        // Load model objects into the adapter
                        for (Book book : books) {
                            bookAdapter.add(book); // add book through the adapter
                        }
                        bookAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //ProgressBar bar = (ProgressBar) MenuItemCompat.getActionView(actionProgress);

        getMenuInflater().inflate(R.menu.menu_progress_bar, menu);
        actionProgress = menu.findItem(R.id.miActionProgress);
        progressBar = (ProgressBar) MenuItemCompat.getActionView(actionProgress);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_share, menu);
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//        // Fetch reference to the share action provider
//        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = (MenuItem) findViewById(R.id.action_search);
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                String keyword = searchView.getQuery().toString();
                showProgressBar();
                fetchBooks(keyword);
                hideProgressBar();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showProgressBar() {
        // Show progress item
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        // Hide progress item
        progressBar.setVisibility(View.GONE);
    }
}
