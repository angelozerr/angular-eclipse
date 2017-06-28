package ts.eclipse.ide.angular2.internal.ui.taginfo;

import org.eclipse.core.resources.IFile;

import ts.eclipse.ide.ui.hover.TypeScriptHover;
import ts.utils.FileUtils;

public class HTMLAngular2TagInfoHoverProcessor extends TypeScriptHover {

	@Override
	protected String getFileExtension(IFile tsFile) {
		return FileUtils.TS_EXTENSION;
	}
}
