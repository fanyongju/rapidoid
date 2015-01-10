package org.rapidoid.app;

/*
 * #%L
 * rapidoid-app
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski
 * %%
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
 * #L%
 */

import org.rapidoid.annotation.Session;
import org.rapidoid.db.DB;
import org.rapidoid.html.Tag;
import org.rapidoid.security.Secure;
import org.rapidoid.util.U;
import org.rapidoid.widget.FormWidget;

public class ViewEntityScreenGeneric extends Screen {

	@Session
	private Object target;

	public Object content() {
		target = entity();

		Tag caption = titleBox(U.capitalized(ctx().pathSegment(0)) + " Details");
		FormWidget details = show(target);

		if (Secure.getObjectPermissions(ctx().username(), target).delete) {
			details = details.buttons(EDIT, BACK, DELETE);
		} else {
			details = details.buttons(EDIT, BACK);
		}

		return row(caption, details);
	}

	public void onEdit() {
		String id = ctx().pathSegment(1);
		ctx().redirect("/edit" + target.getClass().getSimpleName() + "/" + id);
	}

	public void onDelete() {
		showModal("confirmDeletion");
	}

	public void onYesDelete() {
		long id = Long.parseLong(ctx().pathSegment(1));
		DB.delete(id);

		hideModal();
		ctx().goBack(1);
	}

	public Tag confirmDeletion() {
		return modal("Confirm deletion", h4("Are you sure you want to delete the record?"), div(YES_DELETE, CANCEL));
	}

}
