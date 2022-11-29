package com.august.soil.api.configure.support;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.apache.commons.lang3.math.NumberUtils.toInt;

@RequiredArgsConstructor
public class SimpleOffsetPageableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
  
  private static final int DEFAULT_OFFSET = 0;
  
  private static final int DEFAULT_LIMIT = 10;
  
  private final String offsetParam;
  
  private final String limitParam;
  
  public SimpleOffsetPageableHandlerMethodArgumentResolver() {
    this("offset", "limit");
  }
  
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Pageable.class.isAssignableFrom(parameter.getParameterType());
  }
  
  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    String offsetString = webRequest.getParameter(offsetParam);
    String limitString = webRequest.getParameter(limitParam);
  
    int offset = toInt(offsetString, DEFAULT_OFFSET);
    int limit = toInt(limitString, DEFAULT_LIMIT);
  
    if (offset < 0) {
      offset = DEFAULT_OFFSET;
    }
  
    if (limit < 1 || limit > 10) {
      limit = DEFAULT_LIMIT;
    }
  
    return new SimpleOffsetPageRequest(offset, limit);
  }
}
