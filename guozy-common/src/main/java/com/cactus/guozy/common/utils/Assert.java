package com.cactus.guozy.common.utils;

import java.util.Collection;
import java.util.Map;

import static com.cactus.guozy.common.utils.Assert.ExceptionType.*;

/**
 * 断言工具，用来实现<a>
 * href="http://martinfowler.com/ieeeSoftware/failFast.pdf">Fail-Fast</a>。
 * <p>
 * <strong>注意事项：</strong>
 * </p>
 * <ul>
 * <li>Assertion是用来明确设置程序中的条件和约定的，它是一种程序员之间交流的工具，而不是和最终用户交流的工具。</li>
 * <li>一个经过完整单元测试的正确程序，不应该使任何一条assertion失败。</li>
 * <li>应避免过于复杂的assertion条件，而占用过多的运行时间。除非Assertion失败。</li>
 * <li>Assertion的出错信息不是给最终用户看的，因此没必要写得过于详细，更没必要考虑国际化的问题，以至于浪费CPU的时间。
 * 例如下面的语句是要避免的：
 * <p/>
 * <pre>
 * assertTrue(type instanceof MyType, &quot;Unsupported type: &quot; + type);
 * </pre>
 * <p/>
 * 可以替换成：
 * <p/>
 * <pre>
 * assertTrue(type instanceof MyType, &quot;Unsupported type: %s&quot;, type);
 * </pre>
 * <p/>
 * 这样，当assertion顺利通过时，不会占用CPU时间。</li>
 * </ul>
 * <p>
 * 此外，部分方法具有返回值，以方便编程，例如：
 * </p>
 * <p/>
 * <pre>
 * void foo(String param) {
 *     bar(assertNotNull(param));
 * }
 *
 * int bar(String param) {
 *     if (true) {
 *         ...
 *     }
 *
 *     return unreachableCode();
 * }
 * </pre>
 */
public final class Assert {
	
    /** 确保对象不为空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T notNull(T object) {
        return notNull(object, null, null, (Object[]) null);
    }

    /** 确保对象不为空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T notNull(T object, String message, Object... args) {
        return notNull(object, null, message, args);
    }

    /** 确保对象不为空，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static <T> T notNull(T object, ExceptionType exceptionType, String message, Object... args) {
        if (object == null) {
            if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - the argument is required; it must not be null"));
        }

        return object;
    }
    
    /** 确保对象为空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T isNull(T object) {
        return isNull(object, null, null, (Object[]) null);
    }

    /** 确保对象为空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T isNull(T object, String message, Object... args) {
        return isNull(object, null, message, args);
    }

    /** 确保对象为空，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static <T> T isNull(T object, ExceptionType exceptionType, String message, Object... args) {
        if (object != null) {
            if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - the object argument must be null"));
        }

        return object;
    }

    /** 确保表达式为真，否则抛出<code>IllegalArgumentException</code>。 */
    public static void isTrue(boolean expression) {
    	isTrue(expression, null, null, (Object[]) null);
    }

    /** 确保表达式为真，否则抛出<code>IllegalArgumentException</code>。 */
    public static void isTrue(boolean expression, String message, Object... args) {
    	isTrue(expression, null, message, args);
    }

    /** 确保表达式为真，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static void isTrue(boolean expression, ExceptionType exceptionType, String message, Object... args) {
        if (!expression) {
            if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - the expression must be true"));
        }
    }
    
    /** 确保文本长度不为零，否则抛出<code>IllegalArgumentException</code>。 */
    public static String hasLength(String text) {
    	return hasLength(text, null, null, (Object[]) null);
    }
    
    /** 确保文本长度不为零，否则抛出<code>IllegalArgumentException</code>。 */
    public static String hasLength(String text, String message, Object... args) {
    	return hasLength(text, null, message, args);
    }
    
    /** 确保文本长度不为零，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static String hasLength(String text, ExceptionType exceptionType, String message, Object... args) {
    	if (!Strings.hasLength(text)) {
    		if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - this String argument must have length;"));
		}
    	
    	return text;
    }
    
    /** 确保文本不全为空白，否则抛出<code>IllegalArgumentException</code>。 */
    public static String hasText(String text) {
    	return hasText(text, null, null, (Object[]) null);
    }
    
    /** 确保文本不全为空白，否则抛出<code>IllegalArgumentException</code>。 */
    public static String hasText(String text, String message, Object... args) {
    	return hasText(text, null, message, args);
    }
    
