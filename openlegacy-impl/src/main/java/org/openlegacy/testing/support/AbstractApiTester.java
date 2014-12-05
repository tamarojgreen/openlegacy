package org.openlegacy.testing.support;

import org.junit.Assert;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.Session;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.testing.ApiReport;
import org.openlegacy.testing.ApiTester;
import org.openlegacy.testing.TestResult;
import org.openlegacy.testing.TestResult.TestStatus;

import java.text.MessageFormat;
import java.util.List;

public abstract class AbstractApiTester<R extends EntitiesRegistry<?, ?, ?>, S extends Session> implements ApiTester<S> {

	private boolean failOnError = false;

	// TODO
	private boolean assertFieldSampleValues = false;

	private boolean drilldownRecordsActions = true;

	// TODO
	private boolean invokeActions = false;

	private boolean enableUnknownEntities = false;

	private ApiReport apiReport = new ApiReport();

	@Override
	public ApiReport test(S session) {
		List<MenuItem> menus = session.getModule(Menu.class).getFlatMenuEntries();

		for (MenuItem menuItem : menus) {
			List<MenuItem> leafs = menuItem.getMenuItems();
			for (MenuItem leaf : leafs) {
				Object entity = null;
				try {
					entity = session.getEntity(leaf.getTargetEntity());
				} catch (Exception e) {
					// do nothing
				}

				addNotNullResult(
						MessageFormat.format("Entity {0} is null when navigating from menu", leaf.getTargetEntityName()), entity);
				addTrueResult(MessageFormat.format("Retured entity from session {0} doesnt match requested entity in leaf {1}",
						entity.getClass().getSimpleName()), MessageFormat.format("Target entity matched:{0}",
						leaf.getTargetEntityName()), leaf.getTargetEntity().isAssignableFrom(entity.getClass()));

				if (entity != null && drilldownRecordsActions) {
					drilldownRecordsActions(session, entity);
				}
			}
		}
		return apiReport;
	}

	protected abstract void drilldownRecordsActions(S session, Object entity);

	protected abstract R getEntitiesRegistry();

	protected void addNotNullResult(String failMessage, Object object) {
		if (object == null) {
			if (failOnError) {
				Assert.assertNotNull(failMessage, object);
			}
			apiReport.getTestsResults().add(new TestResult(TestStatus.FAIL, failMessage));
		}
	}

	protected void addTrueResult(String failMessage, String okMessage, boolean condition) {
		if (!condition) {
			if (failOnError) {
				Assert.fail(failMessage);
			}
			apiReport.getTestsResults().add(new TestResult(TestStatus.FAIL, failMessage));
		} else {
			apiReport.getTestsResults().add(new TestResult(TestStatus.PASS, okMessage));

		}
	}

	public boolean isAssertFieldSampleValues() {
		return assertFieldSampleValues;
	}

	public void setAssertFieldSampleValues(boolean assertFieldSampleValues) {
		this.assertFieldSampleValues = assertFieldSampleValues;
	}

	public boolean isDrilldownRecordsActions() {
		return drilldownRecordsActions;
	}

	public void setDrilldownRecordsActions(boolean drilldownRecordsActions) {
		this.drilldownRecordsActions = drilldownRecordsActions;
	}

	public boolean isEnableUnknownEntities() {
		return enableUnknownEntities;
	}

	public void setEnableUnknownEntities(boolean enableUnknownEntities) {
		this.enableUnknownEntities = enableUnknownEntities;
	}

	public boolean isInvokeActions() {
		return invokeActions;
	}

	public void setInvokeActions(boolean invokeActions) {
		this.invokeActions = invokeActions;
	}

}
