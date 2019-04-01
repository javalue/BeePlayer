package com.bee.player.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bee.player.R;
import com.bee.player.permission.PermissionDialogConfig;
import com.bee.player.permission.XYPermissionConstant;
import com.bee.player.permission.XYPermissionProxyFragment;
import com.bee.player.play.MediaItem;
import com.bee.player.play.PlayActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView mRv;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = findViewById(R.id.rv_list);
        progressBar = findViewById(R.id.pb_home);
        floatingActionButton = findViewById(R.id.fab_refresh);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requiresPermission();
            }
        });
        requiresPermission();
    }

    private void requiresPermission() {
        if (EasyPermissions.hasPermissions(this, XYPermissionConstant
                .EXTERNAL_STRORAGE)) {
            loadVideos();
        } else {
            PermissionDialogConfig config = new PermissionDialogConfig(XYPermissionConstant
                    .EXTERNAL_STRORAGE, XYPermissionConstant.RC_EXTERNAL_STRORAGE_PERM, "", 0);
            XYPermissionProxyFragment fragment = XYPermissionProxyFragment.newInstance
                    (config, new XYPermissionProxyFragment.OnRationalListener() {

                        @Override
                        public void onPermissionsGranted(int requestCode, @NonNull List<String>
                                perms) {
                            loadVideos();
                        }

                        @Override
                        public void onPermissionsDenied(int requestCode, @NonNull List<String>
                                perms) {
                            Toast.makeText(getApplicationContext(), R.string.str_refuse, Toast
                                    .LENGTH_LONG).show();
                        }
                    });
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment)
                    .commitNowAllowingStateLoss();
        }
    }

    private void loadVideos() {
        progressBar.setVisibility(View.VISIBLE);

        Observable.create(new ObservableOnSubscribe<ArrayList<MediaItem>>() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                long start = System.currentTimeMillis();
                ArrayList<MediaItem> mediaList = getLoadMedia();
                long end = System.currentTimeMillis();
                if (end - start < 1000) {
                    Thread.sleep(500);
                }
                emitter.onNext(mediaList);
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<MediaItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final ArrayList<MediaItem> mediaItems) {

                        HomeVideoAdapter adapter = new HomeVideoAdapter(mediaItems);
                        mRv.setLayoutManager(new LinearLayoutManager(HomeActivity.this,
                                LinearLayoutManager.VERTICAL, false));
                        mRv.setAdapter(adapter);
                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view,
                                                    int position) {
                                Intent intent = new Intent(HomeActivity.this, PlayActivity.class);
                                intent.putExtra("media", mediaItems.get(position));
                                startActivity(intent);
                            }
                        });

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
        });
    }

    public ArrayList<MediaItem> getLoadMedia() {
        ArrayList<MediaItem> mediaList = new ArrayList();
        Cursor cursor =
                getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.VideoColumns.DATE_ADDED + " DESC");
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)); // id
                String displayName =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String album =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)); // 专辑
                String artist =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)); // 艺术家
                String title =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 显示名称
                String mimeType =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                String path =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                long duration =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长
                long size =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小
                String resolution =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                long addData =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));

                MediaItem item = new MediaItem();
                item.id = id;
                item.displayName = displayName;
                item.album = album;
                item.artist = artist;
                item.title = title;
                item.mimeType = mimeType;
                item.path = path;
                item.duration = duration;
                item.size = size;
                item.resolution = resolution;
                mediaList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            return mediaList;
        }
    }
}
