package com.cubes.miletic.events.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.cubes.miletic.events.R;


public class EventsEditText extends LinearLayout {

    final int inputTypeText = InputType.TYPE_CLASS_TEXT;
    final int inputTypeEmail = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    final int inputTypePassVisible = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
    final int inputTypePassInvisible = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

    private LinearLayout linearLayoutEditText;
    private ImageView leftImage, rightImage;
    private EditText editText;
    private View viewLine;
    private TypedArray typedArray;


    public EventsEditText(Context context) {
        super(context);
        typedArray = context.obtainStyledAttributes(R.styleable.EventsEditText);
        setupLayout();
    }

    public EventsEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.EventsEditText);
        setupLayout();
    }

    public EventsEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.EventsEditText);
        setupLayout();
    }

    private void setupLayout(){
        //orientation and gravity for root layout
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        setupLayoutEditText();

        createLeftImage();
        createEditText();
        createRightImage();
        createViewLine();

        setupViews();
    }

    private void setupLayoutEditText() {
        //setup for layout with edit text and images
        linearLayoutEditText = new LinearLayout(getContext());
        linearLayoutEditText.setOrientation(HORIZONTAL);
        linearLayoutEditText.setGravity(Gravity.CENTER_VERTICAL);
        linearLayoutEditText.setPadding(0, 0, 0, 12);
        addView(linearLayoutEditText);
    }

    private void createLeftImage() {
        leftImage = new ImageView(getContext());
        leftImage.setLayoutParams(new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        leftImage.setPadding(0, 4, 0, 4);
        linearLayoutEditText.addView(leftImage);
    }

    private void createEditText() {
        editText = new EditText(getContext());
        editText.setLayoutParams(new LinearLayoutCompat.LayoutParams
                (0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setPadding(16, 0, 16, 0);
        editText.setBackgroundColor(getResources().getColor(R.color.transparent));
        linearLayoutEditText.addView(editText);
    }

    private void createRightImage() {
        rightImage = new ImageView(getContext());
        rightImage.setLayoutParams(new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rightImage.setPadding(0, 3, 0, 3);
        linearLayoutEditText.addView(rightImage);
    }

    private void createViewLine() {
        viewLine = new View(getContext());
        viewLine.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 4));
        addView(viewLine); //this is added to root linear layout
    }

    private void setupViews() {

        int inputType = typedArray.getInt(R.styleable.EventsEditText_android_inputType, 0);

        editText.setInputType(inputType);

        if(inputType == inputTypeText){
            leftImage.setImageResource(R.drawable.ic_person);
            rightImage.setVisibility(GONE);
            String hint = typedArray.getString(R.styleable.EventsEditText_android_hint);
            if(hint != null){
                editText.setHint(hint);
            }
            else{
                editText.setHint(R.string.text);
            }
        }
        else if(inputType == inputTypeEmail){
            leftImage.setImageResource(R.drawable.ic_email);
            rightImage.setImageResource(R.drawable.ic_check);
            editText.setHint(R.string.hint_email);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(isEmailValid(editable.toString())){
                        rightImage.setColorFilter(getResources().getColor(R.color.green));
                    }
                    else{
                        rightImage.setColorFilter(getResources().getColor(R.color.red));
                    }
                }
            });
        }
        else if(inputType == inputTypePassVisible || inputType == inputTypePassInvisible){
            leftImage.setImageResource(R.drawable.ic_lock);

            if(typedArray.getString(R.styleable.EventsEditText_android_hint)==null){
                editText.setHint(R.string.password);
            }
            else{
                editText.setHint(typedArray.getString(R.styleable.EventsEditText_android_hint));
            }

            if(inputType == inputTypePassInvisible){
                rightImage.setImageResource(R.drawable.ic_visibility);
            }
            else{
                rightImage.setImageResource(R.drawable.ic_visibility_off);
            }

            rightImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(editText.getInputType() == inputTypePassInvisible){
                        editText.setInputType(inputTypePassVisible);
                        rightImage.setImageResource(R.drawable.ic_visibility_off);
                    }
                    else{
                        editText.setInputType(inputTypePassInvisible);
                        rightImage.setImageResource(R.drawable.ic_visibility);
                    }
                }
            });
        }

        //set text
        String text = typedArray.getString(R.styleable.EventsEditText_android_text);
        if(text != null){
            editText.setText(text);
        }

        editText.setTextSize(typedArray.getInt(R.styleable.EventsEditText_android_textSize, 17));

        //set color
        int color = typedArray.getColor(R.styleable.EventsEditText_android_color, 0);
        if(color != 0){
            setColor(color);
        }

        //set enable
        boolean isEnabled = typedArray.getBoolean(R.styleable.EventsEditText_android_enabled, true);
        editText.setEnabled(isEnabled);
        rightImage.setEnabled(isEnabled);
    }

    public boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setColor(int color){
        leftImage.setColorFilter(color);
        rightImage.setColorFilter(color);
        viewLine.setBackgroundColor(color);
        editText.setTextColor(color);
        editText.setHintTextColor(getDarkerVersion(color, 0.7f));
    }

    private int getDarkerVersion(int color, float factor){
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a, (int) Math.max(r * factor, 0),
                (int) Math.max(g * factor, 0), (int) Math.max(b * factor, 0));
    }

    public String getText(){
        return editText.getText().toString();
    }

    public int getColor(){
        return rightImage.getSolidColor();
    }
}