    /** 确保文本不全为空白，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static String hasText(String text, ExceptionType exceptionType, String message, Object... args) {
    	if (!Strings.hasText(text)) {
    		if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - this String argument must have text;"));
		}
    	
    	return text;
    }
    
    /** 确保文本不包含子串，否则抛出<code>IllegalArgumentException</code>。 */
    public static String doesNotContain(String textToSearch, String substring) {
    	return doesNotContain(textToSearch, substring, null, null, (Object[]) null);
    }
    
    /** 确保文本不包含子串，否则抛出<code>IllegalArgumentException</code>。 */
    public static String doesNotContain(String textToSearch, String substring, String message, Object... args) {
    	return doesNotContain(textToSearch, substring, null, message, args);
    }
    
    /** 确保文本不包含子串，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static String doesNotContain(String textToSearch, String substring, ExceptionType exceptionType, String message, Object... args) {
    	if (!Strings.hasLength(textToSearch) && Strings.hasLength(substring) && textToSearch.contains(substring)) {
    		if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - this String argument must not contain the substring"));
		}
    	
    	return textToSearch;
    }
    
    /** 确保数组不空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T[] notEmpty(T[] array) {
    	return notEmpty(array, null, null, (Object[]) null);
    }
    
    /** 确保数组不空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T[] notEmpty(T[] array, String message, Object... args) {
    	return notEmpty(array, null, message, args);
    }
    
    /** 确保数组不空，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static <T> T[] notEmpty(T[] array, ExceptionType exceptionType, String message, Object... args) {
    	if (array == null || array.length == 0) {
    		if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - this array must not be empty;"));
		}
    	
    	return array;
    }
    
    /** 确保集合不空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> Collection<T> notEmpty(Collection<T> collection) {
    	return notEmpty(collection, null, null, (Object[]) null);
    }
    
    /** 确保集合不空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> Collection<T> notEmpty(Collection<T> collection, String message, Object... args) {
    	return notEmpty(collection, null, message, args);
    }
    
    /** 确保集合不空，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static <T> Collection<T> notEmpty(Collection<T> collection, ExceptionType exceptionType, String message, Object... args) {
    	if (collection == null || collection.size() == 0) {
    		if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - this collection must not be empty;"));
		}
    	
    	return collection;
    }
    
    /** 确保Map不空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <K, V> Map<K, V> notEmpty(Map<K, V> map) {
    	return notEmpty(map, null, null, (Object[]) null);
    }
    
    /** 确保Map不空，否则抛出<code>IllegalArgumentException</code>。 */
    public static <K, V> Map<K, V> notEmpty(Map<K, V> map, String message, Object... args) {
    	return notEmpty(map, null, message, args);
    }
    
    /** 确保Map不空，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static <K, V> Map<K, V> notEmpty(Map<K, V> map, ExceptionType exceptionType, String message, Object... args) {
    	if (map == null || map.isEmpty()) {
    		if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - this map must not be empty;"));
		}
    	
    	return map;
    }
    
    /** 确保数组不含null元素，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T[] noNullElements(T[] array) {
    	return noNullElements(array, null, null, (Object[]) null);
    }
    
    /** 确保数组不含null元素，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T[] noNullElements(T[] array, String message, Object... args) {
    	return noNullElements(array, null, message, args);
    }
    
    /** 确保数组不含null元素，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static <T> T[] noNullElements(T[] array, ExceptionType exceptionType, String message, Object... args) {
    	if (array != null) {
			for (Object element : array) {
				if (element == null) {
					if (exceptionType == null) {
		                exceptionType = ILLEGAL_ARGUMENT;
		            }
					
					throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - this array must not contain any null elements;"));
				}
			}
		}
    	
    	return array;
    }
    
    /** 确保给定的对象是clazz的实例，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T isInstanceOf(Class<T> clazz, T obj) {
    	return isInstanceOf(clazz, obj, null, null, (Object[]) null);
    }
    
    /** 确保给定的对象是clazz的实例，否则抛出<code>IllegalArgumentException</code>。 */
    public static <T> T isInstanceOf(Class<T> clazz, T obj, String message, Object... args) {
    	return isInstanceOf(clazz, obj, null, message, args);
    }
    
    /** 确保给定的对象是clazz的实例，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static <T> T isInstanceOf(Class<T> clazz, T obj, ExceptionType exceptionType, String message, Object... args) {
    	notNull(clazz, "Type to check against must not be null");
		if (!clazz.isInstance(obj)) {
			if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - the Object is not an instance of the class;"));
		}
    	
    	return obj;
    }
    
    /** 确保subType是superType的子类，否则抛出<code>IllegalArgumentException</code>。 */
    public static void isAssignable(Class<?> superType, Class<?> subType) {
    	isAssignable(superType, subType, null, null, (Object[]) null);
    }

