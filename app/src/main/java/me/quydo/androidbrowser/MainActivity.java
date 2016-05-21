package me.quydo.androidbrowser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	private WebView webView;
	private ProgressBar progressBar;
	private EditText txtUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		progressBar.setMax(100);

		webView = (WebView) findViewById(R.id.webpage);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setSavePassword(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				progressBar.setVisibility(View.VISIBLE);
				progressBar.setProgress(0);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.GONE);
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressBar.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}
		});

		txtUrl = (EditText) findViewById(R.id.txt_url);
		txtUrl.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					webView.loadUrl(txtUrl.getText().toString());
					hideKeyboard();
					return true;
				}
				return false;
			}
		});
	}

	public void back(View v) {
		if (webView.canGoBack())
			webView.goBack();
	}

	public void forward(View v) {
		if (webView.canGoForward())
			webView.goForward();
	}

	public void refresh(View v) {
		webView.reload();
	}

	private void hideKeyboard() {
		InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

}
