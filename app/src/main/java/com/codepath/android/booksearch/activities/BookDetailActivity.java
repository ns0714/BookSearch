package com.codepath.android.booksearch.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.models.Book;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublishDate;
    private Toolbar toolbar;
    private Intent shareIntent;
    private ShareActionProvider miShareAction;
    private MenuItem shareItem;

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


        Picasso.with(this).load(Uri.parse(book.getCoverUrl())).placeholder(R.drawable.ic_nocover).into(ivBookCover, new Callback() {
            @Override
            public void onSuccess() {
                BookDetailActivityPermissionsDispatcher.prepareShareIntentWithCheck(BookDetailActivity.this);
                attachShareIntentAction();
            }

            @Override
            public void onError() {

            }
        });
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void prepareShareIntent() {
        // Fetch Bitmap Uri locally
        ImageView ivImage = (ImageView) findViewById(R.id.ivBookCover);
        Drawable mDrawable = ivImage.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, "Image Description", null);

        Uri uri = Uri.parse(path);
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
    }

    // Attaches the share intent to the share menu item provider
    public void attachShareIntentAction() {
        if (miShareAction != null && shareIntent != null)
            miShareAction.setShareIntent(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_share, menu);
        shareItem = menu.findItem(R.id.menu_item_share);
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        attachShareIntentAction();

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        BookDetailActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
