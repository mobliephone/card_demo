package com.st.nfc;

import android.app.Activity;
import android.content.Intent;

import com.st.CMEApplication;
import com.st.activity.base.ABaseActivity;
import com.st.nfc.activity.TagInfoToolActivity;

/**
 * An Activity implementing the NFC foreground dispatch system overwriting
 * onResume() and onPause(). New Intents will be treated as new Tags.
 * @see CMEApplication#enableNfcForegroundDispatch(Activity)
 * @see CMEApplication#disableNfcForegroundDispatch(Activity)
 * @see CMEApplication#treatAsNewTag(Intent, android.content.Context)
 * @author Gerhard Klostermeier
 *
 */
public abstract class BasicActivity extends ABaseActivity {

    private int typeCheck;

    /**
     * Enable NFC foreground dispatch system.
     * @see CMEApplication#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onResume() {
        super.onResume();
        CMEApplication.enableNfcForegroundDispatch(this);
    }

    /**
     * Disable NFC foreground dispatch system.
     * @see CMEApplication#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onPause() {
        super.onPause();
        CMEApplication.disableNfcForegroundDispatch(this);
    }

    /**
     * Handle new Intent as a new tag Intent and if the tag/device does not
     * support MIFARE Classic, then run {@link TagInfoToolActivity}.
     * @see CMEApplication#treatAsNewTag(Intent, android.content.Context)
     * @see TagInfoToolActivity
     */
    @Override
    public void onNewIntent(Intent intent) {
        typeCheck = CMEApplication.treatAsNewTag(intent, this);
        if (typeCheck == -1 || typeCheck == -2) {
            // Device or tag does not support MIFARE Classic.
            // Run the only thing that is possible: The tag info tool.
            Intent i = new Intent(this, TagInfoToolActivity.class);
            startActivity(i);
        }
    }

    public int getTypeCheck() {
        return typeCheck;
    }
}
