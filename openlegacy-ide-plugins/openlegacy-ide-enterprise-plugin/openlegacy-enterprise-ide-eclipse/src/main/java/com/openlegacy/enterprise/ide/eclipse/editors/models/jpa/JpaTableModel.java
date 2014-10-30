package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.db.definitions.DbTableDefinition;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;
import org.openlegacy.db.definitions.SimpleDbTableUniqueConstraintDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Table;

/**
 * @author Ivan Bort
 * 
 */
public class JpaTableModel extends JpaNamedObject {

	// annotation attributes
	private String name = "";
	private String catalog = "";
	private String schema = "";
	private List<UniqueConstraintDefinition> constraints = new ArrayList<UniqueConstraintDefinition>();

	public JpaTableModel() {
		super(Table.class.getSimpleName());
	}

	public JpaTableModel(UUID uuid) {
		super(Table.class.getSimpleName());
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedDbEntityDefinition dbEntityDefinition) {
		if (dbEntityDefinition != null && dbEntityDefinition.getTableDefinition() != null) {
			DbTableDefinition definition = dbEntityDefinition.getTableDefinition();
			name = definition.getName();
			catalog = definition.getCatalog();
			schema = definition.getSchema();
			constraints.clear();
			constraints.addAll(definition.getUniqueConstraints());
		}
	}

	@Override
	public JpaTableModel clone() {
		JpaTableModel model = new JpaTableModel(this.uuid);
		model.setName(name);
		model.setCatalog(catalog);
		model.setSchema(schema);
		List<UniqueConstraintDefinition> list = new ArrayList<UniqueConstraintDefinition>();
		for (UniqueConstraintDefinition constraint : constraints) {
			List<String> colNames = new ArrayList<String>();
			colNames.addAll(constraint.getColumnNames());
			list.add(new SimpleDbTableUniqueConstraintDefinition(colNames, constraint.getName()));
		}
		model.constraints = list;
		return model;
	}

	public boolean isModelEqual(JpaTableModel model) {
		boolean isConstraintsEqual = isConstraintsEqual(model.getConstraints());
		return StringUtils.equals(name, model.getName()) && StringUtils.equals(catalog, model.getCatalog())
				&& StringUtils.equals(schema, model.getSchema()) && isConstraintsEqual;
	}

	private boolean isConstraintsEqual(List<UniqueConstraintDefinition> modelConstrains) {
		if (constraints.size() != modelConstrains.size()) {
			return false;
		}
		for (int i = 0; i < constraints.size(); i++) {
			UniqueConstraintDefinition a = constraints.get(i);
			UniqueConstraintDefinition b = modelConstrains.get(i);
			if (!StringUtils.equals(a.getName(), b.getName())) {
				return false;
			}
			if (a.getColumnNames().size() != b.getColumnNames().size()) {
				return false;
			}
			for (int j = 0; j < a.getColumnNames().size(); j++) {
				if (!StringUtils.equals(a.getColumnNames().get(j), b.getColumnNames().get(j))) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isDefaultModel() {
		return StringUtils.isEmpty(name) && StringUtils.isEmpty(catalog) && StringUtils.isEmpty(schema) && constraints.isEmpty();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<UniqueConstraintDefinition> getConstraints() {
		return constraints;
	}

}
