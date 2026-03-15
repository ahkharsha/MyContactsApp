package com.mycontactapp.contact.decorator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MaskedEmailDecorator
 * Enhances contact display by masking the username portion of any visible email addresses for privacy.
 * E.g., john.doe@example.com -> j******e@example.com
 */
public class MaskedEmailDecorator extends ContactDecorator {

    // Matches standard email format and captures the portions to keep vs mask
    private static final Pattern EMAIL_PATTERN = Pattern.compile("([a-zA-Z0-9._%+-]+)(@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})");

    public MaskedEmailDecorator(ContactDisplay wrappee) {
        super(wrappee);
    }

    @Override
    public String getFormattedDetails() {
        String original = super.getFormattedDetails();
        if (original == null) return null;

        Matcher matcher = EMAIL_PATTERN.matcher(original);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String username = matcher.group(1);
            String domain = matcher.group(2);

            String maskedUsername;
            if (username.length() <= 2) {
                maskedUsername = "*".repeat(username.length());
            } else {
                maskedUsername = username.charAt(0) + "*".repeat(Math.max(0, username.length() - 2)) + username.charAt(username.length() - 1);
            }

            matcher.appendReplacement(result, maskedUsername + domain);
        }
        matcher.appendTail(result);

        return result.toString();
    }
}
