package com.example.photoprisim;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.webkit.ClientCertRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import android.webkit.SslErrorHandler;
import android.net.http.SslError;

public class MainActivity extends AppCompatActivity {
    private WebView mywebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mywebView=(WebView) findViewById(R.id.webview);
        mywebView.setWebViewClient(new mywebClient());
        mywebView.loadUrl("https://your-server-here.com");
        WebSettings webSettings=mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
    }

    public class mywebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, final ClientCertRequest request) {
            try {
                InputStream certificateFileStream = getResources().openRawResource(R.raw.bundle);
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                String password = "changeme";
                keyStore.load(certificateFileStream, password.toCharArray());
                Enumeration<String> aliases = keyStore.aliases();
                String alias = aliases.nextElement();
                Key key = keyStore.getKey(alias, password.toCharArray());
                PrivateKey mPrivateKey = (PrivateKey)key;
                Certificate cert = keyStore.getCertificate(alias);
                X509Certificate[] mCertificates = new X509Certificate[1];
                mCertificates[0] = (X509Certificate)cert;
                certificateFileStream.close();
                request.proceed(mPrivateKey, mCertificates);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
