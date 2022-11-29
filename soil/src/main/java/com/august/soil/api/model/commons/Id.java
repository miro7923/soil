package com.august.soil.api.model.commons;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**
 * DB 테이블 내 PK로 보통 Long 타입의 숫자를 쓰게 되는데 이걸 그냥 Long id;라고 선언해 두면
 * 메서드의 인자값으로 id값을 넣어줄 때 어느 객체의 id인지 헷갈리는 경우가 많다.
 * 그래서 제네릭을 통해 클래스 타입과 id값을 담을 데이터타입을 명시적으로 선언해 주면 코드상에서 어느 객체의 id를 전달하고자 하는지 명확하게 알 수 있다.
 * @param <R> 클래스 타입(ex. User.class)
 * @param <V> 데이터 타입(ex. Long) - Primitive 타입이 아닌 Wrapper 타입을 넣을 것
 */
@AllArgsConstructor
public class Id<R, V> {

  private final Class<R> reference;

  private final V value;
  
  /**
   * Id<R, V> 객체를 만들어주는 메서드로 보다 편하게 사용할 수 있게 static 메서드로 선언
   * 객체 생성 없이 Id.of()로 호출할 수 있다.
   * @param reference 클래스타입
   * @param value 데이터타입 값
   * @return Id<R, V> 객체(ex. Id<User, Long>)
   */
  public static <R, V> Id<R, V> of(Class<R> reference, V value) {
    checkArgument(reference != null, "reference must be provided.");
    checkArgument(value != null, "value must be provided.");

    return new Id<>(reference, value);
  }

  public V getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Id<?, ?> id = (Id<?, ?>) o;
    return Objects.equals(reference, id.reference) && Objects.equals(value, id.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference, value);
  }

  @Override
  public String toString() {
    return reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
