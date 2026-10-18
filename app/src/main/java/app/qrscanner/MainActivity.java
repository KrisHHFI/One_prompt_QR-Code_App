package app.qrscanner;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends ComponentActivity {

    private static final String KEY_RESULT = "last_result";

    private TextView resultView;
    private Button openButton;
    private Button copyButton;
    private String lastResult;

    private final ActivityResultLauncher<ScanOptions> scanner =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    showResult(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = findViewById(R.id.result);
        openButton = findViewById(R.id.btn_open);
        copyButton = findViewById(R.id.btn_copy);
        Button scanButton = findViewById(R.id.btn_scan);

        scanButton.setOnClickListener(v -> startScan());
        copyButton.setOnClickListener(v -> copyResult());
        openButton.setOnClickListener(v -> openResult());

        if (savedInstanceState != null && savedInstanceState.getString(KEY_RESULT) != null) {
            showResult(savedInstanceState.getString(KEY_RESULT));
        } else {
            updateButtons();
            if (savedInstanceState == null) {
                startScan(); // go straight to the camera when the app opens
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RESULT, lastResult);
    }

    private void startScan() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Point the camera at a QR code");
        options.setBeepEnabled(false);
        options.setOrientationLocked(false);
        scanner.launch(options);
    }

    private void showResult(String text) {
        lastResult = text;
        resultView.setText(text);
        updateButtons();
    }

    private void updateButtons() {
        copyButton.setVisibility(lastResult != null ? View.VISIBLE : View.GONE);
        openButton.setVisibility(toOpenableUri(lastResult) != null ? View.VISIBLE : View.GONE);
    }

    private void copyResult() {
        if (lastResult == null) return;
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("QR code", lastResult));
        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
    }

    private void openResult() {
        Uri uri = toOpenableUri(lastResult);
        if (uri == null) return;
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No app can open this", Toast.LENGTH_SHORT).show();
        }
    }

    /** Returns a Uri for links, phone numbers, emails, maps etc.; null for plain text. */
    private static Uri toOpenableUri(String text) {
        if (text == null) return null;
        String t = text.trim();
        if (t.isEmpty() || t.contains(" ") || t.contains("\n")) return null;
        String lower = t.toLowerCase();
        if (lower.startsWith("www.")) return Uri.parse("https://" + t);
        String[] schemes = {"http:", "https:", "mailto:", "tel:", "sms:", "smsto:", "geo:", "market:"};
        for (String s : schemes) {
            if (lower.startsWith(s)) return Uri.parse(t);
        }
        return null;
    }
}
