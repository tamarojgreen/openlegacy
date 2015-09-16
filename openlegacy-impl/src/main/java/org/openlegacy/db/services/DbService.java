package org.openlegacy.db.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Transactional
public class DbService {

	private final static Log logger = LogFactory.getLog(DbService.class);

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	transient EntityManager entityManager;

	public Object createOrUpdateEntity(Object entity) {
		return entityManager.merge(entity);
	}

	@SuppressWarnings("rawtypes")
	public Object getEntitiesPerPage(Class<?> entityClass, int pageSize, int pageNumber) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(entityClass)));
		Long count = entityManager.createQuery(countQuery).getSingleResult();
		Query query = entityManager.createQuery(String.format("FROM %s", entityClass.getSimpleName()));
		int pageCount = (int) Math.ceil((count.intValue() * 1.0) / pageSize);
		if (pageNumber > pageCount || pageNumber <= 0) {
			pageNumber = 1;
		}

		if (pageNumber == 1) {
			query.setFirstResult(0);
		} else if (pageNumber <= pageCount) {
			query.setFirstResult(pageSize * (pageNumber - 1));
		}

		query.setMaxResults(pageSize);
		List res = query.getResultList();
		TableDbObject tableDbObject = new TableDbObject(res, pageCount);
		return tableDbObject;
	}

	public Object getEntitiesWithConditions(Class<?> entityClass, Map<String, String> queryConditions) {
		String query = "FROM " + entityClass.getSimpleName() + " WHERE ";
		boolean firstIteration = true;
		for (Entry<String, String> entry : queryConditions.entrySet()) {
			if (!firstIteration) {
				query += " AND ";
			} else {
				firstIteration = false;
			}
			query += entry.getKey() + "=" + entry.getValue();
		}
		return entityManager.createQuery(query).getResultList();
	}

	public Object getEntityById(Class<?> entityClass, Object primaryKey) {
		return entityManager.find(entityClass, primaryKey);
	}

	public void deleteEntityById(Class<?> entityClass, Object id) throws EntityNotFoundException, Exception {
		try {
			Object rootEntity = entityManager.getReference(entityClass, id);
			entityManager.remove(rootEntity);
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public void deleteEntityByIdWithParent(Class<?> entityClass, Object id, Class<?> parentClass, Object parentId)
			throws EntityNotFoundException, Exception {
		Object entity = entityManager.find(entityClass, id);
		Object parent = entityManager.find(parentClass, parentId);
		Field[] parentFields = parentClass.getDeclaredFields();
		for (Field field : parentFields) {
			Type genericFieldType = field.getGenericType();

			if (genericFieldType instanceof ParameterizedType) {
				ParameterizedType aType = (ParameterizedType) genericFieldType;
				Type[] fieldArgTypes = aType.getActualTypeArguments();
				Class<?> fieldParameterizedType = (Class<?>) fieldArgTypes[fieldArgTypes.length - 1];

				if (entity.getClass().getSimpleName().equals(fieldParameterizedType.getSimpleName())) {
					field.setAccessible(true);
					try {
						Object listValue = field.get(parent);
						if (listValue instanceof List) {
							((List<?>) listValue).remove(entity);
						}
						entityManager.merge(parent);
						entityManager.remove(entity);
					} catch (IllegalArgumentException e) {
						logger.error(e.getMessage(), e);
					} catch (IllegalAccessException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	public class TableDbObject {

		private int pageCount = 1;
		private Object result;

		public TableDbObject(Object result, int pageCount) {
			this.pageCount = pageCount;
			this.result = result;
		}

		public int getPageCount() {
			return pageCount;
		}

		public Object getResult() {
			return result;
		}
	}
}
