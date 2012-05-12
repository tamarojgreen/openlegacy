package org.openlegacy.web.tags;

import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;

public class SpanTag extends AbstractHtmlElementTag {

	private static final long serialVersionUID = 1L;

	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		tagWriter.startTag("span");
		writeDefaultAttributes(tagWriter);
		tagWriter.appendValue(getDisplayString(getBoundValue()));
		return EVAL_BODY_INCLUDE;
	}
}
