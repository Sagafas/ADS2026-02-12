package by.it.group510901.petsevich.lesson12;

import java.util.*;

/*
toString()
put(Integer, String)
remove(Integer)
get(Integer)
containsKey(Integer)

size()
clear()
isEmpty()
*/

public class MyAvlMap implements Map<Integer, String> {
    AvlTreeNode<Integer, String> root;
    private int size;

    MyAvlMap() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(root, (Integer)key) != null;
    }

    @Override
    public String get(Object key) {
        AvlTreeNode<Integer, String> node = find(root, (Integer)key);
        if(node == null) return null;
        return node.value;
    }

    private AvlTreeNode<Integer, String> find(AvlTreeNode<Integer, String> node, Integer key) {
        if(node == null)
            return null;

        if(Objects.equals(node.key, key))
            return node;

        if(key < node.key)
            return find(node.left, key);
        return find(node.right, key);
    }

    @Override
    public String put(Integer key, String value) {
        if (value == null || key == null)
            throw new NullPointerException();

        Result<Integer, String> result = insert(root, key, value);
        root = result.node;
        if(result.oldValue == null)
            size++;
        return result.oldValue;
    }

    private Result<Integer, String> insert(AvlTreeNode<Integer, String> node, Integer key, String value) {
        if(node == null)
            return new Result<>(new AvlTreeNode<>(key, value),  null);

        Result<Integer, String> result = null;
        if(node.key > key) {
            result = insert(node.left, key, value);
            node.left = result.node;
        }
        else if(node.key < key) {
            result = insert(node.right, key, value);
            node.right = result.node;
        }
        else {
            String oldValue = node.value;
            node.value = value;
            return new Result<>(node, oldValue);
        }

        node = rebalance(node);
        return new Result<>(node, result.oldValue);
    }

    @Override
    public String remove(Object key) {
       Result<Integer, String> result = delete(root, key);
       if(result.oldValue != null)
           size--;
       return result.oldValue;
    }

    private Result<Integer, String> delete(AvlTreeNode<Integer, String> node, Object key) {
        if(node == null)
            return new Result<>(null, null);

        Result<Integer, String> result = null;
        if(node.key > (Integer)key) {
            result = delete(node.left, key);
            node.left = result.node;
        }
        else if(node.key < (Integer)key) {
            result = delete(node.right, key);
            node.right = result.node;
        }
        else {
            String oldValue = node.value;
            if(node.left == null)
               return new Result<>(node.right, oldValue);

            if(node.right == null)
                return new Result<>(node.left, oldValue);

            AvlTreeNode<Integer, String> minNode = getMinNode(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            result = delete(node.right, node.key);
            result.oldValue = oldValue;
            node.right = result.node;
        }

        if(result.oldValue == null)
            return new Result<>(node, null);

        node = rebalance(node);
        return new Result<>(node, result.oldValue);
    }

    private AvlTreeNode<Integer, String> getMinNode(AvlTreeNode<Integer, String> node) {
        while(node.left != null)
            node = node.left;
        return node;
    }

    private AvlTreeNode<Integer, String> rebalance(AvlTreeNode<Integer, String> x) {
        updateHeight(x);
        int balance = getBalanceFactor(x);
        if(balance > 1) {
            if(getBalanceFactor(x.left) < 0)
                return rotateLeftRight(x);
            return rotateRight(x);
        }

        if(balance < -1) {
            if(getBalanceFactor(x.right) > 0)
                return rotateRightLeft(x);
            return rotateLeft(x);
        }
        return x;
    }

    private AvlTreeNode<Integer, String> rotateRight(AvlTreeNode<Integer, String> x) {
        AvlTreeNode<Integer, String> y = x.left;
        AvlTreeNode<Integer, String> yRight = y.right;
        y.right = x;
        x.left = yRight;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private AvlTreeNode<Integer, String> rotateLeft(AvlTreeNode<Integer, String> x) {
        AvlTreeNode<Integer, String> y = x.right;
        AvlTreeNode<Integer, String> yLeft = y.left;
        y.left = x;
        x.right = yLeft;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private AvlTreeNode<Integer, String> rotateLeftRight(AvlTreeNode<Integer, String> x) {
        x.left = rotateLeft(x.left);
        return rotateRight(x);
    }

    private AvlTreeNode<Integer, String> rotateRightLeft(AvlTreeNode<Integer, String> x) {
        x.right = rotateRight(x.right);
        return rotateLeft(x);
    }

    private void updateHeight(AvlTreeNode<Integer, String> x) {
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
    }

    private int getBalanceFactor(AvlTreeNode<Integer, String> x) {
        if(x == null) return 0;
        return getHeight(x.left) - getHeight(x.right);
    }

    private int getHeight(AvlTreeNode<Integer, String> x) {
        if(x == null)
            return 0;
        return x.height;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        buildString(root, builder);
        if(size != 0)
            builder.setLength(builder.length()-2);
        builder.append('}');
        return builder.toString();
    }

    private void buildString(AvlTreeNode<Integer, String> node, StringBuilder builder) {
        if(node == null)
            return;

        buildString(node.left, builder);
        builder.append(node.key);
        builder.append('=');
        builder.append(node.value);
        builder.append(", ");
        buildString(node.right, builder);
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public Set<Integer> keySet() {
        return Set.of();
    }

    @Override
    public Collection<String> values() {
        return List.of();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return Set.of();
    }
}

class Result<K, V> {
    AvlTreeNode<K, V> node;
    V oldValue;

    Result(AvlTreeNode<K, V> node, V oldValue) {
        this.node = node;
        this.oldValue = oldValue;
    }
}

class AvlTreeNode<K, V> {
    K key;
    V value;
    AvlTreeNode<K, V> left;
    AvlTreeNode<K, V> right;
    int height = 1;

    AvlTreeNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.left = this.right = null;
    }
}
