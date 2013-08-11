package org.openlegacy.designtime.rpc.formatters;

import org.apache.commons.beanutils.PropertyUtils;
import org.openlegacy.definitions.FieldDefinition;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * A field definition formatter, which recieves a list of replace text configurations to handle changing properties of field
 * definitions
 * 
 * @author Roi Mor
 * 
 */
public class ReplaceTexFieldDefinitionFormatter implements FieldDefinitionFormatter {

	private List<ReplaceTextConfiguration> replaceTextConfigurations;

	public void format(FieldDefinition fieldDefinition) {
		for (ReplaceTextConfiguration replaceTextConfiguration : replaceTextConfigurations) {

			String propertyValue = "";
			try {
				PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(fieldDefinition,
						replaceTextConfiguration.getSourcePropertyName());
				propertyValue = (String)propertyDescriptor.getReadMethod().invoke(fieldDefinition);
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
				PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(fieldDefinition,
						replaceTextConfiguration.getTargetPropertyName());
				propertyDescriptor.getWriteMethod().invoke(fieldDefinition, propertyValue);
			} catch (Exception e) {
				throw (new IllegalStateException(e));
			}
		}
	}

	public void setReplaceTextConfigurations(List<ReplaceTextConfiguration> replaceTextConfigurations) {
		this.replaceTextConfigurations = replaceTextConfigurations;
	}

}
