/*
 * Kiwix Android
 * Copyright (c) 2020 Kiwix <android.kiwix.org>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.kiwix.kiwixmobile.core.page.bookmark.viewmodel.effects

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.processors.PublishProcessor
import org.junit.jupiter.api.Test
import org.kiwix.kiwixmobile.core.base.SideEffect
import org.kiwix.kiwixmobile.core.dao.NewBookmarksDao
import org.kiwix.kiwixmobile.core.page.bookmark.BookmarksActivity
import org.kiwix.kiwixmobile.core.page.bookmarkState
import org.kiwix.kiwixmobile.core.utils.DialogShower
import org.kiwix.kiwixmobile.core.utils.KiwixDialog

internal class ShowDeleteBookmarksDialogTest {

  @Test
  fun `invoke with shows dialog that offers ConfirmDelete action`() {
    val effects = mockk<PublishProcessor<SideEffect<*>>>(relaxed = true)
    val newBookmarksDao = mockk<NewBookmarksDao>()
    val activity = mockk<BookmarksActivity>()
    val showDeleteBookmarksDialog =
      ShowDeleteBookmarksDialog(effects, bookmarkState(), newBookmarksDao)
    val dialogShower = mockk<DialogShower>()
    every { activity.activityComponent.inject(showDeleteBookmarksDialog) } answers {
      showDeleteBookmarksDialog.dialogShower = dialogShower
      Unit
    }
    val lambdaSlot = slot<() -> Unit>()
    showDeleteBookmarksDialog.invokeWith(activity)
    verify { dialogShower.show(KiwixDialog.DeleteBookmarks, capture(lambdaSlot)) }
    lambdaSlot.captured.invoke()
    verify { effects.offer(DeleteBookmarkItems(effects, bookmarkState(), newBookmarksDao)) }
  }
}
