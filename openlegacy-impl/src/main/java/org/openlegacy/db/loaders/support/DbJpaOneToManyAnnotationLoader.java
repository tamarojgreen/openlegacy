package org.openlegacy.db.loaders.support;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.FieldType;
import org.openlegacy.annotations.db.DbColumn;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbOneToManyDefinition;
import org.openlegacy.loaders.FieldLoader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Component
public class DbJpaOneToManyAnnotationLoader implements FieldLoader {

	@Override
	public boolean match(EntitiesRegistry entitiesRegistry, Field field) {
		return field.getAnnotation(OneToMany.class) != null;
	}

	@Override
	public void load(EntitiesRegistry entitiesRegistry, Field field, Class<?> containingClass, int fieldOrder) {
		if (field.getAnnotation(DbColumn.class) == null) {
			return;
		}
		DbEntityDefinition dbEntityDefinition = (DbEntityDefinition)entitiesRegistry.get(containingClass);
		if (dbEntityDefinition != null) {
			DbFieldDefinition dbFieldDefinition = dbEntityDefinition.getColumnFieldsDefinitions().get(field.getName());
			if (dbFieldDefinition == null) {
				dbFieldDefinition = new SimpleDbColumnFieldDefinition(field.getName(), FieldType.General.class);
			}
			if (dbFieldDefinition instanceof SimpleDbColumnFieldDefinition) {
				OneToMany oneToMany = field.getAnnotation(OneToMany.class);
				SimpleDbColumnFieldDefinition columnFieldDefinition = (SimpleDbColumnFieldDefinition)dbFieldDefinition;
				SimpleDbOneToManyDefinition simpleDbOneToMany = new SimpleDbOneToManyDefinition();
				simpleDbOneToMany.setCascadeTypeNames(StringUtils.join(oneToMany.cascade(), ',').split(","));
				simpleDbOneToMany.setFetchTypeName(oneToMany.fetch().name());
				simpleDbOneToMany.setMappedBy(oneToMany.mappedBy());
				simpleDbOneToMany.setOrphanRemoval(oneToMany.orphanRemoval());
				simpleDbOneToMany.setTargetEntity(oneToMany.targetEntity());
				simpleDbOneToMany.setTargetEntityClassName(oneToMany.targetEntity().getSimpleName());
				JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
				if (joinColumn != null) {
					simpleDbOneToMany.setJoinColumnName(joinColumn.name());
				}

				columnFieldDefinition.setOneToManyDefinition(simpleDbOneToMany);
			}
			dbEntityDefinition.getColumnFieldsDefinitions().put(field.getName(), dbFieldDefinition);
		}
	}

}
