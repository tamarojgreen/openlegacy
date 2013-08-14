package org.openlegacy.designtime.formatters;

import org.apache.commons.beanutils.PropertyUtils;
import org.openlegacy.definitions.AbstractPartEntityDefinition;
import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.definitions.PartEntityDefinition;
import org.openlegacy.utils.StringUtil;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * A field definition formatter, which receives a list of replace text configurations to handle changing properties of field
 * definitions
 * 
 * @author Roi Mor
 * 
 */
public class ReplaceTextDefinitionFormatter implements DefinitionFormatter {

	private List<ReplaceTextConfiguration> replaceTextConfigurations;

	public void format(FieldDefinition fieldDefinition) {
		formatInner(fieldDefinition);
	}

	public void format(PartEntityDefinition<?> partDefinition) {
		formatInner(partDefinition);
		String name = partDefinition.getPartName().toLowerCase();
		AbstractPartEntityDefinition<?> updatablePartDefinition = (AbstractPartEntityDefinition<?>)partDefinition;
		updatablePartDefinition.setPartName(StringUtil.toClassName(name));
	}

	private void formatInner(Object definitions) {
		for (ReplaceTextConfiguration replaceTextConfiguration : replaceTextConfigurations) {

			String propertyValue = "";
			try {
				PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(definitions,
						replaceTextConfiguration.getSourcePropertyName());
				if (propertyDescriptor == null) {
					continue;
				}
				propertyValue = (String)propertyDescriptor.getReadMethod().invoke(definitions);
			} catch (Exception e) {
				throw (new IllegalStateException(e));
			}

			String originalText = replaceTextConfiguration.getOriginalText();
			if (replaceTextConfiguration.isPrefix() && propertyValue.startsWith(originalText)) {
				propertyValue = propertyValue.replace(originalText, "");
			}
			if (replaceTextConfiguration.isSuffix() && propertyValue.endsWith(originalText)) {
				propertyValue = propertyValue.substring(0, propertyValue.length() - originalText.length() - 1);
			}
			if (!replaceTextConfiguration.isPrefix() && !replaceTextConfiguration.isSuffix()) {
				propertyValue = propertyValue.replaceAll(originalText, replaceTextConfiguration.getNewText());
			}

			try {
				PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(definitions,
						replaceTextConfiguration.getTargetPropertyName());
				if (propertyDescriptor == null) {
					continue;
				}
				propertyDescriptor.getWriteMethod().invoke(definitions, propertyValue);
			} catch (Exception e) {
				throw (new IllegalStateException(e));
			}
		}
	}

	public void setReplaceTextConfigurations(List<ReplaceTextConfiguration> replaceTextConfigurations) {
		this.replaceTextConfigurations = replaceTextConfigurations;
	}

}
