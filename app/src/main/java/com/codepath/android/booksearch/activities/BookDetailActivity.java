package com.codepath.android.booksearch.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.models.Book;
import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublishDate;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPublishDate = (TextView) findViewById(R.id.tvPublishDate);

        // Extract book object from intent extras
        Book book = (Book) getIntent().getParcelableExtra("book");

        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        tvPublishDate.setText(book.getPublishDate());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(book.getTitle());


        Picasso.with(this).load(Uri.parse(book.getCoverUrl())).placeholder(R.drawable.ic_nocover).into(ivBookCover);
        // Use book object to populate data into views
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
//        return true;
//    }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        MenuItem searchItem = (MenuItem) findViewById(R.id.action_search);
//        //final SearchView searchView = (SearchView) searchItem.getActionView();
//        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
//
//        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchView.clearFocus();
//                String keyword = searchView.getQuery().toString();
//                //fetchBooks(keyword);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
}
