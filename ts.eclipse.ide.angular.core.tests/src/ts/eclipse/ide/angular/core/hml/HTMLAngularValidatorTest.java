/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package ts.eclipse.ide.angular.core.hml;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.html.core.internal.validation.HTMLValidator;
import org.eclipse.wst.html.core.internal.validation.LocalizedMessage;
import org.eclipse.wst.sse.core.internal.validate.ValidationMessage;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.junit.Assert;
import org.junit.Test;

import ts.utils.IOUtils;

/**
 * Test for HTML Angular custom validator to ignore errors and add some errors
 * like syntax of binding property/event.
 *
 */
public class HTMLAngularValidatorTest {

	@Test
	public void domPropertyBinding() throws CoreException {
		// 'value' property binding is valid
		List messages = validate("<input [value]='' />");
		Assert.assertEquals(0, messages.size());
		messages = validate("<input bind-value='' />");
		Assert.assertEquals(0, messages.size());

		// 'xxx' property binding is NOT valid
		messages = validate("<input [xxx]='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng property binding 'xxx'.", 7, 5,
				ValidationMessage.WARNING);
		messages = validate("<input bind-xxx='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng property binding 'xxx'.", 7, 8,
				ValidationMessage.WARNING);
	}

	@Test
	public void domBindingSyntax() throws CoreException {

		List messages = validate("<input [value='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0),
				"Binding syntax error: the attribute '[value' must be closed with ']'.", 7, 6, ValidationMessage.ERROR);

		messages = validate("<input (value='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0),
				"Binding syntax error: the attribute '(value' must be closed with ')'.", 7, 6, ValidationMessage.ERROR);

		messages = validate("<input [(value='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0),
				"Binding syntax error: the attribute '[(value' must be closed with ')]'.", 7, 7,
				ValidationMessage.ERROR);
	}

	@Test
	public void specialDomPropertyBinding() throws CoreException {
		// 'textContent' property binding is valid
		List messages = validate("<input [textContent]='' />");
		Assert.assertEquals(0, messages.size());
		messages = validate("<input bind-textContent='' />");
		Assert.assertEquals(0, messages.size());
		
		// 'attr.*', 'class.*', 'style.*'
		// https://angular.io/docs/ts/latest/guide/template-syntax.html#!#attribute-binding
		messages = validate("<td [attr.colspan]=\"1 + 1\">One-Two</td>");
		Assert.assertEquals(0, messages.size());
		// https://angular.io/docs/ts/latest/guide/template-syntax.html#!#class-binding
		messages = validate("<div [class.special]=\"isSpecial\">The class binding is special</div>");
		Assert.assertEquals(0, messages.size());
		// https://angular.io/docs/ts/latest/guide/template-syntax.html#!#style-binding
		messages = validate("<button [style.color] = \"isSpecial ? 'red': 'green'\">Red</button>");
		Assert.assertEquals(0, messages.size());
	}

	@Test
	public void domEventBinding() throws CoreException {
		// 'click' event binding is valid
		List messages = validate("<input (click)='' />");
		Assert.assertEquals(0, messages.size());
		messages = validate("<input on-click='' />");
		Assert.assertEquals(0, messages.size());

		// 'xxx' event binding is NOT valid
		messages = validate("<input (xxx)='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng event binding 'xxx'.", 7, 5,
				ValidationMessage.WARNING);
		messages = validate("<input on-xxx='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng event binding 'xxx'.", 7, 6,
				ValidationMessage.WARNING);
	}

	@Test
	public void domPropertyAndEventBinding() throws CoreException {
		// 'value' property binding is valid
//		List messages = validate("<input [(value)]='' />");
//		Assert.assertEquals(0, messages.size());
//		messages = validate("<input bindon-value='' />");
//		Assert.assertEquals(0, messages.size());

		// 'xxx' property binding is NOT valid
		List messages = validate("<input [(xxx)]='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng property/event binding 'xxx'.", 7, 7,
				ValidationMessage.WARNING);
		messages = validate("<input bindon-xxx='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng property/event binding 'xxx'.", 7, 10,
				ValidationMessage.WARNING);
	}

	@Test
	public void letBinding() throws CoreException {
		List messages = validate("<template let-x />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input let-x />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "\"let-\" is only supported on template elements.", 7, 5,
				ValidationMessage.ERROR);
	}

	@Test
	public void varBinding() throws CoreException {
		List messages = validate("<input var--x />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "\"-\" is not allowed in variable names", 7, 6,
				ValidationMessage.ERROR);

		messages = validate("<template var-x />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0),
				"\"var-\" on <template> elements is deprecated. Use \"let-\" instead!", 10, 5,
				ValidationMessage.WARNING);

		messages = validate("<input var-x />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0),
				"\"var-\" on non <template> elements is deprecated. Use \"ref-\" instead!", 7, 5,
				ValidationMessage.WARNING);

	}

	@Test
	public void domKeyEventBinding() throws CoreException {
		// keyup
		List messages = validate("<input (keyup)='' />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input (keyup.enter)='' />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input (keyup.control.space)='' />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input (keyup.a)='' />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input (keyup.aa)='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined key event part 'aa'.", 14, 2,
				ValidationMessage.WARNING);

		messages = validate("<input (keyup.)='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng event binding 'keyup.'.", 7, 8,
				ValidationMessage.WARNING);

		// keydown
		messages = validate("<input (keydown)='' />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input (keydown.enter)='' />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input (keydown.control.space)='' />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input (keydown.a)='' />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input (keydown.aa)='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined key event part 'aa'.", 16, 2,
				ValidationMessage.WARNING);

		messages = validate("<input (keydown.)='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng event binding 'keydown.'.", 7, 10,
				ValidationMessage.WARNING);

		// keydown
		messages = validate("<input (keypress.enter)='' />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "Undefined ng event binding 'keypress.enter'.", 7, 16,
				ValidationMessage.WARNING);
	}

	@Test
	public void refBinding() throws CoreException {
		List messages = validate("<input ref-x />");
		Assert.assertEquals(0, messages.size());

		messages = validate("<input ref--x />");
		Assert.assertEquals(1, messages.size());
		assertMessage((LocalizedMessage) messages.get(0), "\"-\" is not allowed in reference names", 7, 6,
				ValidationMessage.ERROR);
	}

	private void assertMessage(LocalizedMessage message, String msg, int offset, int length, int severity) {
		Assert.assertNotNull(message);
		Assert.assertEquals(msg, message.getLocalizedMessage());
		Assert.assertEquals(offset, message.getOffset());
		Assert.assertEquals(length, message.getLength());
		Assert.assertEquals(severity, message.getSeverity());
	}

	private List validate(String html) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
		if (!project.exists()) {
			project.create(new NullProgressMonitor());
			project.open(new NullProgressMonitor());
		}
		// Add tsconfig.json in order to the project has angular nature
		IFile tsconfigJson = project.getFile("tsconfig.json");
		if (!tsconfigJson.exists()) {
			tsconfigJson.create(IOUtils.toInputStream("{}"), 1, new NullProgressMonitor());
		}
		IFile file = project.getFile("test.html");
		if (file.exists()) {
			file.setContents(IOUtils.toInputStream(html), 1, new NullProgressMonitor());
		} else {
			file.create(IOUtils.toInputStream(html), 1, new NullProgressMonitor());
		}

		HTMLValidator validator = new HTMLValidator();
		ValidationResult result = validator.validate(file, 1, new ValidationState(), new NullProgressMonitor());
		IReporter reporter = result.getReporter(new NullProgressMonitor());
		return reporter.getMessages();

	}
}
