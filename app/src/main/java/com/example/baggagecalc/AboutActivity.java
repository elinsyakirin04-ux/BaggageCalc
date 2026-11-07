package com.example.baggagecalc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    private static final String APP_URL =
            "https://github.com/elinsyakirin04-ux/BaggageCalc";

    private final int navy = Color.rgb(19, 36, 62);
    private final int blue = Color.rgb(25, 103, 210);
    private final int muted = Color.rgb(100, 116, 139);
    private final int pale = Color.rgb(239, 244, 250);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(pale);
        scroll.setClipToPadding(true);

        LinearLayout content = column();
        content.setPadding(dp(20), dp(12), dp(20), dp(28));
        scroll.addView(content, new ScrollView.LayoutParams(-1, -2));
        setContentView(scroll);

        ViewCompat.setOnApplyWindowInsetsListener(
                scroll, (view, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );
                    view.setPadding(
                            bars.left, bars.top,
                            bars.right, bars.bottom
                    );
                    return insets;
                }
        );

        WindowCompat.getInsetsController(getWindow(), scroll)
                .setAppearanceLightStatusBars(true);
        WindowCompat.getInsetsController(getWindow(), scroll)
                .setAppearanceLightNavigationBars(true);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);

        Button back = button("← Back", false);
        toolbar.addView(back);
        back.setOnClickListener(view -> finish());

        TextView title = text("About the app", 20, navy, true);
        title.setGravity(Gravity.END);
        toolbar.addView(
                title, new LinearLayout.LayoutParams(0, -2, 1)
        );
        content.addView(toolbar);

        LinearLayout hero = column();
        hero.setPadding(dp(24), dp(28), dp(24), dp(28));

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
        add(content, hero, 18);

        hero.addView(text("✈", 44, Color.WHITE, true));
        add(hero, text("BaggageCalc", 32, Color.WHITE, true), 12);
        add(hero, text(
                "Travel lighter.\nPlan smarter.",
                21, Color.rgb(213, 233, 252), false
        ), 8);

        TextView version = text(
                "VERSION 1.0  •  ICT602", 12, Color.WHITE, true
        );
        version.setPadding(dp(14), dp(10), dp(14), dp(10));
        version.setBackground(background(
                Color.argb(35, 255, 255, 255), 12
        ));

        LinearLayout.LayoutParams versionParams =
                new LinearLayout.LayoutParams(-2, -2);
        versionParams.topMargin = dp(22);
        hero.addView(version, versionParams);

        LinearLayout developer = card();
        add(content, developer, 20);
        developer.addView(text(
                "MEET THE DEVELOPER", 12, blue, true
        ));

        LinearLayout identity = new LinearLayout(this);
        identity.setGravity(Gravity.CENTER_VERTICAL);
        add(developer, identity, 20);

        TextView avatar = text("SA", 23, Color.WHITE, true);
        avatar.setGravity(Gravity.CENTER);
        avatar.setBackground(background(blue, 22));
        identity.addView(
                avatar, new LinearLayout.LayoutParams(dp(64), dp(64))
        );

        LinearLayout nameBlock = column();
        LinearLayout.LayoutParams nameParams =
                new LinearLayout.LayoutParams(0, -2, 1);
        nameParams.setMarginStart(dp(16));
        identity.addView(nameBlock, nameParams);

        nameBlock.addView(text(
                "MUHAMMAD SYAKIRIN\nAMBRAN", 18, navy, true
        ));
        add(nameBlock, text(
                "Student developer", 14, muted, false
        ), 6);

        divider(developer);
        detail(developer, "STUDENT NUMBER", "2025156711");
        detail(developer, "CLASS / GROUP", "RCDCS2405B");
        detail(developer, "PROGRAMME CODE", "CS240");
        detail(developer, "COURSE CODE", "ICT602");

        LinearLayout information = card();
        add(content, information, 20);
        information.addView(text(
                "Built for smoother journeys", 21, navy, true
        ));
        add(information, text(
                "Estimate excess baggage fees before you fly. "
                        + "Choose your ticket class, enter your baggage "
                        + "weight and rate, then view a clear fee breakdown.",
                15, muted, false
        ), 12);

        divider(information);
        detail(information, "ECONOMY ALLOWANCE", "20 kg");
        detail(information, "BUSINESS ALLOWANCE", "40 kg");
        detail(information, "SERVICE TAX", "8% of the excess charge");

        add(information, text(
                "The allowances and tax above are assumed for this "
                        + "assignment. Actual airline fees may differ.",
                13, muted, false
        ), 18);

        LinearLayout website = card();
        add(content, website, 20);
        website.addView(text(
                "Explore the project", 21, navy, true
        ));
        add(website, text(
                "View the source code and project information on GitHub.",
                15, muted, false
        ), 10);

        TextView link = text(APP_URL, 14, blue, false);
        link.setPadding(0, dp(12), 0, dp(12));
        link.setPaintFlags(
                link.getPaintFlags()
                        | android.graphics.Paint.UNDERLINE_TEXT_FLAG
        );
        link.setOnClickListener(view -> openWebsite());
        add(website, link, 12);

        Button openWebsite = button("Open GitHub project  ↗", true);
        add(website, openWebsite, 10);
        openWebsite.setOnClickListener(view -> openWebsite());

        Button returnButton = button("Back to calculator  →", true);
        add(content, returnButton, 24);
        returnButton.setOnClickListener(view -> finish());

        TextView copyright = text(
                "© 2026 MUHAMMAD SYAKIRIN AMBRAN\n"
                        + "All rights reserved.\n\n"
                        + "Developed for the ICT602 individual assignment.",
                12, muted, false
        );
        copyright.setGravity(Gravity.CENTER);
        add(content, copyright, 24);

        content.setAlpha(0f);
        content.setTranslationY(dp(24));
        content.animate().alpha(1f).translationY(0f)
                .setDuration(500).start();
    }

    private void openWebsite() {
        try {
            startActivity(new Intent(
                    Intent.ACTION_VIEW, Uri.parse(APP_URL)
            ));
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(
                    this, "No browser is available on this device.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void detail(LinearLayout parent,
                        String label, String value) {
        add(parent, text(label, 11, muted, true), 16);
        add(parent, text(value, 16, navy, true), 5);
    }

    private void divider(LinearLayout parent) {
        View line = new View(this);
        line.setBackgroundColor(Color.rgb(226, 232, 240));
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(1));
        params.topMargin = dp(22);
        parent.addView(line, params);
    }

    private LinearLayout column() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private LinearLayout card() {
        LinearLayout layout = column();
        layout.setPadding(dp(22), dp(24), dp(22), dp(24));
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
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return view;
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
        button.setPadding(dp(16), dp(10), dp(16), dp(10));
        button.setBackgroundTintList(null);
        button.setBackground(background(primary ? blue : pale, 16));
        return button;
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private void add(LinearLayout parent, View child, int margin) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(margin);
        parent.addView(child, params);
    }

    private int dp(int value) {
        return Math.round(
                value * getResources().getDisplayMetrics().density
        );
    }
}
