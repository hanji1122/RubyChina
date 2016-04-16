package com.luckyhan.rubychina.ui.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.widget.EditText;

import com.luckyhan.rubychina.utils.BitmapUtils;

import java.util.ArrayList;

public class EmojiModel {

    private Context mContext;
    private EditText mEditText;
    private ArrayList<Emotion> mEmojiList;

    public EmojiModel(EditText editText) {
        mEditText = editText;
        mContext = mEditText.getContext();
        initEmojiList();
    }

    public void delete() {
        if (mEditText == null) {
            return;
        }
        mEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    public void addEmojiSpan(int pageIndex, int position) {
        int index = pageIndex * 20 + position;
        Emotion emotion = mEmojiList.get(index);

        int id = mContext.getResources().getIdentifier(emotion.key, "drawable", mContext.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(mContext, id);
        Bitmap bitmap = BitmapUtils.drawableToBitmap(drawable);

        final SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(emotion.value);
        sb.append(" ");
        sb.setSpan(new ImageSpan(null, bitmap), 0, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mEditText.getEditableText().insert(mEditText.getSelectionStart(), sb);
    }

    public ArrayList<Emotion> getEmojiList() {
        return mEmojiList;
    }

    private void initEmojiList() {
        if (mEmojiList == null) {
            mEmojiList = new ArrayList<>();
            mEmojiList.add(new Emotion("emoji_0x1f603", ":smiley:"));
            mEmojiList.add(new Emotion("emoji_0x1f60d", ":heart_eyes:"));
            mEmojiList.add(new Emotion("emoji_0x1f612", ":unamused:"));
            mEmojiList.add(new Emotion("emoji_0x1f633", ":flushed:"));
            mEmojiList.add(new Emotion("emoji_0x1f601", ":grin:"));
            mEmojiList.add(new Emotion("emoji_0x1f618", ":kissing_heart:"));
            mEmojiList.add(new Emotion("emoji_0x1f609", ":wink:"));
            mEmojiList.add(new Emotion("emoji_0x1f620", ":angry:"));
            mEmojiList.add(new Emotion("emoji_0x1f61e", ":disappointed:"));
            mEmojiList.add(new Emotion("emoji_0x1f625", ":disappointed_relieved:"));
            mEmojiList.add(new Emotion("emoji_0x1f62d", ":sob:"));
            mEmojiList.add(new Emotion("emoji_0x1f61d", ":stuck_out_tongue_closed_eyes:"));
            mEmojiList.add(new Emotion("emoji_0x1f621", ":rage:"));
            mEmojiList.add(new Emotion("emoji_0x1f623", ":persevere:"));
            mEmojiList.add(new Emotion("emoji_0x1f614", ":pensive:"));
            mEmojiList.add(new Emotion("emoji_0x1f604", ":smile:"));
            mEmojiList.add(new Emotion("emoji_0x1f637", ":mask:"));
            mEmojiList.add(new Emotion("emoji_0x1f61a", ":kissing_closed_eyes:"));
            mEmojiList.add(new Emotion("emoji_0x1f613", ":sweat:"));
            mEmojiList.add(new Emotion("emoji_0x1f602", ":joy:"));

            mEmojiList.add(new Emotion("emoji_0x1f60a", ":blush:"));
            mEmojiList.add(new Emotion("emoji_0x1f622", ":cry:"));
            mEmojiList.add(new Emotion("emoji_0x1f61c", ":stuck_out_tongue_winking_eye:"));
            mEmojiList.add(new Emotion("emoji_0x1f628", ":fearful:"));
            mEmojiList.add(new Emotion("emoji_0x1f630", ":cold_sweat:"));
            mEmojiList.add(new Emotion("emoji_0x1f632", ":astonished:"));
            mEmojiList.add(new Emotion("emoji_0x1f60f", ":smirk:"));
            mEmojiList.add(new Emotion("emoji_0x1f631", ":scream:"));
            mEmojiList.add(new Emotion("emoji_0x1f62a", ":sleepy:"));
            mEmojiList.add(new Emotion("emoji_0x1f616", ":confounded:"));
            mEmojiList.add(new Emotion("emoji_0x1f60c", ":relieved:"));
            mEmojiList.add(new Emotion("emoji_0x1f47f", ":imp:"));
            mEmojiList.add(new Emotion("emoji_0x1f47b", ":ghost:"));
            mEmojiList.add(new Emotion("emoji_0x1f385", ":santa:"));
            mEmojiList.add(new Emotion("emoji_0x1f467", ":girl:"));
            mEmojiList.add(new Emotion("emoji_0x1f466", ":boy:"));
            mEmojiList.add(new Emotion("emoji_0x1f469", ":woman:"));
            mEmojiList.add(new Emotion("emoji_0x1f468", ":man:"));
            mEmojiList.add(new Emotion("emoji_0x1f436", ":dog:"));
            mEmojiList.add(new Emotion("emoji_0x1f431", ":cat:"));

            mEmojiList.add(new Emotion("emoji_0x1f44d", ":thumbsup:"));
            mEmojiList.add(new Emotion("emoji_0x1f44e", ":-1:"));
            mEmojiList.add(new Emotion("emoji_0x1f44a", ":facepunch:"));
            mEmojiList.add(new Emotion("emoji_0x270a", ":fist:"));
            mEmojiList.add(new Emotion("emoji_0x270c", ":v:"));
            mEmojiList.add(new Emotion("emoji_0x1f4aa", ":muscle:"));
            mEmojiList.add(new Emotion("emoji_0x1f44f", ":clap:"));
            mEmojiList.add(new Emotion("emoji_0x1f448", ":point_left:"));
            mEmojiList.add(new Emotion("emoji_0x1f446", ":point_up_2:"));
            mEmojiList.add(new Emotion("emoji_0x1f449", ":point_right:"));
            mEmojiList.add(new Emotion("emoji_0x1f447", ":point_down:"));
            mEmojiList.add(new Emotion("emoji_0x1f44c", ":ok_hand:"));
            mEmojiList.add(new Emotion("emoji_0x2764", ":heart:"));
            mEmojiList.add(new Emotion("emoji_0x1f494", ":broken_heart:"));
            mEmojiList.add(new Emotion("emoji_0x1f64f", ":pray:"));
            mEmojiList.add(new Emotion("emoji_0x2600", ":sunny:"));
            mEmojiList.add(new Emotion("emoji_0x1f319", ":crescent_moon:"));
            mEmojiList.add(new Emotion("emoji_0x1f31f", ":star2:"));
            mEmojiList.add(new Emotion("emoji_0x26a1", ":zap:"));
            mEmojiList.add(new Emotion("emoji_0x2601", ":cloud:"));

            mEmojiList.add(new Emotion("emoji_0x2614", ":umbrella:"));
            mEmojiList.add(new Emotion("emoji_0x1f341", ":maple_leaf:"));
            mEmojiList.add(new Emotion("emoji_0x1f33b", ":sunflower:"));
            mEmojiList.add(new Emotion("emoji_0x1f343", ":leaves:"));
            mEmojiList.add(new Emotion("emoji_0x1f457", ":dress:"));
            mEmojiList.add(new Emotion("emoji_0x1f380", ":ribbon:"));
            mEmojiList.add(new Emotion("emoji_0x1f444", ":lips:"));
            mEmojiList.add(new Emotion("emoji_0x1f339", ":rose:"));
            mEmojiList.add(new Emotion("emoji_0x2615", ":coffee:"));
            mEmojiList.add(new Emotion("emoji_0x1f382", ":birthday:"));
            mEmojiList.add(new Emotion("emoji_0x1f559", ":clock10:"));
            mEmojiList.add(new Emotion("emoji_0x1f37a", ":beer:"));
            mEmojiList.add(new Emotion("emoji_0x1f50d", ":mag:"));
            mEmojiList.add(new Emotion("emoji_0x1f4f1", ":iphone:"));
            mEmojiList.add(new Emotion("emoji_0x1f3e0", ":house:"));
            mEmojiList.add(new Emotion("emoji_0x1f697", ":red_car:"));
            mEmojiList.add(new Emotion("emoji_0x1f381", ":gift:"));
            mEmojiList.add(new Emotion("emoji_0x26bd", ":soccer:"));
            mEmojiList.add(new Emotion("emoji_0x1f4a3", ":bomb:"));
            mEmojiList.add(new Emotion("emoji_0x1f48e", ":gem:"));
        }
    }

}
