package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa.JpaEntityUtils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.net.MalformedURLException;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * @author Ivan Bort
 * 
 */
public class ModelUpdater {

	public static void updateJpaEntityModel(JpaEntity entity, JpaEntityModel model, String key, String text) {
		if (text != null) {
			if (key.equals(DbAnnotationConstants.NAME)) {
				model.setName(text);
			}
		}
		JpaEntityUtils.ActionGenerator.generateJpaEntityActions(entity, model);
	}

	public static void updateJpaTableModel(JpaEntity entity, JpaTableModel model, String key, String text) {
		if (text != null) {
			if (key.equals(DbAnnotationConstants.CATALOG)) {
				model.setCatalog(text);
			} else if (key.equals(DbAnnotationConstants.NAME)) {
				model.setName(text);
			} else if (key.equals(DbAnnotationConstants.SCHEMA)) {
				model.setSchema(text);
			}
		}
		JpaEntityUtils.ActionGenerator.generateJpaTableActions(entity, model);
	}

	public static void updateJpaFieldModel(JpaEntity entity, JpaFieldModel model, String key, String text, Boolean selection) {
		if (selection != null) {
			if (key.equals(DbAnnotationConstants.UNIQUE)) {
				model.setUnique(selection);
			} else if (key.equals(DbAnnotationConstants.NULLABLE)) {
				model.setNullable(selection);
			} else if (key.equals(DbAnnotationConstants.INSERTABLE)) {
				model.setInsertable(selection);
			} else if (key.equals(DbAnnotationConstants.UPDATABLE)) {
				model.setUpdatable(selection);
			} else if (key.equals(DbAnnotationConstants.DB_ID_ANNOTATION)) {
				model.setKey(selection);
			}
		}
		if (text != null) {
			if (key.equals(DbAnnotationConstants.NAME)) {
				model.setName(text);
			} else if (key.equals(DbAnnotationConstants.COLUMN_DEFINITION)) {
				model.setColumnDefinition(text);
			} else if (key.equals(DbAnnotationConstants.TABLE)) {
				model.setTable(text);
			} else if (key.equals(DbAnnotationConstants.LENGTH)) {
				model.setLength(StringUtils.isEmpty(text) ? 0 : Integer.valueOf(text));
			} else if (key.equals(DbAnnotationConstants.PRECISION)) {
				model.setPrecision(StringUtils.isEmpty(text) ? 0 : Integer.valueOf(text));
			} else if (key.equals(DbAnnotationConstants.SCALE)) {
				model.setScale(StringUtils.isEmpty(text) ? 0 : Integer.valueOf(text));
			} else if (key.equals(Constants.FIELD_NAME)) {
				model.setFieldName(StringUtils.uncapitalize(text));
			}
		}
		JpaEntityUtils.ActionGenerator.generateJpaFieldActions(entity, model);
	}

	public static void updateJpaListFieldModel(JpaEntity entity, JpaListFieldModel model, String key, String text,
			Boolean selection, String fullyQualifiedName) throws MalformedURLException, CoreException {
		if (selection != null) {
			if (key.equals(DbAnnotationConstants.ORPHAN_REMOVAL)) {
				model.setOrphanRemoval(selection);
			}
		}
		if (text != null) {
			if (key.equals(DbAnnotationConstants.CASCADE)) {
				model.setCascade(StringUtils.isEmpty(text) ? new CascadeType[] {}
						: new CascadeType[] { CascadeType.valueOf(text) });
			} else if (key.equals(DbAnnotationConstants.FETCH)) {
				model.setFetch(StringUtils.isEmpty(text) ? FetchType.LAZY : FetchType.valueOf(text));
			} else if (key.equals(DbAnnotationConstants.MAPPED_BY)) {
				model.setMappedBy(text);
			} else if (key.equals(DbAnnotationConstants.TARGET_ENTITY)) {
				model.setTargetEntityClassName(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setTargetEntity(clazz);
				}
			} else if (key.equals(Constants.LIST_TYPE_ARG)) {
				model.setFieldTypeArgs(text);
				if (fullyQualifiedName != null) {
					Class<?> clazz = Utils.getClazz(fullyQualifiedName);
					model.setFieldTypeArgsClass(clazz);
				}
			}
		}
		JpaEntityUtils.ActionGenerator.generateJpaListFieldActions(entity, model);
	}

}
