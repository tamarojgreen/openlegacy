package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa.JpaEntityUtils;

import org.eclipse.core.runtime.Assert;
import org.openlegacy.db.definitions.DbEntityDefinition;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntity extends AbstractEntity {

	private DbEntityDefinition entityDefinition;

	private JpaEntityModel entityModel;
	private JpaTableModel tableModel;
	private JpaNavigationModel navigationModel;

	private Map<UUID, JpaFieldModel> fields = new HashMap<UUID, JpaFieldModel>();
	private List<JpaFieldModel> sortedFields = new ArrayList<JpaFieldModel>();

	public JpaEntity(DbEntityDefinition dbEntityDefinition) {
		Assert.isNotNull(dbEntityDefinition, Messages.getString("jpa.error.entity.creation.definition.is.null"));
		this.entityDefinition = dbEntityDefinition;
		init();
	}

	private void init() {
		entityModel = JpaEntityUtils.getJpaEntityModel(entityDefinition);
		tableModel = JpaEntityUtils.getJpaTableModel(entityDefinition);

		// initialize fields
		if (!entityDefinition.getColumnFieldsDefinitions().isEmpty()) {
			fields = JpaEntityUtils.getJpaFieldsModels(entityModel, entityDefinition.getColumnFieldsDefinitions(), sortedFields);
		}

		navigationModel = JpaEntityUtils.getJpaNavigationModel(entityDefinition);
	}

	@Override
	public String getEntityFullyQualifiedName() {
		if (entityDefinition != null) {
			return MessageFormat.format("{0}.{1}", entityDefinition.getPackageName(), entityDefinition.getEntityClassName());
		}
		return "";
	}

	public void addJpaFieldModel(JpaFieldModel newModel) {
		fields.put(newModel.getUUID(), newModel.clone());
		sortedFields.add(newModel);
		newFieldsCount++;
		setDirty(true);
	}

	public void removeJpaFieldModel(JpaFieldModel model) {
		if (fields.containsKey(model.getUUID())) {
			fields.remove(model.getUUID());
			sortedFields.remove(model);
			newFieldsCount--;
			setDirty(!this.actions.isEmpty() || (newFieldsCount != 0));
		}
	}

	public DbEntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

	public JpaEntityModel getEntityModel() {
		return entityModel;
	}

	public JpaTableModel getTableModel() {
		return tableModel;
	}

	public Map<UUID, JpaFieldModel> getFields() {
		return fields;
	}

	public List<JpaFieldModel> getSortedFields() {
		return sortedFields;
	}

	public JpaNavigationModel getNavigationModel() {
		return navigationModel;
	}

}
