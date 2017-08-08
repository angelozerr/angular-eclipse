package ts.eclipse.ide.angular.core.hml;

import org.junit.Assert;
import org.junit.Test;

import ts.eclipse.ide.angular.internal.core.html.schema.DomElementSchemaRegistry;

public class DomElementSchemaRegistryTest {

	@Test
	public void propertyExists() {
		boolean result = DomElementSchemaRegistry.INSTANCE.hasProperty("button", "value");
		Assert.assertTrue(result);
	}

	@Test
	public void propertyNotExists() {
		boolean result = DomElementSchemaRegistry.INSTANCE.hasProperty("button", "click");
		Assert.assertFalse(result);
	}

	@Test
	public void eventExists() {
		boolean result = DomElementSchemaRegistry.INSTANCE.hasEvent("button", "click");
		Assert.assertTrue(result);
	}

	@Test
	public void eventNotExists() {
		boolean result = DomElementSchemaRegistry.INSTANCE.hasEvent("button", "value");
		Assert.assertFalse(result);
	}

	@Test
	public void propertyTextContentExists() {
		boolean result = DomElementSchemaRegistry.INSTANCE.hasProperty("button", "textContent");
		Assert.assertTrue(result);
	}
}
