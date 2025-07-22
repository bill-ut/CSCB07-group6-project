package com.example.b07demosummer2024;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import com.example.b07demosummer2024.data.*;
import com.example.b07demosummer2024.questions.*;

import java.util.HashMap;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class JsonReaderTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.b07demosummer2024", context.getPackageName());

        JsonReader reader = new JsonReader();
        HashMap<String, Question> map = reader.getQuestionMap(context, "questions.json");

        assertNotNull(map);
        assertTrue(map.containsKey("warmup1"));
        assertEquals("Which best describes your situation?", map.get("warmup1").getStatement());
        assertTrue(map.get("warmup1") instanceof SelectionQuestion);
    }
}