/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.example.android.permissionrequest.test;

import android.graphics.Rect;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebView;

import com.example.android.permissionrequest.ConfirmationDialogFragment;
import com.example.android.permissionrequest.MainActivity;
import com.example.android.permissionrequest.PermissionRequestFragment;
import com.example.android.permissionrequest.R;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Tests for PermissionRequest sample.
 */
public class SampleTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mTestActivity;
    private PermissionRequestFragment mTestFragment;

    public SampleTests() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTestActivity = getActivity();
        mTestFragment = (PermissionRequestFragment)
                mTestActivity.getSupportFragmentManager().getFragments().get(1);
    }

    /**
     * Test if the test fixture has been set up correctly.
     */
    public void testPreconditions() {
        assertNotNull("mTestActivity is null", mTestActivity);
        assertNotNull("mTestFragment is null", mTestFragment);
    }

    public void testWebView_grantPermissionRequest() throws Throwable {
        View view = mTestFragment.getView();
        assertNotNull(view);
        final WebView webView = (WebView) view.findViewById(R.id.web_view);
        assertNotNull(webView);

        final ConsoleMonitor monitor = new ConsoleMonitor();
        mTestFragment.setConsoleMonitor(monitor);

        // Click the "Start" button
        assertTrue(monitor.waitForKeyword("Page loaded", 2000));
        clickToggle(webView);

        // Wait for the dialog
        ConfirmationDialogFragment dialogFragment = waitForDialog();
        assertNotNull(dialogFragment);

        // Click "Allow"
        monitor.reset();
        clickDialogButton(dialogFragment, android.R.id.button1);

        assertTrue(monitor.waitForKeyword("Started", 2000));

        // Click the "Stop" button
        monitor.reset();
        clickToggle(webView);
        assertTrue(monitor.waitForKeyword("Stopped", 2000));

        // Click the "Start" button
        monitor.reset();
        clickToggle(webView);

        // Wait for the dialog
        dialogFragment = waitForDialog();
        assertNotNull(dialogFragment);

        // Click "Deny"
        monitor.reset();
        clickDialogButton(dialogFragment, android.R.id.button2);
        assertTrue(monitor.waitForKeyword("Denied", 2000));
    }

    /**
     * Click the Start/Stop button.
     *
     * @param webView The {@link WebView}.
     * @throws Throwable
     */
    private void clickToggle(final WebView webView) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                webView.getHitRect(rect);
                int x = rect.width() / 2;
                int y = 100;
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_DOWN, x, y, 0);
                webView.dispatchTouchEvent(event);
                event = MotionEvent.obtain(SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_UP, x, y, 0);
                webView.dispatchTouchEvent(event);
            }
        });
    }

    /**
     * Wait for the dialog for 2 seconds (100 ms * 20 trials).
     *
     * @return The dialog.
     * @throws InterruptedException
     */
    private ConfirmationDialogFragment waitForDialog() throws InterruptedException {
        int count = 20;
        ConfirmationDialogFragment dialog = null;
        while (0 < count) {
            dialog = (ConfirmationDialogFragment) mTestFragment.getChildFragmentManager()
                    .findFragmentByTag("dialog");
            if (null != dialog) {
                break;
            }
            Thread.sleep(100);
            --count;
        }
        return dialog;
    }

    /**
     * Press the specified button on the dialog.
     *
     * @param dialogFragment The dialog.
     * @param buttonId       The resource ID of the button to press.
     * @throws Throwable
     */
    private void clickDialogButton(final ConfirmationDialogFragment dialogFragment,
                                   final int buttonId) throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogFragment.getDialog().findViewById(buttonId).performClick();
                assertFalse(dialogFragment.isVisible());
            }
        });
    }

    private class ConsoleMonitor implements PermissionRequestFragment.ConsoleMonitor {

        private final ConcurrentLinkedQueue<String> mMessages = new ConcurrentLinkedQueue<String>();

        @Override
        public void onConsoleMessage(ConsoleMessage message) {
            mMessages.offer(message.message());
        }

        public boolean waitForKeyword(String keyword, long timeoutMs) throws InterruptedException {
            long time = 0;
            while (time < timeoutMs) {
                String message;
                while (null != (message = mMessages.poll())) {
                    if (message.contains(keyword)) {
                        return true;
                    }
                }
                Thread.sleep(100);
                time += 100;
            }
            return false;
        }

        public void reset() {
            mMessages.clear();
        }

    }

}
