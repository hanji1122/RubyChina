package com.luckyhan.rubychina.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.ui.base.BaseActivity;
import com.luckyhan.rubychina.utils.ToastUtils;

/**
 * Author HanJi
 * Email hanji1122@gmail.com
 * Date 2016-06-04
 */
public class PermissionCheckBaseActivity extends BaseActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0x00;

    private AlertDialog mRequestPermissionDialog;
    private boolean mCheckFromSettingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCheckFromSettingBack) {
            checkPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showShort(getContext(), "授权成功");
                } else {
                    ToastUtils.showShort(getContext(), "拒绝授权");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.dialog_title_tip);
                        builder.setMessage(R.string.dialog_msg_permission_request_fail);
                        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    ActivityCompat.requestPermissions(
                                            PermissionCheckBaseActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            REQUEST_WRITE_EXTERNAL_STORAGE);
                                }
                            }
                        });
                        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.dialog_title_tip);
                        builder.setMessage(R.string.dialog_msg_permission_go_setting);
                        builder.setPositiveButton(R.string.dialog_go_setting, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCheckFromSettingBack = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();

                    }
                }
                break;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    private void showRequestPermissionDialog() {
        if (mRequestPermissionDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.dialog_title_tip);
            builder.setMessage(R.string.dialog_msg_permission_request);
            builder.setPositiveButton(R.string.next_step, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(
                                PermissionCheckBaseActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                }
            });
            builder.setCancelable(false);
            mRequestPermissionDialog = builder.show();
        } else {
            mRequestPermissionDialog.show();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        mCheckFromSettingBack = false;
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showRequestPermissionDialog();
            } else {
                showRequestPermissionDialog();
            }
        }
    }

}
