package tn.esprit.utils;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class EmailService {
    private static final String API_TOKEN = "mlsn.18e82c8f8bb4c333220abb4351f54e9c92f4cc1f6698d174bd5df67a723b9e74";
    private static final String FROM_EMAIL = "MS_DdZpVQ@tunisiehayon.tn";
    private static final String FROM_NAME = "AgriNova";

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public boolean sendVerificationEmail(String recipientEmail, String recipientName, String verificationCode) {
        sendEmailAsync(
                recipientEmail,
                recipientName,
                "Verify Your Email - Agrinova Platform",
                buildVerificationEmailHtml(recipientName, verificationCode, recipientEmail),
                verificationCode
        );
        return true;
    }

    public boolean sendPasswordResetEmail(String recipientEmail, String recipientName, String resetCode) {
        sendEmailAsync(
                recipientEmail,
                recipientName,
                "Reset Your Password - Agrinova Platform",
                buildPasswordResetEmailHtml(recipientName, resetCode),
                resetCode
        );
        return true;
    }

    private void sendEmailAsync(String recipientEmail, String recipientName, String subject, String htmlContent, String code) {
        CompletableFuture.runAsync(() -> {
            try {
                Email email = new Email();
                email.setFrom(FROM_NAME, FROM_EMAIL);
                email.addRecipient(recipientName, recipientEmail);
                email.setSubject(subject);
                email.setPlain("Code: " + code);
                email.setHtml(htmlContent);

                MailerSend ms = new MailerSend();
                ms.setToken(API_TOKEN);

                MailerSendResponse response = ms.emails().send(email);
                System.out.println("Email sent successfully to: " + recipientEmail + " | Message ID: " + response.messageId);
            } catch (MailerSendException e) {
                System.err.println("Failed to send email (MailerSend): " + e.getMessage());
                if (e.code > 0) {
                    System.err.println("MailerSend error code: " + e.code);
                }

                if (e.errors != null) {
                    System.err.println("MailerSend errors: " + e.errors);
                }
                System.out.println("EMAIL SIMULATION MODE - Code: " + code);
            } catch (Exception e) {
                System.err.println("Failed to send email: " + e.getMessage());
                System.out.println("EMAIL SIMULATION MODE - Code: " + code);
            }
        });
    }

    private String buildVerificationEmailHtml(String recipientName, String verificationCode, String recipientEmail) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Email Verification - Agrinova Platform</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                <div style="background-color: #2E7D32; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0;">
                    <h1 style="margin: 0;">Agrinova Platform</h1>
                    <p style="margin: 5px 0 0 0;">Agricultural Management System</p>
                </div>

                <div style="background-color: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px;">
                    <h2 style="color: #2E7D32;">Welcome to Agrinova, %s!</h2>

                    <p>Thank you for signing up with Agrinova Platform. To complete your registration and verify your email address, please use the verification code below:</p>

                    <div style="background-color: white; border: 2px dashed #2E7D32; padding: 20px; text-align: center; margin: 20px 0; border-radius: 8px;">
                        <h3 style="margin: 0; font-size: 32px; font-weight: bold; color: #2E7D32; letter-spacing: 3px;">%s</h3>
                    </div>

                    <p><strong>Important:</strong></p>
                    <ul>
                        <li>This verification code will expire in 10 minutes</li>
                        <li>Enter this code in the signup form to verify your email</li>
                        <li>If you didn't request this verification, please ignore this email</li>
                    </ul>

                    <hr style="border: none; border-top: 1px solid #ddd; margin: 20px 0;">

                    <p style="color: #666; font-size: 14px;">
                        This email was sent to %s. If you have any questions or need assistance, please contact our support team.
                    </p>

                    <p style="color: #666; font-size: 12px; text-align: center; margin-top: 20px;">
                        © 2026 Agrinova Platform. All rights reserved.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(recipientName, verificationCode, recipientEmail);
    }

    private String buildPasswordResetEmailHtml(String recipientName, String resetCode) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Password Reset - Agrinova Platform</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                <div style="background-color: #d32f2f; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0;">
                    <h1 style="margin: 0;">Password Reset Request</h1>
                    <p style="margin: 5px 0 0 0;">Agrinova Platform</p>
                </div>

                <div style="background-color: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px;">
                    <h2 style="color: #d32f2f;">Password Reset for %s</h2>

                    <p>We received a request to reset your password for your Agrinova Platform account. Use the reset code below to create a new password:</p>

                    <div style="background-color: white; border: 2px dashed #d32f2f; padding: 20px; text-align: center; margin: 20px 0; border-radius: 8px;">
                        <h3 style="margin: 0; font-size: 32px; font-weight: bold; color: #d32f2f; letter-spacing: 3px;">%s</h3>
                    </div>

                    <p><strong>Important:</strong></p>
                    <ul>
                        <li>This reset code will expire in 15 minutes</li>
                        <li>Enter this code in the password reset form</li>
                        <li>If you didn't request this reset, please ignore this email and ensure your account is secure</li>
                    </ul>

                    <hr style="border: none; border-top: 1px solid #ddd; margin: 20px 0;">

                    <p style="color: #666; font-size: 14px;">
                        This email was sent to your registered email address. If you have any questions or need assistance, please contact our support team.
                    </p>

                    <p style="color: #666; font-size: 12px; text-align: center; margin-top: 20px;">
                        © 2026 Agrinova Platform. All rights reserved.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(recipientName, resetCode);
    }
}
