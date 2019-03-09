
package com.st.nfc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.st.CMEApplication;
import com.st.R;
import com.st.nfc.BasicActivity;


/**
 * Display tag info like technology, size, sector count, etc.
 * This is the only thing a user can do with a device that does not support
 * MIFARE Classic.
 * @author Gerhard Klostermeier
 *
 * 显示标签信息，如技术、尺寸、扇区计数等。
 * 这是用户唯一能用不支持的设备做的事情。
 */
public class TagInfoToolActivity extends BasicActivity {

    LinearLayout mLayout;
    TextView mErrorMessage;
    int mMFCSupport;

    /**
     * Calls {@link #updateTagInfo(Tag)} (and initialize some member
     * variables).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nfc_tag_info_tool);

        mLayout = (LinearLayout) findViewById(R.id.linearLayoutTagInfoTool);
        mErrorMessage = (TextView) findViewById(
                R.id.textTagInfoToolErrorMessage);
        updateTagInfo(CMEApplication.getTag());

        setTitle("NFC加载界面");
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_nfc_tag_info_tool;
    }

    @Override
    public void onInitView() {

    }

    /**
     * Calls {@link CMEApplication#treatAsNewTag(Intent, android.content.Context)} and
     * then calls {@link #updateTagInfo(Tag)}
     */
    @Override
    public void onNewIntent(Intent intent) {
        CMEApplication.treatAsNewTag(intent, this);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            updateTagInfo(CMEApplication.getTag());
        }
    }

    /**
     * Show a dialog with further information.
     * @param view The View object that triggered the method
     * (in this case the read more button).
     */
    public void onReadMore(View view) {
        int titleID = 0;
        int messageID = 0;
        if (mMFCSupport == -1) {
            // Device does not support MIFARE Classic.
            titleID = R.string.dialog_no_mfc_support_device_title;
            messageID = R.string.dialog_no_mfc_support_device;
        } else if (mMFCSupport == -2) {
            // Tag does not support MIFARE Classic.
            titleID = R.string.dialog_no_mfc_support_tag_title;
            messageID = R.string.dialog_no_mfc_support_tag;
        }
        CharSequence styledText = Html.fromHtml(
                getString(messageID));
        AlertDialog ad = new AlertDialog.Builder(this)
        .setTitle(titleID)
        .setMessage(styledText)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton(R.string.action_ok,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing.
            }
         })
         .show();
        // Make links clickable.
        ((TextView)ad.findViewById(android.R.id.message)).setMovementMethod(
                LinkMovementMethod.getInstance());
    }

    /**
     * Update and display the tag information.
     * If there is no MIFARE Classic support, a warning will be shown.
     * @param tag A Tag from an NFC Intent.
     */
    private void updateTagInfo(Tag tag) {

        if (tag != null) {
            // Check for MIFARE Classic support.
            mMFCSupport = CMEApplication.checkMifareClassicSupport(tag, this);

            mLayout.removeAllViews();
            // Display generic info.
            // Create views and add them to the layout.
            TextView headerGenericInfo = new TextView(this);
            headerGenericInfo.setText(CMEApplication.colorString(
                    getString(R.string.text_generic_info),
                    getResources().getColor(R.color.blue)));
            headerGenericInfo.setBackgroundColor(
                    getResources().getColor(R.color.dark_gray));
            headerGenericInfo.setTextAppearance(this,
                    android.R.style.TextAppearance_Large);
            headerGenericInfo.setGravity(Gravity.CENTER_HORIZONTAL);
            int pad = CMEApplication.dpToPx(5); // 5dp to px.
            headerGenericInfo.setPadding(pad, pad, pad, pad);
            mLayout.addView(headerGenericInfo);
            TextView genericInfo = new TextView(this);
            genericInfo.setPadding(pad, pad, pad, pad);
            genericInfo.setTextAppearance(this,
                    android.R.style.TextAppearance_Medium);
            mLayout.addView(genericInfo);
            // Get generic info and set these as text.
            String uid = CMEApplication.byte2HexString(tag.getId());
            int uidLen = tag.getId().length;
            uid += " (" + uidLen + " byte";
            if (uidLen == 7) {
                uid += ", CL2";
            } else if (uidLen == 10) {
                uid += ", CL3";
            }
            uid += ")";
            NfcA nfca = NfcA.get(tag);
            // Swap ATQA to match the CMEApplication order like shown here:
            // http://nfc-tools.org/index.php?title=ISO14443A
            byte[] atqaBytes = nfca.getAtqa();
            atqaBytes = new byte[] {atqaBytes[1], atqaBytes[0]};
            String atqa = CMEApplication.byte2HexString(atqaBytes);
            // SAK in big endian.
            byte[] sakBytes = new byte[] {
                    (byte)((nfca.getSak() >> 8) & 0xFF),
                    (byte)(nfca.getSak() & 0xFF)};
            String sak;
            // Print the first SAK byte only if it is not 0.
            if (sakBytes[0] != 0) {
                sak = CMEApplication.byte2HexString(sakBytes);
            } else {
                sak = CMEApplication.byte2HexString(new byte[] {sakBytes[1]});
            }
            String ats = "-";
            IsoDep iso = IsoDep.get(tag);
            if (iso != null ) {
                byte[] atsBytes = iso.getHistoricalBytes();
                if (atsBytes != null && atsBytes.length > 0) {
                    ats = CMEApplication.byte2HexString(atsBytes);
                }
            }
            // Identify tag type.
            int tagTypeResourceID = getTagIdentifier(atqa, sak, ats);
            String tagType;
            if (tagTypeResourceID == R.string.tag_unknown && mMFCSupport > -2) {
                tagType = getString(R.string.tag_unknown_mf_classic);
            } else {
                tagType = getString(tagTypeResourceID);
            }

            int hc = getResources().getColor(R.color.light_green);
            genericInfo.setText(TextUtils.concat(
                    CMEApplication.colorString(getString(R.string.text_uid) + ":", hc),
                    "\n", uid, "\n",
                    CMEApplication.colorString(getString(
                            R.string.text_rf_tech) + ":", hc),
                    // Tech is always ISO 14443a due to NFC Intent filter.
                    "\n", getString(R.string.text_rf_tech_14a), "\n",
                    CMEApplication.colorString(getString(R.string.text_atqa) + ":", hc),
                    "\n", atqa, "\n",
                    CMEApplication.colorString(getString(R.string.text_sak) + ":", hc),
                    "\n", sak, "\n",
                    CMEApplication.colorString(getString(
                            R.string.text_ats) + ":", hc),
                    "\n", ats, "\n",
                    CMEApplication.colorString(getString(
                            R.string.text_tag_type_and_manuf) + ":", hc),
                    "\n", tagType));

            // Add message that the tag type might be wrong.
            if (tagTypeResourceID != R.string.tag_unknown) {
                TextView tagTypeInfo = new TextView(this);
                tagTypeInfo.setPadding(pad, 0, pad, pad);
                tagTypeInfo.setText(
                        "(" + getString(R.string.text_tag_type_guess) + ")");
                mLayout.addView(tagTypeInfo);
            }

            LinearLayout layout = (LinearLayout) findViewById(
                    R.id.linearLayoutTagInfoToolSupport);
            // Check for MIFARE Classic support.
            if (mMFCSupport == 0) {
                // Display MIFARE Classic info.
                // Create views and add them to the layout.
                TextView headerMifareInfo = new TextView(this);
                headerMifareInfo.setText(CMEApplication.colorString(
                        getString(R.string.text_mf_info),
                        getResources().getColor(R.color.blue)));
                headerMifareInfo.setBackgroundColor(
                        getResources().getColor(R.color.dark_gray));
                headerMifareInfo.setTextAppearance(
                        this, android.R.style.TextAppearance_Large);
                headerMifareInfo.setGravity(Gravity.CENTER_HORIZONTAL);
                headerMifareInfo.setPadding(pad, pad, pad, pad);
                mLayout.addView(headerMifareInfo);
                TextView mifareInfo = new TextView(this);
                mifareInfo.setPadding(pad, pad, pad, pad);
                mifareInfo.setTextAppearance(this,
                        android.R.style.TextAppearance_Medium);
                mLayout.addView(mifareInfo);

                // Get MIFARE info and set these as text.
                MifareClassic mfc = MifareClassic.get(tag);
                String size = "" + mfc.getSize();
                String sectorCount = "" + mfc.getSectorCount();
                String blockCount = "" + mfc.getBlockCount();
                mifareInfo.setText(TextUtils.concat(
                        CMEApplication.colorString(getString(
                                R.string.text_mem_size) + ":", hc),
                        "\n", size, " byte\n",
                        CMEApplication.colorString(getString(
                                R.string.text_block_size) + ":", hc),
                        // Block size is always 16 byte on MIFARE Classic Tags.
                        "\n", "" + MifareClassic.BLOCK_SIZE, " byte\n",
                        CMEApplication.colorString(getString(
                                R.string.text_sector_count) + ":", hc),
                        "\n", sectorCount, "\n",
                        CMEApplication.colorString(getString(
                                R.string.text_block_count) + ":", hc),
                        "\n", blockCount));
                layout.setVisibility(View.GONE);
            } else if (mMFCSupport == -1) {
                // No MIFARE Classic Support (due to the device hardware).
                // Set error message.
                mErrorMessage.setText(R.string.text_no_mfc_support_device);
                layout.setVisibility(View.VISIBLE);
            } else if (mMFCSupport == -2) {
                // The tag does not support MIFARE Classic.
                // Set error message.
                mErrorMessage.setText(R.string.text_no_mfc_support_tag);
                layout.setVisibility(View.VISIBLE);
            }
        } else {
            // There is no Tag.
            TextView text = new TextView(this);
            int pad = CMEApplication.dpToPx(5);
            text.setPadding(pad, pad, 0, 0);
            text.setTextAppearance(this, android.R.style.TextAppearance_Large);
            text.setText(getString(R.string.text_no_tag));
            mLayout.removeAllViews();
            mLayout.addView(text);
            Toast.makeText(this, R.string.info_no_tag_found,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get (determine) the tag type resource ID from ATQA + SAK + ATS.
     * If no resource is found check for the tag type only on ATQA + SAK
     * (and then on ATQA only).
     * @param atqa The ATQA from the tag.
     * @param sak The SAK from the tag.
     * @param ats The ATS from the tag.
     * @return The resource ID.
     */
    private int getTagIdentifier(String atqa, String sak, String ats) {
        String prefix = "tag_";
        ats = ats.replace("-", "");

        // First check on ATQA + SAK + ATS.
        int ret = getResources().getIdentifier(
                prefix + atqa + sak + ats, "string", getPackageName());

        if (ret == 0) {
            // Check on ATQA + SAK.
            ret = getResources().getIdentifier(
                    prefix + atqa + sak, "string", getPackageName());
        }

        if (ret == 0) {
            // Check on ATQA.
            ret = getResources().getIdentifier(
                    prefix + atqa, "string", getPackageName());
        }

        if (ret == 0) {
            // No match found return "Unknown".
            return R.string.tag_unknown;
        }
        return ret;
    }
}