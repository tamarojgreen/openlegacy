package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.DbOneToManyDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * @author Ivan Bort
 * 
 */
public class JpaListFieldModel extends JpaFieldModel {

	// @OneToMany attributes
	private CascadeType[] cascade = {};
	private FetchType fetch = FetchType.LAZY;
	private String mappedBy = "";
	private boolean orphanRemoval = false;
	private Class<?> targetEntity = void.class;
	private String targetEntityClassName = void.class.getSimpleName();
	// other
	private String fieldTypeArgs = Object.class.getSimpleName();
	private Class<?> fieldTypeArgsClass = Object.class;

	public JpaListFieldModel(NamedObject parent) {
		super(parent);
		this.javaTypeName = Messages.getString("type.list");//$NON-NLS-1$
	}

	public JpaListFieldModel(UUID uuid, NamedObject parent) {
		super(uuid, parent);
		this.javaTypeName = Messages.getString("type.list");//$NON-NLS-1$
	}

	@Override
	public void init(DbFieldDefinition dbFieldDefinition) {
		super.init(dbFieldDefinition);
		if (dbFieldDefinition instanceof SimpleDbColumnFieldDefinition) {
			fieldTypeArgs = ((SimpleDbColumnFieldDefinition)dbFieldDefinition).getFieldTypeArgs();
		}
		DbOneToManyDefinition oneToManyDefinition = dbFieldDefinition.getOneToManyDefinition();
		if (oneToManyDefinition != null) {
			if (oneToManyDefinition.getCascadeTypeNames().length > 0) {
				List<CascadeType> list = new ArrayList<CascadeType>();
				for (String cascadeName : oneToManyDefinition.getCascadeTypeNames()) {
					list.add(CascadeType.valueOf(cascadeName));
				}
				cascade = list.toArray(new CascadeType[] {});
			}
			if (!StringUtils.isEmpty(oneToManyDefinition.getFetchTypeName())) {
				fetch = FetchType.valueOf(oneToManyDefinition.getFetchTypeName());
			}
			mappedBy = oneToManyDefinition.getMappedBy();
			orphanRemoval = oneToManyDefinition.isOrphanRemoval();
			if (!StringUtils.isEmpty(oneToManyDefinition.getTargetEntityClassName())) {
				targetEntityClassName = oneToManyDefinition.getTargetEntityClassName();
			}
		}
	}

	@Override
	public JpaFieldModel clone() {
		JpaListFieldModel model = new JpaListFieldModel(uuid, parent);
		model.setModelName(this.getModelName());
		model.setFieldName(this.getFieldName());
		model.previousFieldName = this.previousFieldName;
		model.javaTypeName = this.javaTypeName;
		model.setName(getName());
		model.setUnique(isUnique());
		model.setNullable(isNullable());
		model.setInsertable(isInsertable());
		model.setUpdatable(isUpdatable());
		model.setColumnDefinition(getColumnDefinition());
		model.setTable(getTable());
		model.setLength(getLength());
		model.setPrecision(getPrecision());
		model.setScale(getScale());
		model.setKey(isKey());

		model.setDisplayName(getDisplayName());
		model.setEditable(isEditable());
		model.setPassword(isPassword());
		model.setSampleValue(getSampleValue());
		model.setDefaultValue(getDefaultValue());
		model.setHelpText(getHelpText());
		model.setRightToLeft(isRightToLeft());
		model.setInternal(isInternal());
		model.setMainDisplayFiled(isMainDisplayFiled());

		model.initialized = isInitialized();
		// List specific
		model.setFieldTypeArgs(fieldTypeArgs);
		// @OneToMany attributes
		model.setCascade(Arrays.copyOf(cascade, cascade.length));
		model.setFetch(fetch);
		model.setMappedBy(mappedBy);
		model.setOrphanRemoval(orphanRemoval);
		model.setTargetEntity(targetEntity);
		model.setTargetEntityClassName(targetEntityClassName);
		return model;
	}

	public boolean isDefaultOneToManyAttrs() {
		return cascade.length == 0
				&& FetchType.LAZY.equals(fetch)
				&& StringUtils.isEmpty(mappedBy)
				&& !orphanRemoval
				&& (void.class.getSimpleName().equalsIgnoreCase(targetEntityClassName) || StringUtils.isEmpty(targetEntityClassName));
	}

	public boolean equalsOneToManyAttrs(JpaFieldModel model) {
		if (model instanceof JpaListFieldModel) {
			JpaListFieldModel listModel = (JpaListFieldModel)model;
			boolean cascadeEqual = isCascadeEqual(listModel.getCascade());
			return cascadeEqual && fetch.equals(listModel.getFetch()) && StringUtils.equals(mappedBy, listModel.getMappedBy())
					&& orphanRemoval == listModel.isOrphanRemoval()
					&& StringUtils.equals(targetEntityClassName, listModel.getTargetEntityClassName());
		}
		return super.equalsColumnAttrs(model);
	}

	private boolean isCascadeEqual(CascadeType[] modelCascade) {
		if (cascade.length != modelCascade.length) {
			return false;
		}
		for (int i = 0; i < cascade.length; i++) {
			if (!StringUtils.equals(cascade[i].toString(), modelCascade[i].toString())) {
				return false;
			}
		}
		return true;
	}

	public CascadeType[] getCascade() {
		return cascade;
	}

	public void setCascade(CascadeType[] cascade) {
		this.cascade = cascade;
	}

	public FetchType getFetch() {
		return fetch;
	}

	public void setFetch(FetchType fetch) {
		this.fetch = fetch;
	}

	public String getMappedBy() {
		return mappedBy;
	}

	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

	public boolean isOrphanRemoval() {
		return orphanRemoval;
	}

	public void setOrphanRemoval(boolean orphanRemoval) {
		this.orphanRemoval = orphanRemoval;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

	public String getFieldTypeArgs() {
		return fieldTypeArgs;
	}

	public void setFieldTypeArgs(String fieldTypeArgs) {
		this.fieldTypeArgs = fieldTypeArgs;
	}

	public Class<?> getFieldTypeArgsClass() {
		return fieldTypeArgsClass;
	}

	public void setFieldTypeArgsClass(Class<?> fieldTypeArgsClass) {
		this.fieldTypeArgsClass = fieldTypeArgsClass;
	}

}
