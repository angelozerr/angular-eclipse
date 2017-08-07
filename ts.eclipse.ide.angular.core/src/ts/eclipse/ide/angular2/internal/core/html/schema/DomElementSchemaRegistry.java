package ts.eclipse.ide.angular2.internal.core.html.schema;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ts.eclipse.ide.angular2.core.html.INgBindingCollector;
import ts.eclipse.ide.angular2.core.html.INgBindingType;
import ts.utils.StringUtils;

/**
 * 
 * @see https://github.com/angular/angular/blob/master/modules/@angular/compiler/src/schema/dom_element_schema_registry.ts
 */
public class DomElementSchemaRegistry implements IElementSchemaRegistry {

	private final static String[] SCHEMA = new String[] {
			"*|textContent,%classList,className,id,innerHTML,*beforecopy,*beforecut,*beforepaste,*copy,*cut,*paste,*search,*selectstart,*webkitfullscreenchange,*webkitfullscreenerror,*wheel,outerHTML,#scrollLeft,#scrollTop",
			"^*|accessKey,contentEditable,dir,!draggable,!hidden,innerText,lang,*abort,*autocomplete,*autocompleteerror,*beforecopy,*beforecut,*beforepaste,*blur,*cancel,*canplay,*canplaythrough,*change,*click,*close,*contextmenu,*copy,*cuechange,*cut,*dblclick,*drag,*dragend,*dragenter,*dragleave,*dragover,*dragstart,*drop,*durationchange,*emptied,*ended,*error,*focus,*input,*invalid,*keydown,*keypress,*keyup,*load,*loadeddata,*loadedmetadata,*loadstart,*message,*mousedown,*mouseenter,*mouseleave,*mousemove,*mouseout,*mouseover,*mouseup,*mousewheel,*mozfullscreenchange,*mozfullscreenerror,*mozpointerlockchange,*mozpointerlockerror,*paste,*pause,*play,*playing,*progress,*ratechange,*reset,*resize,*scroll,*search,*seeked,*seeking,*select,*selectstart,*show,*stalled,*submit,*suspend,*timeupdate,*toggle,*volumechange,*waiting,*webglcontextcreationerror,*webglcontextlost,*webglcontextrestored,*webkitfullscreenchange,*webkitfullscreenerror,*wheel,outerText,!spellcheck,%style,#tabIndex,title,!translate",
			"media|!autoplay,!controls,%crossOrigin,#currentTime,!defaultMuted,#defaultPlaybackRate,!disableRemotePlayback,!loop,!muted,*encrypted,#playbackRate,preload,src,#volume",
			":svg:^*|*abort,*autocomplete,*autocompleteerror,*blur,*cancel,*canplay,*canplaythrough,*change,*click,*close,*contextmenu,*cuechange,*dblclick,*drag,*dragend,*dragenter,*dragleave,*dragover,*dragstart,*drop,*durationchange,*emptied,*ended,*error,*focus,*input,*invalid,*keydown,*keypress,*keyup,*load,*loadeddata,*loadedmetadata,*loadstart,*mousedown,*mouseenter,*mouseleave,*mousemove,*mouseout,*mouseover,*mouseup,*mousewheel,*pause,*play,*playing,*progress,*ratechange,*reset,*resize,*scroll,*seeked,*seeking,*select,*show,*stalled,*submit,*suspend,*timeupdate,*toggle,*volumechange,*waiting,%style,#tabIndex",
			":svg:graphics^:svg:|", ":svg:animation^:svg:|*begin,*end,*repeat", ":svg:geometry^:svg:|",
			":svg:componentTransferFunction^:svg:|", ":svg:gradient^:svg:|", ":svg:textContent^:svg:graphics|",
			":svg:textPositioning^:svg:textContent|",
			"a|charset,coords,download,hash,host,hostname,href,hreflang,name,password,pathname,ping,port,protocol,referrerpolicy,rel,rev,search,shape,target,text,type,username",
			"area|alt,coords,hash,host,hostname,href,!noHref,password,pathname,ping,port,protocol,referrerpolicy,search,shape,target,username",
			"audio^media|", "br|clear", "base|href,target",
			"body|aLink,background,bgColor,link,*beforeunload,*blur,*error,*focus,*hashchange,*languagechange,*load,*message,*offline,*online,*pagehide,*pageshow,*popstate,*rejectionhandled,*resize,*scroll,*storage,*unhandledrejection,*unload,text,vLink",
			"button|!autofocus,!disabled,formAction,formEnctype,formMethod,!formNoValidate,formTarget,name,type,value",
			"canvas|#height,#width", "content|select", "dl|!compact", "datalist|", "details|!open",
			"dialog|!open,returnValue", "dir|!compact", "div|align", "embed|align,height,name,src,type,width",
			"fieldset|!disabled,name", "font|color,face,size",
			"form|acceptCharset,action,autocomplete,encoding,enctype,method,name,!noValidate,target",
			"frame|frameBorder,longDesc,marginHeight,marginWidth,name,!noResize,scrolling,src",
			"frameset|cols,*beforeunload,*blur,*error,*focus,*hashchange,*languagechange,*load,*message,*offline,*online,*pagehide,*pageshow,*popstate,*rejectionhandled,*resize,*scroll,*storage,*unhandledrejection,*unload,rows",
			"hr|align,color,!noShade,size,width", "head|", "h1,h2,h3,h4,h5,h6|align", "html|version",
			"iframe|align,!allowFullscreen,frameBorder,height,longDesc,marginHeight,marginWidth,name,referrerpolicy,%sandbox,scrolling,src,srcdoc,width",
			"img|align,alt,border,%crossOrigin,#height,#hspace,!isMap,longDesc,lowsrc,name,referrerpolicy,sizes,src,srcset,useMap,#vspace,#width",
			"input|accept,align,alt,autocapitalize,autocomplete,!autofocus,!checked,!defaultChecked,defaultValue,dirName,!disabled,%files,formAction,formEnctype,formMethod,!formNoValidate,formTarget,#height,!incremental,!indeterminate,max,#maxLength,min,#minLength,!multiple,name,pattern,placeholder,!readOnly,!required,selectionDirection,#selectionEnd,#selectionStart,#size,src,step,type,useMap,value,%valueAsDate,#valueAsNumber,#width",
			"keygen|!autofocus,challenge,!disabled,keytype,name", "li|type,#value", "label|htmlFor", "legend|align",
			"link|as,charset,%crossOrigin,!disabled,href,hreflang,integrity,media,rel,%relList,rev,%sizes,target,type",
			"map|name",
			"marquee|behavior,bgColor,direction,height,#hspace,#loop,#scrollAmount,#scrollDelay,!trueSpeed,#vspace,width",
			"menu|!compact", "meta|content,httpEquiv,name,scheme", "meter|#high,#low,#max,#min,#optimum,#value",
			"ins,del|cite,dateTime", "ol|!compact,!reversed,#start,type",
			"object|align,archive,border,code,codeBase,codeType,data,!declare,height,#hspace,name,standby,type,useMap,#vspace,width",
			"optgroup|!disabled,label", "option|!defaultSelected,!disabled,label,!selected,text,value",
			"output|defaultValue,%htmlFor,name,value", "p|align", "param|name,type,value,valueType", "picture|",
			"pre|#width", "progress|#max,#value", "q,blockquote,cite|",
			"script|!async,charset,%crossOrigin,!defer,event,htmlFor,integrity,src,text,type",
			"select|!autofocus,!disabled,#length,!multiple,name,!required,#selectedIndex,#size,value", "shadow|",
			"source|media,sizes,src,srcset,type", "span|", "style|!disabled,media,type", "caption|align",
			"th,td|abbr,align,axis,bgColor,ch,chOff,#colSpan,headers,height,!noWrap,#rowSpan,scope,vAlign,width",
			"col,colgroup|align,ch,chOff,#span,vAlign,width",
			"table|align,bgColor,border,%caption,cellPadding,cellSpacing,frame,rules,summary,%tFoot,%tHead,width",
			"tr|align,bgColor,ch,chOff,vAlign", "tfoot,thead,tbody|align,ch,chOff,vAlign", "template|",
			"textarea|autocapitalize,!autofocus,#cols,defaultValue,dirName,!disabled,#maxLength,#minLength,name,placeholder,!readOnly,!required,#rows,selectionDirection,#selectionEnd,#selectionStart,value,wrap",
			"title|text", "track|!default,kind,label,src,srclang", "ul|!compact,type", "unknown|",
			"video^media|#height,poster,#width", ":svg:a^:svg:graphics|", ":svg:animate^:svg:animation|",
			":svg:animateMotion^:svg:animation|", ":svg:animateTransform^:svg:animation|", ":svg:circle^:svg:geometry|",
			":svg:clipPath^:svg:graphics|", ":svg:cursor^:svg:|", ":svg:defs^:svg:graphics|", ":svg:desc^:svg:|",
			":svg:discard^:svg:|", ":svg:ellipse^:svg:geometry|", ":svg:feBlend^:svg:|", ":svg:feColorMatrix^:svg:|",
			":svg:feComponentTransfer^:svg:|", ":svg:feComposite^:svg:|", ":svg:feConvolveMatrix^:svg:|",
			":svg:feDiffuseLighting^:svg:|", ":svg:feDisplacementMap^:svg:|", ":svg:feDistantLight^:svg:|",
			":svg:feDropShadow^:svg:|", ":svg:feFlood^:svg:|", ":svg:feFuncA^:svg:componentTransferFunction|",
			":svg:feFuncB^:svg:componentTransferFunction|", ":svg:feFuncG^:svg:componentTransferFunction|",
			":svg:feFuncR^:svg:componentTransferFunction|", ":svg:feGaussianBlur^:svg:|", ":svg:feImage^:svg:|",
			":svg:feMerge^:svg:|", ":svg:feMergeNode^:svg:|", ":svg:feMorphology^:svg:|", ":svg:feOffset^:svg:|",
			":svg:fePointLight^:svg:|", ":svg:feSpecularLighting^:svg:|", ":svg:feSpotLight^:svg:|",
			":svg:feTile^:svg:|", ":svg:feTurbulence^:svg:|", ":svg:filter^:svg:|", ":svg:foreignObject^:svg:graphics|",
			":svg:g^:svg:graphics|", ":svg:image^:svg:graphics|", ":svg:line^:svg:geometry|",
			":svg:linearGradient^:svg:gradient|", ":svg:mpath^:svg:|", ":svg:marker^:svg:|", ":svg:mask^:svg:|",
			":svg:metadata^:svg:|", ":svg:path^:svg:geometry|", ":svg:pattern^:svg:|", ":svg:polygon^:svg:geometry|",
			":svg:polyline^:svg:geometry|", ":svg:radialGradient^:svg:gradient|", ":svg:rect^:svg:geometry|",
			":svg:svg^:svg:graphics|#currentScale,#zoomAndPan", ":svg:script^:svg:|type", ":svg:set^:svg:animation|",
			":svg:stop^:svg:|", ":svg:style^:svg:|!disabled,media,title,type", ":svg:switch^:svg:graphics|",
			":svg:symbol^:svg:|", ":svg:tspan^:svg:textPositioning|", ":svg:text^:svg:textPositioning|",
			":svg:textPath^:svg:textContent|", ":svg:title^:svg:|", ":svg:use^:svg:graphics|",
			":svg:view^:svg:|#zoomAndPan" };

