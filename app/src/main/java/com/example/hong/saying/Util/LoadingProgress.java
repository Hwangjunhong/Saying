package com.example.hong.saying.Util;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.hong.saying.R;

public class LoadingProgress {
	private static Dialog mDialog;
	public static ProgressDialog mProgressDialog;


	public static void showDialog(Context context) {
		mDialog = new Dialog(context, R.style.LoadingDialog);
		mDialog.addContentView(
				new ProgressBar(context),
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
		);

		mDialog.setCancelable(false);
		mDialog.show();
	}
	public static void showDialog(Context context, Boolean touch) {
		mDialog = new Dialog(context, R.style.LoadingDialog);
		mDialog.addContentView(
				new ProgressBar(context),
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
		);

		mDialog.setCancelable(touch);
		mDialog.show();
	}

	public static void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	public static void initProgressDialog(Context context) {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(context.getString(R.string.loading));
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.show();
	}

	public static void setProgress(int i) {
		ObjectAnimator animation = ObjectAnimator.ofInt(mProgressDialog, "progress",i);
		animation.setDuration(500); // 0.5 second
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.start();
		}

	public static void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
}
