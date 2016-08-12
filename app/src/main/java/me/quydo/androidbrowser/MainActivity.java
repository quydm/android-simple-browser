package me.quydo.androidbrowser;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import me.quydo.androidbrowser.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		setupWebView();
		setupTxtUrl();
	}

	public void back(View v) {
		if (binding.webView.canGoBack())
			binding.webView.goBack();
	}

	public void forward(View v) {
		if (binding.webView.canGoForward())
			binding.webView.goForward();
	}

	public void refresh(View v) {
		binding.webView.reload();
	}

	private void hideKeyboard() {
		InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	private String buildUrl(String url) {
		if (url.startsWith("http://") || url.startsWith("https://"))
			return url;
		return "http://".concat(url);
	}

	private void setupWebView() {
		binding.webView.getSettings().setSaveFormData(false);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
			binding.webView.getSettings().setSavePassword(false);
		binding.webView.getSettings().setJavaScriptEnabled(true);
		binding.webView.setVerticalScrollBarEnabled(false);
		binding.webView.setHorizontalScrollBarEnabled(false);
		binding.webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				binding.progressBar.setVisibility(View.VISIBLE);
				binding.progressBar.setProgress(0);
				binding.txtUrl.setText(url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				binding.progressBar.setVisibility(View.GONE);
			}
		});
		binding.webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				binding.progressBar.setProgress(newProgress);
			}
		});
	}

	private void setupTxtUrl() {
		binding.txtUrl.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					binding.webView.loadUrl(buildUrl(binding.txtUrl.getText().toString()));
					hideKeyboard();
					return true;
				}
				return false;
			}
		});
	}

}
