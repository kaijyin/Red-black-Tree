package RedblackTree;

/**
 * @auther kaij_yin@163.com
 * @create 2021-08-30-13:21
 * @Description:
 */
interface Map<KeyType,ValueType>{

    boolean put(KeyType key,ValueType value);
    boolean remove(KeyType key);
    ValueType get(KeyType key);
    Integer size();
}