package com.august.soil.api.configure.support;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.builder.ToStringBuilder.*;

public class SimpleOffsetPageRequest implements Pageable {
  
  private final int offset;
  
  private final int limit;
  
  public SimpleOffsetPageRequest() {
    this(0, 10);
  }
  
  public SimpleOffsetPageRequest(int offset, int limit) {
    checkArgument(offset >= 0, "Offset must be greater or equals to zero.");
    checkArgument(limit >= 1, "Limit must be greater than zero.");
  
    this.offset = offset;
    this.limit = limit;
  }
  
  @Override
  public int offset() {
    return offset;
  }
  
  @Override
  public int limit() {
    return limit;
  }
  
  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
