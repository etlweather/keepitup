package de.ibba.keepitup.ui.dialog;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import de.ibba.keepitup.R;
import de.ibba.keepitup.model.FileEntry;
import de.ibba.keepitup.test.mock.MockClipboardManager;
import de.ibba.keepitup.ui.BaseUITest;
import de.ibba.keepitup.ui.GlobalSettingsActivity;
import de.ibba.keepitup.ui.adapter.FileEntryAdapter;
import de.ibba.keepitup.util.BundleUtil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class FolderChooseDialogTest extends BaseUITest {

    @Rule
    public final ActivityTestRule<GlobalSettingsActivity> rule = new ActivityTestRule<>(GlobalSettingsActivity.class, false, false);

    private GlobalSettingsActivity activity;
    private String root;

    @Before
    public void beforeEachTestMethod() {
        super.beforeEachTestMethod();
        root = getFileManager().getExternalRootDirectory(0).getAbsolutePath();
        createTestFiles();
        activity = (GlobalSettingsActivity) launchSettingsInputActivity(rule);
        deleteLogFolder();
    }

    private void deleteLogFolder() {
        getFileManager().delete(getFileManager().getExternalDirectory("log", 0));
    }

    @Test
    public void testDisplayInitialFileListLevel1FolderExists() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel1FolderExistsScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        FileEntryAdapter adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel1FolderDoesNotExist() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder4");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder4")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder4")));
        assertEquals("folder4", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel1FolderDoesNotExistScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder4");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder4")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder4")));
        assertEquals("folder4", dialog.getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder4")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder4")));
        assertEquals("folder4", getDialog().getFolder());
        FileEntryAdapter adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder4")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder4")));
        assertEquals("folder4", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel1Empty() {
        getFileManager().delete(getFileManager().getExternalRootDirectory(0));
        FolderChooseDialog dialog = openFolderChooseDialog("xyz");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("xyz")));
        assertEquals("xyz", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertFalse(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel1EmptyScreenRotation() {
        getFileManager().delete(getFileManager().getExternalRootDirectory(0));
        FolderChooseDialog dialog = openFolderChooseDialog("xyz");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("xyz")));
        assertEquals("xyz", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("xyz")));
        assertEquals("xyz", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        adapter = getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("xyz")));
        assertEquals("xyz", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        adapter = getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertFalse(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel2FolderExists() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(5)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder2_file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel2FolderExistsScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(5)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder2_file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel2FolderDoesNotExist() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder6");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder6")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder6")));
        assertEquals("folder2/folder2_folder6", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(5)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder2_file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel2FolderDoesNotExistScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder6");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder6")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder6")));
        assertEquals("folder2/folder2_folder6", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder6")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder6")));
        assertEquals("folder2/folder2_folder6", getDialog().getFolder());
        adapter = getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder6")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder6")));
        assertEquals("folder2/folder2_folder6", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(5)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder2_file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel3() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder2/folder2_folder2_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel3ScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder2/folder2_folder2_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testDisplayInitialFileListLevel3Empty() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1/xyz");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1/xyz")));
        assertEquals("folder1/folder1_folder1/xyz", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
    }

    @Test
    public void testDisplayInitialFileListLevel3EmptyScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1/xyz");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1/xyz")));
        assertEquals("folder1/folder1_folder1/xyz", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1/xyz")));
        assertEquals("folder1/folder1_folder1/xyz", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1/xyz")));
        assertEquals("folder1/folder1_folder1/xyz", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testShowFilesLevel1() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(withId(R.id.checkbox_dialog_folder_choose_show_files)).perform(click());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.checkbox_dialog_folder_choose_show_files)).perform(click());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testShowFilesLevel1ScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        onView(withId(R.id.checkbox_dialog_folder_choose_show_files)).perform(click());
        adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testShowFilesLevel3NoFiles() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder2/folder2_folder2_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.checkbox_dialog_folder_choose_show_files)).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testShowFilesLevel3NoFilesScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder2/folder2_folder2_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.checkbox_dialog_folder_choose_show_files)).perform(click());
        adapter = getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectFolderLevel1InitialSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root)));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("")));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("..", true, true, false)));
        assertTrue(adapter.isItemSelected());
        assertEquals("", dialog.getFolder());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectFolderLevel1InitialSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root)));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("")));
        adapter = getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("..", true, true, false)));
        assertTrue(adapter.isItemSelected());
        assertEquals("", getDialog().getFolder());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectFolderLevel1NoInitialSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("xyz");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("xyz")));
        assertEquals("xyz", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root)));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("")));
        assertEquals("", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("..", true, true, false)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectFolderLevel1NoInitialSelectScreenRotaion() {
        FolderChooseDialog dialog = openFolderChooseDialog("xyz");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("xyz")));
        assertEquals("xyz", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", getDialog().getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root)));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("")));
        assertEquals("", getDialog().getFolder());
        adapter = getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("..", true, true, false)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectFolderLevel2InitialSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2")));
        assertEquals("folder2/folder2_folder2", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("..", true, true, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectFolderLevel2InitialSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2")));
        assertEquals("folder2/folder2_folder2", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        adapter = getAdapter();
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("..", true, true, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectFolderLevel2NoInitialSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/xyz");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/xyz")));
        assertEquals("folder2/xyz", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("..", true, true, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectFolderLevel2NoInitialSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/xyz");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/xyz")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/xyz")));
        assertEquals("folder2/xyz", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", getDialog().getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        adapter = getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("..", true, true, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testOpenFolderLevel1InitialSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testOpenFolderLevel1InitialSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", getDialog().getFolder());
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testOpenFolderLevel1NoInitialSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder4");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder4")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder4")));
        assertEquals("folder4", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(5)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder2_file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testOpenFolderLevel1NoInitialSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder4");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder4")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder4")));
        assertEquals("folder4", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder1")));
        assertEquals("folder2/folder2_folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertEquals(5, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder2_file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testOpenFolderLevel2InitialSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testOpenFolderLevel2InitialSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testOpenFolderLevel2NoInitialSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder5");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder5")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder5")));
        assertEquals("folder1/folder1_folder5", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testOpenFolderLevel2NoInitialSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder5");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder5")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder5")));
        assertEquals("folder1/folder1_folder5", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel1InputAndSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download"));
        assertEquals("folder2/xyz/download", dialog.getFolder());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel1InputAndSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download"));
        assertEquals("folder2/xyz/download", dialog.getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/xyz/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/xyz/download")));
        assertEquals("folder2/xyz/download", getDialog().getFolder());
        FileEntryAdapter adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel1InputAndOpen() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/abc"));
        assertEquals("folder2/abc", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel1InputAndOpenScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/abc"));
        assertEquals("folder2/abc", dialog.getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/abc")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/abc")));
        assertEquals("folder2/abc", getDialog().getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2"));
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        FileEntryAdapter adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel1CopyPaste() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        MockClipboardManager clipboardManager = prepareMockClipboardManager(dialog);
        clipboardManager.putData("test2/test2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(longClick());
        assertEquals(2, activity.getSupportFragmentManager().getFragments().size());
        onView(withId(R.id.listview_dialog_context_options)).check(matches(withListSize(2)));
        onView(withId(R.id.textview_dialog_context_options_title)).check(matches(withText("Text options")));
        onView(allOf(withId(R.id.textview_list_item_context_option_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_context_options), 0))).check(matches(withText("Copy")));
        onView(allOf(withId(R.id.textview_list_item_context_option_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_context_options), 1))).check(matches(withText("Paste")));
        onView(withId(R.id.imageview_dialog_context_options_cancel)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.textview_list_item_context_option_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_context_options), 1))).perform(click());
        assertEquals("test2/test2", dialog.getFolder());
        assertEquals("test2/test2", clipboardManager.getData());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(6)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("file2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withText("folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 4))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withText("folder3")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 5))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel2InputAndSelect() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download"));
        assertEquals("folder2/xyz/download", dialog.getFolder());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder2")));
        assertEquals("folder1/folder1_folder2", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel2InputAndSelectScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download"));
        assertEquals("folder2/xyz/download", dialog.getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/xyz/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/xyz/download")));
        assertEquals("folder2/xyz/download", getDialog().getFolder());
        FileEntryAdapter adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder2")));
        assertEquals("folder1/folder1_folder2", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel2InputAndOpen() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download"));
        assertEquals("folder2/xyz/download", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder2")));
        assertEquals("folder1/folder1_folder2", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder2")));
        assertEquals("folder1/folder1_folder2", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel2InputAndOpenScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download"));
        assertEquals("folder2/xyz/download", getDialog().getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/xyz/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/xyz/download")));
        assertEquals("folder2/xyz/download", getDialog().getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download123"));
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/xyz/download123")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/xyz/download123")));
        assertEquals("folder2/xyz/download123", getDialog().getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        FileEntryAdapter adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1")));
        assertEquals("folder1", getDialog().getFolder());
        adapter = getAdapter();
        assertEquals(6, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("file2", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(4), getFileEntry("folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(5), getFileEntry("folder3", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel2CopyPaste() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder1/folder1_folder1");
        MockClipboardManager clipboardManager = prepareMockClipboardManager(dialog);
        clipboardManager.putData("test2/test2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder1/folder1_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder1/folder1_folder1")));
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(longClick());
        assertEquals(2, activity.getSupportFragmentManager().getFragments().size());
        onView(withId(R.id.listview_dialog_context_options)).check(matches(withListSize(2)));
        onView(withId(R.id.textview_dialog_context_options_title)).check(matches(withText("Text options")));
        onView(allOf(withId(R.id.textview_list_item_context_option_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_context_options), 0))).check(matches(withText("Copy")));
        onView(allOf(withId(R.id.textview_list_item_context_option_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_context_options), 1))).check(matches(withText("Paste")));
        onView(withId(R.id.imageview_dialog_context_options_cancel)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.textview_list_item_context_option_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_context_options), 0))).perform(click());
        assertEquals("folder1/folder1_folder1", dialog.getFolder());
        assertEquals("folder1/folder1_folder1", clipboardManager.getData());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(4)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_file)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder1_file1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(not(isDisplayed())));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withText("folder1_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 2))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withText("folder1_folder2")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 3))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(4, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder1_file1", false, false, false)));
        assertTrue(areEntriesEqual(adapter.getItem(2), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getItem(3), getFileEntry("folder1_folder2", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder1_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel3InputAndOpen() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder2/folder2_folder2_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download"));
        assertEquals("folder2/xyz/download", dialog.getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testFolderLevel3InputAndOpenScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2/folder2_folder2/folder2_folder2_folder1");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", dialog.getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).perform(replaceText("folder2/xyz/download"));
        assertEquals("folder2/xyz/download", getDialog().getFolder());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        FileEntryAdapter adapter = getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2/folder2_folder2/folder2_folder2_folder1")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2/folder2_folder2/folder2_folder2_folder1")));
        assertEquals("folder2/folder2_folder2/folder2_folder2_folder1", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("folder2_folder2_folder1")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        adapter = getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(areEntriesEqual(adapter.getSelectedItem(), getFileEntry("folder2_folder2_folder1", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testIdenticalFileNameOpen() {
        getFileManager().delete(getFileManager().getExternalRootDirectory(0));
        getFileManager().delete(getFileManager().getExternalRootDirectory(1));
        createIdenticalNameTestFiles();
        FolderChooseDialog dialog = openFolderChooseDialog("download");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download")));
        assertEquals("download", dialog.getFolder());
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download")));
        assertEquals("download", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("download")));
        FileEntryAdapter adapter = dialog.getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("download", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download/download")));
        assertEquals("download/download", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("download")));
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("download", true, false, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download/download/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download/download/download")));
        assertEquals("download/download/download", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download/download/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download/download/download")));
        assertEquals("download/download/download", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("download")));
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("download", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download/download")));
        assertEquals("download/download", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("download")));
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("download", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download")));
        assertEquals("download", dialog.getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("download")));
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("download", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testIdenticalFileNameOpenScreenRotation() {
        getFileManager().delete(getFileManager().getExternalRootDirectory(0));
        getFileManager().delete(getFileManager().getExternalRootDirectory(1));
        createIdenticalNameTestFiles();
        FolderChooseDialog dialog = openFolderChooseDialog("download");
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).perform(click());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download/download/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download/download/download")));
        assertEquals("download/download/download", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(1)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        FileEntryAdapter adapter = getAdapter();
        assertEquals(1, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, true)));
        assertFalse(adapter.isItemSelected());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).perform(click());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/download")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("download")));
        assertEquals("download", getDialog().getFolder());
        onView(withId(R.id.listview_dialog_folder_choose_file_entries)).check(matches(withListSize(2)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withText("..")));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_open), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 0))).check(matches(withDrawable(R.drawable.icon_folder_open_shadow)));
        onView(allOf(withId(R.id.imageview_list_item_file_entry_symbol), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withDrawable(R.drawable.icon_folder)));
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withChildDescendantAtPosition(withId(R.id.listview_dialog_folder_choose_file_entries), 1))).check(matches(withText("download")));
        adapter = getAdapter();
        assertEquals(2, adapter.getItemCount());
        assertTrue(areEntriesEqual(adapter.getItem(0), getFileEntry("..", true, true, false)));
        assertTrue(areEntriesEqual(adapter.getItem(1), getFileEntry("download", true, false, true)));
        assertTrue(adapter.isItemSelected());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    @Test
    public void testSelectSwipeScreenRotation() {
        FolderChooseDialog dialog = openFolderChooseDialog("folder2");
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", dialog.getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(withId(R.id.scrollview_dialog_folder_choose)).perform(swipeUp());
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withText("folder3"))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder3")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder3")));
        assertEquals("folder3", getDialog().getFolder());
        rotateScreen(activity);
        onView(isRoot()).perform(waitFor(1000));
        deleteLogFolder();
        onView(allOf(withId(R.id.textview_list_item_file_entry_name), withText("folder2"))).perform(click());
        onView(withId(R.id.textview_dialog_folder_choose_absolute)).check(matches(withText(root + "/folder2")));
        onView(withId(R.id.edittext_dialog_folder_choose_folder)).check(matches(withText("folder2")));
        assertEquals("folder2", getDialog().getFolder());
        onView(withId(R.id.imageview_dialog_folder_choose_cancel)).perform(click());
    }

    private FolderChooseDialog openFolderChooseDialog(String folder) {
        FolderChooseDialog folderChooseDialog = new FolderChooseDialog();
        Bundle bundle = BundleUtil.stringsToBundle(new String[]{folderChooseDialog.getFolderRootKey(), folderChooseDialog.getFolderKey()}, new String[]{root, folder});
        folderChooseDialog.setArguments(bundle);
        folderChooseDialog.show(activity.getSupportFragmentManager(), GlobalSettingsActivity.class.getName());
        return folderChooseDialog;
    }

    private FolderChooseDialog getDialog() {
        return (FolderChooseDialog) rule.getActivity().getSupportFragmentManager().getFragments().get(0);
    }

    private FileEntryAdapter getAdapter() {
        return getDialog().getAdapter();
    }

    private void createIdenticalNameTestFiles() {
        File downloadFolder1 = new File(root, "download");
        assertTrue(downloadFolder1.mkdirs());
        File downloadFolder2 = new File(downloadFolder1, "download");
        assertTrue(downloadFolder2.mkdirs());
        File downloadFolder3 = new File(downloadFolder2, "download");
        assertTrue(downloadFolder3.mkdirs());
    }

    private void createTestFiles() {
        try {
            File folder1 = new File(root, "folder1");
            File file1 = new File(root, "file1");
            File file2 = new File(root, "file2");
            File folder2 = new File(root, "folder2");
            File folder3 = new File(root, "folder3");
            assertTrue(folder1.mkdir());
            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
            assertTrue(folder2.mkdir());
            assertTrue(folder3.mkdir());
            File folder1Folder1 = new File(folder1, "folder1_folder1");
            File folder1Folder2 = new File(folder1, "folder1_folder2");
            File folder1File1 = new File(folder1, "folder1_file1");
            assertTrue(folder1Folder1.mkdir());
            assertTrue(folder1Folder2.mkdir());
            assertTrue(folder1File1.createNewFile());
            File folder2Folder1 = new File(folder2, "folder2_folder1");
            File folder2File1 = new File(folder2, "folder2_file1");
            File folder2File2 = new File(folder2, "folder2_file2");
            File folder2Folder2 = new File(folder2, "folder2_folder2");
            assertTrue(folder2Folder1.mkdir());
            assertTrue(folder2File1.createNewFile());
            assertTrue(folder2File2.createNewFile());
            assertTrue(folder2Folder2.mkdir());
            File folder2Folder2Folder1 = new File(folder2Folder2, "folder2_folder2_folder1");
            assertTrue(folder2Folder2Folder1.mkdir());
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private FileEntry getFileEntry(String name, boolean directory, boolean parent, boolean canVisit) {
        FileEntry fileEntry = new FileEntry();
        fileEntry.setName(name);
        fileEntry.setDirectory(directory);
        fileEntry.setParent(parent);
        fileEntry.setCanVisit(canVisit);
        return fileEntry;
    }

    private boolean areEntriesEqual(FileEntry entry1, FileEntry entry2) {
        if (!entry1.getName().equals(entry2.getName())) {
            return false;
        }
        if (entry1.isDirectory() != entry2.isDirectory()) {
            return false;
        }
        if (entry1.canVisit() != entry2.canVisit()) {
            return false;
        }
        return entry1.isParent() == entry2.isParent();
    }

    private MockClipboardManager prepareMockClipboardManager(FolderChooseDialog folderChooseDialog) {
        MockClipboardManager clipboardManager = new MockClipboardManager();
        clipboardManager.clearData();
        folderChooseDialog.injectClipboardManager(clipboardManager);
        return clipboardManager;
    }
}