    /** 确保subType是superType的子类，否则抛出<code>IllegalArgumentException</code>。 */
    public static void isAssignable(Class<?> superType, Class<?> subType, String message, Object... args) {
    	isAssignable(superType, subType, null, message, args);
    }

    /** 确保subType是superType的子类，否则抛出指定异常，默认为<code>IllegalArgumentException</code>。 */
    public static void isAssignable(Class<?> superType, Class<?> subType, ExceptionType exceptionType, String message, Object... args) {
    	notNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			if (exceptionType == null) {
                exceptionType = ILLEGAL_ARGUMENT;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - subType is not assignable to superType"));			
		}
    }
    
    /** 断言表达式为真，否则抛出<code>IllegalStateException</code>。 */
    public static void state(boolean expression) {
    	state(expression, null, null, (Object[]) null);
    }

    /** 断言表达式为真，否则抛出<code>IllegalStateException</code>。 */
    public static void state(boolean expression, String message, Object... args) {
    	state(expression, null, message, args);
    }

    /** 断言表达式为真，否则抛出指定异常，默认为<code>IllegalStateException</code>。 */
    public static void state(boolean expression, ExceptionType exceptionType, String message, Object... args) {
    	if (!expression) {
    		if (exceptionType == null) {
                exceptionType = ILLEGAL_STATE;
            }

            throw exceptionType.newInstance(getMessage(message, args, "[Assertion failed] - this state invariant must be true"));	
		}
    }

    /** 不可能到达的代码。 */
    public static <T> T unreachableCode() {
        unreachableCode(null, (Object[]) null);
        return null;
    }

    /** 不可能到达的代码。 */
    public static <T> T unreachableCode(String message, Object... args) {
        throw UNREACHABLE_CODE.newInstance(getMessage(message, args, "[Assertion failed] - the code is expected as unreachable"));
    }

    /** 不可能发生的异常。 */
    public static <T> T unexpectedException(Throwable e) {
        unexpectedException(e, null, (Object[]) null);
        return null;
    }

    /** 不可能发生的异常。 */
    public static <T> T unexpectedException(Throwable e, String message, Object... args) {
        RuntimeException exception = UNEXPECTED_FAILURE.newInstance(getMessage(message, args, "[Assertion failed] - unexpected exception is thrown"));

        exception.initCause(e);

        throw exception;
    }

    /** 未预料的失败。 */
    public static <T> T fail() {
        fail(null, (Object[]) null);
        return null;
    }

    /** 未预料的失败。 */
    public static <T> T fail(String message, Object... args) {
        throw UNEXPECTED_FAILURE.newInstance(getMessage(message, args, "[Assertion failed] - unexpected failure"));
    }

    /** 不支持的操作。 */
    public static <T> T unsupportedOperation() {
        unsupportedOperation(null, (Object[]) null);
        return null;
    }

    /** 不支持的操作。 */
    public static <T> T unsupportedOperation(String message, Object... args) {
        throw UNSUPPORTED_OPERATION.newInstance(getMessage(message, args,
                                                           "[Assertion failed] - unsupported operation or unimplemented function"));
    }

    /** 取得带参数的消息。 */
    private static String getMessage(String message, Object[] args, String defaultMessage) {
        if (message == null) {
            message = defaultMessage;
        }

        if (args == null || args.length == 0) {
            return message;
        }

        return String.format(message, args);
    }

    /** Assertion错误类型。 */
    public static enum ExceptionType {
        ILLEGAL_ARGUMENT {
            @Override
            RuntimeException newInstance(String message) {
                return new IllegalArgumentException(message);
            }
        },

        ILLEGAL_STATE {
            @Override
            RuntimeException newInstance(String message) {
                return new IllegalStateException(message);
            }
        },

        NULL_POINT {
            @Override
            RuntimeException newInstance(String message) {
                return new NullPointerException(message);
            }
        },

        UNREACHABLE_CODE {
            @Override
            RuntimeException newInstance(String message) {
                return new UnreachableCodeException(message);
            }
        },

        UNEXPECTED_FAILURE {
            @Override
            RuntimeException newInstance(String message) {
                return new UnexpectedFailureException(message);
            }
        },

        UNSUPPORTED_OPERATION {
            @Override
            RuntimeException newInstance(String message) {
                return new UnsupportedOperationException(message);
            }
        };

        abstract RuntimeException newInstance(String message);
    }
}
