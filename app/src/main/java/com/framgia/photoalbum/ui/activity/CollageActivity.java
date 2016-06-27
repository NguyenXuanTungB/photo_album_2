package com.framgia.photoalbum.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.framgia.photoalbum.R;
import com.framgia.photoalbum.data.model.FeatureItem;
import com.framgia.photoalbum.ui.adapter.ListFeatureAdapter;
import com.framgia.photoalbum.ui.custom.PartImageView;
import com.framgia.photoalbum.util.CommonUtils;
import com.framgia.photoalbum.util.FileUtils;
import com.framgia.photoalbum.util.PermissionUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.framgia.photoalbum.util.CollageUtils.PickImageToMergeListener;
import static com.framgia.photoalbum.util.CollageUtils.createLayout_2_1;
import static com.framgia.photoalbum.util.CollageUtils.createLayout_2_2;
import static com.framgia.photoalbum.util.CollageUtils.createLayout_2_3;
import static com.framgia.photoalbum.util.CollageUtils.createLayout_3_1;
import static com.framgia.photoalbum.util.CollageUtils.createLayout_3_2;
import static com.framgia.photoalbum.util.CollageUtils.createLayout_4_1;
import static com.framgia.photoalbum.util.CollageUtils.isAllChose;
import static com.framgia.photoalbum.util.CollageUtils.releaseMemory;

public class CollageActivity extends AppCompatActivity implements PickImageToMergeListener {
    private static final String TAG = "CollageActivity";
    public static final int REQUEST_CODE_CHOOSE_IMAGE = 0;
    public static final String KEY_COLLAGE = "Collage";
    public static final int LAYOUT_2_1 = 0;
    public static final int LAYOUT_2_2 = 1;
    public static final int LAYOUT_2_3 = 2;
    public static final int LAYOUT_3_1 = 3;
    public static final int LAYOUT_3_2 = 4;
    public static final int LAYOUT_4_1 = 5;
    private ArrayList<FeatureItem> layoutItems = new ArrayList<>();
    private ListFeatureAdapter mAdapter;
    private PartImageView[] mPartImageViews;
    private int mPosition;
    public Bitmap[] mImageBitmaps = new Bitmap[4];
    public int numberView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.rootPartImageView)
    RelativeLayout rootView;

    @Bind(R.id.listLayoutCollage)
    RecyclerView listLayoutCollage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);
        ButterKnife.bind(this);
        initView();
        bindWidgetControl();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE_IMAGE) {
            if (resultCode == RESULT_OK) {
                String imagePath = data.getStringExtra(ChooseImageActivity.IMAGE_PATH);
                Bitmap bitmap = CommonUtils.decodeSampledBitmapResource(
                        imagePath,
                        mPartImageViews[mPosition].getWidth(),
                        mPartImageViews[mPosition].getHeight()
                );
                mImageBitmaps[mPosition] = bitmap;
                mPartImageViews[mPosition].setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collage_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveImage) {
            if (!isAllChose(mImageBitmaps, numberView)) {
                showSaveConfirmDialog();
            } else {
                Bitmap saveBitmap = CommonUtils.getBitmapFromView(rootView);
                FileUtils.saveEditedImage(getBaseContext(), saveBitmap);
            }
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void initData() {
        layoutItems.add(new FeatureItem(R.drawable.ic_collage_1, getString(R.string.collage_1)));
        layoutItems.add(new FeatureItem(R.drawable.ic_collage_2, getString(R.string.collage_2)));
        layoutItems.add(new FeatureItem(R.drawable.ic_collage_3, getString(R.string.collage_3)));
        layoutItems.add(new FeatureItem(R.drawable.ic_collage_4, getString(R.string.collage_4)));
        layoutItems.add(new FeatureItem(R.drawable.ic_collage_5, getString(R.string.collage_5)));
        layoutItems.add(new FeatureItem(R.drawable.ic_collage_6, getString(R.string.collage_6)));
    }

    private void initView() {
        initData();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listLayoutCollage.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ListFeatureAdapter(
                this,
                layoutItems,
                new ListFeatureAdapter.OnFeatureClicked() {
                    @Override
                    public void onClick(View v, int position) {
                        initLayout(position);
                    }
                });
        listLayoutCollage.setAdapter(mAdapter);

        initLayout(LAYOUT_2_1);
    }

    private void initLayout(int type) {
        rootView.removeAllViews();
        switch (type) {
            case LAYOUT_2_1:
                numberView = 2;
                mPartImageViews = new PartImageView[numberView];
                createLayout_2_1(rootView, this, mPartImageViews);
                break;
            case LAYOUT_2_2:
                numberView = 2;
                mPartImageViews = new PartImageView[numberView];
                createLayout_2_2(rootView, this, mPartImageViews);
                break;
            case LAYOUT_2_3:
                numberView = 2;
                mPartImageViews = new PartImageView[numberView];
                createLayout_2_3(rootView, this, mPartImageViews);
                break;
            case LAYOUT_3_1:
                numberView = 3;
                mPartImageViews = new PartImageView[numberView];
                createLayout_3_1(rootView, this, mPartImageViews);
                break;
            case LAYOUT_3_2:
                numberView = 3;
                mPartImageViews = new PartImageView[numberView];
                createLayout_3_2(rootView, this, mPartImageViews);
                break;
            case LAYOUT_4_1:
                numberView = 4;
                mPartImageViews = new PartImageView[numberView];
                createLayout_4_1(rootView, this, mPartImageViews);
                break;
        }
    }

    private void bindWidgetControl() {

    }

    @Override
    public void onPick(int position) {
        mPosition = position;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestWriteStoragePermission(this, R.id.rootView);
        } else {
            startChooseImageActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMemory(mImageBitmaps);
    }

    private void startChooseImageActivity() {
        Intent intent = new Intent(this, ChooseImageActivity.class);
        intent.putExtra(KEY_COLLAGE, true);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startChooseImageActivity();
            } else {
                Toast.makeText(this, getString(R.string.write_permission_not_granted), Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public Dialog showSaveConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_image_not_chosen);
        builder.setMessage(R.string.msg_save_confirm);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bitmap saveBitmap = CommonUtils.getBitmapFromView(rootView);
                FileUtils.saveEditedImage(getBaseContext(), saveBitmap);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setCancelable(false);
        builder.show();
        return builder.create();
    }
}