	private static final String[] EMPTY_ARRAY = new String[0];

	public static final IElementSchemaRegistry INSTANCE = new DomElementSchemaRegistry();

	private enum PropertyType {
		EVENT, BOOLEAN, NUMBER, STRING, OBJECT;
	}

	private final Map<String, Map<String, PropertyType>> schema;

	private final Map<String, String> attrToPropMap;

	public DomElementSchemaRegistry() {
		this.schema = new HashMap<String, Map<String, PropertyType>>();
		for (String encodedType : SCHEMA) {
			String[] parts = encodedType.split("[|]");
			String[] properties = parts.length > 1 ? parts[1].split("[,]") : EMPTY_ARRAY;
			String[] typeParts = (parts[0] + "^").split("[\\^]");
			String typeName = typeParts[0];
			Map<String, PropertyType> type = new HashMap<String, DomElementSchemaRegistry.PropertyType>();
			for (String tag : typeName.split(",")) {
				this.schema.put(tag, type);
			}
			Map<String, PropertyType> superType = typeParts.length > 1 ? this.schema.get(typeParts[1])
					: this.schema.get("");
			if (isPresent(superType)) {
				for (Map.Entry<String, PropertyType> t : superType.entrySet()) {
					type.put(t.getKey(), t.getValue());
				}
			}
			for (String property : properties) {
				if (StringUtils.isEmpty(property)) {

				} else if (property.startsWith("*")) {
					// We don't yet support events.
					// If ever allowing to bind to events, GO THROUGH A SECURITY
					// REVIEW, allowing events will
					// almost certainly introduce bad XSS vulnerabilities.
					type.put(property.substring(1), PropertyType.EVENT);
				} else if (property.startsWith("!")) {
					type.put(property.substring(1), PropertyType.BOOLEAN);
				} else if (property.startsWith("#")) {
					type.put(property.substring(1), PropertyType.NUMBER);
				} else if (property.startsWith("%")) {
					type.put(property.substring(1), PropertyType.OBJECT);
				} else {
					type.put(property, PropertyType.STRING);
				}
			}
		}
		this.attrToPropMap = new HashMap<String, String>();
		attrToPropMap.put("class", "className");
		attrToPropMap.put("formaction", "formAction");
		attrToPropMap.put("innerHtml", "innerHTML");
		attrToPropMap.put("readonly", "readOnly");
		attrToPropMap.put("tabindex", "tabIndex");
	}

