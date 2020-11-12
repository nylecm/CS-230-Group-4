package security;

public interface Validator<T> {

   boolean isValid(T t);
}
