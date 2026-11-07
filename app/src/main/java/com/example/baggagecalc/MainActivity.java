package com.example.baggagecalc;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Add your actual repository URL here after creating it.
    private static final String APP_URL = "";

    private final int navy = Color.rgb(19, 36, 62);
    private final int blue = Color.rgb(25, 103, 210);
    private final int muted = Color.rgb(100, 116, 139);
    private final int pale = Color.rgb(239, 244, 250);

    private ScrollView scroll;
    private LinearLayout content;
    private LinearLayout resultCard;

    private EditText etWeight, etRate;
    private Button economyButton, businessButton;
    private TextView allowanceText;
    private TextView excessText, chargeText, taxText, totalText;
    private TextView resultNotice;

    private boolean business = false;
    private ValueAnimator amountAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.enableEdgeToEdge(getWindow());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        buildScreen();

        ViewCompat.setOnApplyWindowInsetsListener(scroll, (view, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.ime()
            );

            view.setPadding(
                    bars.left, bars.top, bars.right, bars.bottom
            );

            return insets;
        });

        WindowCompat.getInsetsController(
                getWindow(), scroll
        ).setAppearanceLightStatusBars(true);

        WindowCompat.getInsetsController(
                getWindow(), scroll
        ).setAppearanceLightNavigationBars(true);

        content.setAlpha(0f);
        content.setTranslationY(dp(20));
        content.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(450)
                .start();
    }

    private void buildScreen() {
        scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(pale);
        scroll.setClipToPadding(true);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(12), dp(20), dp(28));

        scroll.addView(content, new ScrollView.LayoutParams(
                -1, -2
        ));

        setContentView(scroll);

        // Toolbar
        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);

        TextView brand = text("BaggageCalc", 21, navy, true);
        toolbar.addView(brand, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        Button about = button("About", false);
        toolbar.addView(about);
        about.setOnClickListener(view -> startActivity(
                new Intent(this, AboutActivity.class)
        ));

        Button share = button("Share", false);
        toolbar.addView(share);
        share.setOnClickListener(view -> shareApplication());

        content.addView(toolbar);

        // Hero card
        LinearLayout hero = column();

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.rgb(16, 29, 56),
                        Color.rgb(21, 61, 112),
                        Color.rgb(22, 125, 173)
                }
        );

        gradient.setCornerRadius(dp(26));
        hero.setBackground(gradient);
        hero.setPadding(dp(24), dp(24), dp(24), dp(24));

        add(content, hero, 18);

        hero.addView(text("✈  TRAVEL SMARTER", 13,
                Color.rgb(174, 222, 255), true));

        add(hero, text("Pack your bags.\nKnow your fees.", 30,
                Color.WHITE, true), 16);

        add(hero, text(
                "A little planning. A smoother journey.",
                15, Color.rgb(211, 229, 248), false
        ), 12);

        // Input card
        LinearLayout form = card();
        add(content, form, 20);

        form.addView(text("Your flight details", 21, navy, true));

        add(form, text(
                "Enter your checked baggage weight and rate.",
                14, muted, false
        ), 6);

        TextView weightLabel = text(
                "BAGGAGE WEIGHT · KG", 12, muted, true
        );
        add(form, weightLabel, 24);

        etWeight = input("e.g. 25");
        etWeight.setId(View.generateViewId());
        weightLabel.setLabelFor(etWeight.getId());
        add(form, etWeight, 8);

        add(form, text("TICKET CLASS", 12, muted, true), 20);

        LinearLayout classes = new LinearLayout(this);

        economyButton = button("Economy\n20 kg", false);
        businessButton = button("Business\n40 kg", false);

        LinearLayout.LayoutParams left =
                new LinearLayout.LayoutParams(0, dp(76), 1);
        left.setMarginEnd(dp(8));

        classes.addView(economyButton, left);
        classes.addView(businessButton,
                new LinearLayout.LayoutParams(0, dp(76), 1));

        add(form, classes, 10);

        economyButton.setOnClickListener(view -> selectClass(false));
        businessButton.setOnClickListener(view -> selectClass(true));

        allowanceText = text("", 13, blue, false);
        add(form, allowanceText, 10);

        TextView rateLabel = text(
                "EXCESS RATE · RM PER KG", 12, muted, true
        );
        add(form, rateLabel, 20);

        etRate = input("e.g. 15");
        etRate.setId(View.generateViewId());
        rateLabel.setLabelFor(etRate.getId());
        add(form, etRate, 8);

        add(form, text(
                "An 8% service tax is added to the excess charge.",
                13, muted, false
        ), 16);

        Button calculate = button("Calculate my fee  →", true);
        add(form, calculate, 22);

        calculate.setOnClickListener(view -> {
            view.animate().cancel();
            view.setScaleX(0.97f);
            view.setScaleY(0.97f);
            view.animate().scaleX(1f).scaleY(1f)
                    .setDuration(180).start();
            calculateFee();
        });

        Button reset = button("Reset details", false);
        add(form, reset, 8);
        reset.setOnClickListener(view -> resetForm());

        // Result card
        resultCard = card();
        add(content, resultCard, 20);

        resultCard.addView(text("Your fee summary", 21, navy, true));

        excessText = text("", 16, navy, false);
        chargeText = text("", 16, navy, false);
        taxText = text("", 14, muted, false);

        add(resultCard, excessText, 20);
        add(resultCard, chargeText, 14);
        add(resultCard, taxText, 14);

        View divider = new View(this);
        divider.setBackgroundColor(Color.rgb(226, 232, 240));

        LinearLayout.LayoutParams dividerParams =
                new LinearLayout.LayoutParams(-1, dp(1));
        dividerParams.topMargin = dp(20);
        dividerParams.bottomMargin = dp(20);
        resultCard.addView(divider, dividerParams);

        resultCard.addView(text("TOTAL PAYABLE", 12, muted, true));

        totalText = text("", 34, blue, true);
        add(resultCard, totalText, 6);

        resultNotice = text("", 14, muted, false);
        add(resultCard, resultNotice, 14);

        TextView footer = text(
                "BaggageCalc • ICT602\n"
                        + "Estimates use the allowances and tax assumed "
                        + "for this assignment.",
                12, muted, false
        );
        footer.setGravity(Gravity.CENTER);
        add(content, footer, 22);

        updateClassAppearance();
        clearResults();
    }

    private void selectClass(boolean selectedBusiness) {
        business = selectedBusiness;
        updateClassAppearance();
        clearResults();
    }

    private void updateClassAppearance() {
        styleClass(economyButton, !business);
        styleClass(businessButton, business);

        economyButton.setSelected(!business);
        businessButton.setSelected(business);

        allowanceText.setText(business
                ? "Business includes 40 kg of checked baggage."
                : "Economy includes 20 kg of checked baggage.");
    }

    private void styleClass(Button button, boolean selected) {
        button.setBackgroundTintList(null);
        button.setBackground(background(
                selected ? blue : pale, 16
        ));
        button.setTextColor(selected ? Color.WHITE : navy);
    }

    private void calculateFee() {
        etWeight.setError(null);
        etRate.setError(null);
        clearResults();

        Double weight = readNumber(etWeight, false);
        if (weight == null) return;

        Double rate = readNumber(etRate, true);
        if (rate == null) return;

        double allowance = business ? 40 : 20;
        double excess = Math.max(0, weight - allowance);
        double charge = excess * rate;
        double tax = charge * 0.08;
        double total = charge + tax;

        if (Double.isInfinite(total)) {
            etRate.setError("Amount too large. Enter a smaller value.");
            etRate.requestFocus();
            return;
        }

        excessText.setText(String.format(
                Locale.US, "Excess weight: %.2f kg", excess
        ));

        chargeText.setText(String.format(
                Locale.US, "Excess baggage charge: RM %.2f", charge
        ));

        taxText.setText(String.format(
                Locale.US, "Service tax (8%%): RM %.2f", tax
        ));

        resultNotice.setText(excess == 0
                ? "Within your allowance. No excess baggage fee!"
                : "Includes 8% service tax.");

        resultNotice.setTextColor(excess == 0
                ? Color.rgb(21, 128, 90) : muted);

        // Count the total up to its final amount.
        amountAnimator = ValueAnimator.ofFloat(0f, 1f);
        amountAnimator.setDuration(650);
        amountAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            totalText.setText(String.format(
                    Locale.US, "RM %.2f", total * progress
            ));
        });
        amountAnimator.start();

        // Reveal the result with a short slide and fade.
        resultCard.animate().cancel();
        resultCard.setAlpha(0.3f);
        resultCard.setTranslationY(dp(24));
        resultCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .start();

        View focused = getCurrentFocus();
        if (focused != null) {
            InputMethodManager keyboard =
                    (InputMethodManager) getSystemService(
                            INPUT_METHOD_SERVICE
                    );
            if (keyboard != null) {
                keyboard.hideSoftInputFromWindow(
                        focused.getWindowToken(), 0
                );
            }
            focused.clearFocus();
        }

        scroll.postDelayed(() -> scroll.smoothScrollTo(
                0, resultCard.getTop()
        ), 300);
    }

    private Double readNumber(EditText field, boolean isRate) {
        String input = field.getText().toString().trim();

        if (input.isEmpty()) {
            field.setError(isRate
                    ? "Please enter the rate per kg."
                    : "Please enter the baggage weight.");
            field.requestFocus();
            return null;
        }

        try {
            double value = Double.parseDouble(input);

            if (Double.isNaN(value) || Double.isInfinite(value)) {
                field.setError("Enter a valid number.");
            } else if (value < 0 || (isRate && value == 0)) {
                field.setError(isRate
                        ? "Rate must be greater than 0."
                        : "Weight cannot be negative.");
            } else {
                return value;
            }
        } catch (NumberFormatException exception) {
            field.setError("Enter a number, such as 12.5.");
        }

        field.requestFocus();
        return null;
    }

    private void clearResults() {
        if (amountAnimator != null) {
            amountAnimator.cancel();
        }

        excessText.setText("Excess weight: —");
        chargeText.setText("Excess baggage charge: —");
        taxText.setText("Service tax (8%): —");
        totalText.setText("RM —");
        resultNotice.setText("Your estimate will appear here.");
        resultNotice.setTextColor(muted);
    }

    private void resetForm() {
        etWeight.setText("");
        etRate.setText("");
        etWeight.setError(null);
        etRate.setError(null);

        business = false;
        updateClassAppearance();
        clearResults();

        scroll.smoothScrollTo(0, 0);
    }

    private void shareApplication() {
        if (APP_URL.isEmpty()) {
            Toast.makeText(
                    this,
                    "Add your GitHub repository URL before sharing.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(
                Intent.EXTRA_TEXT,
                "Try BaggageCalc — estimate your excess baggage fee:\n"
                        + APP_URL
        );
        startActivity(Intent.createChooser(share, "Share BaggageCalc"));
    }

    // Design helpers
    private LinearLayout column() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private LinearLayout card() {
        LinearLayout layout = column();
        layout.setPadding(dp(20), dp(22), dp(20), dp(22));
        layout.setBackground(background(Color.WHITE, 24));
        layout.setElevation(dp(2));
        return layout;
    }

    private TextView text(String value, int size,
                          int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);

        if (bold) {
            view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }

        return view;
    }

    private EditText input(String hint) {
        EditText field = new EditText(this);
        field.setHint(hint);
        field.setTextSize(18);
        field.setTextColor(navy);
        field.setHintTextColor(muted);
        field.setSingleLine(true);
        field.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        );
        field.setPadding(dp(16), dp(12), dp(16), dp(12));
        field.setMinHeight(dp(56));
        field.setBackground(background(pale, 14));
        return field;
    }

    private Button button(String label, boolean primary) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(14);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setTextColor(primary ? Color.WHITE : navy);
        button.setMinHeight(dp(52));
        button.setMinimumWidth(0);
        button.setMinWidth(0);
        button.setPadding(dp(14), dp(8), dp(14), dp(8));
        button.setBackgroundTintList(null);
        button.setBackground(background(
                primary ? blue : pale, 16
        ));
        return button;
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private void add(LinearLayout parent, View child, int topMargin) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(topMargin);
        parent.addView(child, params);
    }

    private int dp(int value) {
        return Math.round(
                value * getResources().getDisplayMetrics().density
        );
    }

    @Override
    protected void onDestroy() {
        if (amountAnimator != null) {
            amountAnimator.cancel();
        }
        super.onDestroy();
    }
}