	private boolean isPresent(Object obj) {
		return obj != null;
	}

	@Override
	public boolean hasProperty(String tagName, String propName) {
		if (tagName.indexOf('-') != -1) {
			if (tagName.equals("ng-container") || tagName.equals("ng-content")) {
				return false;
			}

			// Can't tell now as we don't know which properties a custom element
			// will get
			// once it is instantiated
			return false;
		} else {
			PropertyType type = getType(tagName, propName);
			return type != null && type != PropertyType.EVENT;
		}
	}

	@Override
	public boolean hasEvent(String tagName, String eventName) {
		PropertyType type = getType(tagName, eventName);
		return type != null && type == PropertyType.EVENT;
	}

	private PropertyType getType(String tagName, String name) {
		Map<String, PropertyType> elementProperties = this.schema.get(tagName.toLowerCase());
		if (!isPresent(elementProperties)) {
			elementProperties = this.schema.get("unknown");
		}
		return elementProperties.get(name);
	}

	@Override
	public Object securityContext(String tagName, String propName) {
		return null;
	}

	@Override
	public String getMappedPropName(String propName) {
		String mappedPropName = attrToPropMap.get(propName);
		return isPresent(mappedPropName) ? mappedPropName : propName;
	}

	@Override
	public void collectProperty(String tagName, String attrName, INgBindingType bindingType,
			INgBindingCollector collector) {
		collect(tagName, attrName, bindingType, false, collector);
	}

	@Override
	public void collectEvent(String tagName, String attrName, INgBindingType bindingType,
			INgBindingCollector collector) {
		collect(tagName, attrName, bindingType, true, collector);
	}

	private void collect(String tagName, String attrName, INgBindingType bindingType, boolean event,
			INgBindingCollector collector) {
		Map<String, PropertyType> elementProperties = this.schema.get(tagName.toLowerCase());
		Set<Entry<String, PropertyType>> entries = elementProperties.entrySet();
		for (Entry<String, PropertyType> entry : entries) {
			if (isMatch(event, entry)) {
				collector.collect(attrName, entry.getKey(), null, bindingType);
			}
		}
	}

	private boolean isMatch(boolean event, Entry<String, PropertyType> entry) {
		PropertyType type = entry.getValue();
		return event ? type.equals(PropertyType.EVENT) : !type.equals(PropertyType.EVENT);
	}
}
