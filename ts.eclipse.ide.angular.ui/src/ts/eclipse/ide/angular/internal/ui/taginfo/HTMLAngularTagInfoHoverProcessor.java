package ts.eclipse.ide.angular.internal.ui.taginfo;

import org.eclipse.core.resources.IFile;

import ts.eclipse.ide.ui.hover.TypeScriptHover;
import ts.utils.FileUtils;

public class HTMLAngularTagInfoHoverProcessor extends TypeScriptHover {

	@Override
	protected String getFileExtension(IFile tsFile) {
		return FileUtils.TS_EXTENSION;
	}
}
