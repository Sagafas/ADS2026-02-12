package by.it.group510901.petsevich.lesson12;

import java.util.*;

/*
toString()
put(Integer,String)
remove(Integer)
get(Integer)
containsKey(Integer)
containsValue(String)

size()
clear()
isEmpty()

headMap(Integer)
tailMap(Integer)
firstKey()
lastKey()
 */

public class MyRbMap implements SortedMap<Integer, String> {
    private RbTreeNode nil;
    private RbTreeNode root;
    private int size;

    MyRbMap() {
        nil = new RbTreeNode(null, null);
        nil.color = Color.Black;
        nil.left = nil.right = nil.parent = nil;
        root = nil;
        size = 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap newMap = new MyRbMap();
        buildLowerSubmap(root, newMap, toKey);
        return newMap;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap newMap = new MyRbMap();
        buildHigherSubmap(root, newMap, fromKey);
        return newMap;
    }

    private void buildLowerSubmap(RbTreeNode node, SortedMap<Integer, String> newMap, Integer toKey) {
        if(node == nil)
            return;

        buildLowerSubmap(node.left, newMap, toKey);
        if(node.key < toKey)
            newMap.put(node.key, node.value);
        buildLowerSubmap(node.right, newMap, toKey);
    }

    private void buildHigherSubmap(RbTreeNode node, SortedMap<Integer, String> newMap, Integer fromKey) {
        if(node == nil)
            return;

        buildHigherSubmap(node.left, newMap, fromKey);
        if(node.key >= fromKey)
            newMap.put(node.key, node.value);
        buildHigherSubmap(node.right, newMap, fromKey);
    }

    @Override
    public Integer firstKey() {
        return getMinNode(root).key;
    }

    @Override
    public Integer lastKey() {
        return getMaxNode(root).key;
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
        if (!(key instanceof Integer)) return false;
        return searchByKey(root, (Integer) key) != nil;
    }

    private RbTreeNode searchByKey(RbTreeNode node, Integer key) {
        if(node == nil || Objects.equals(key, node.key))
            return node;
        if(key < node.key)
            return searchByKey(node.left, key);
        return searchByKey(node.right, key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (!(value instanceof String)) return false;
        return searchByValue(root, (String)value) != nil;
    }

    private RbTreeNode searchByValue(RbTreeNode node, String value) {
        if(node == nil || Objects.equals(value, node.value))
            return node;
        if(value.compareTo(node.value) < 0)
            return searchByValue(node.left, value);
        return searchByValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        return searchByKey(root, (Integer)key).value;
    }

    @Override
    public String put(Integer key, String value) {
        return insert(key, value);
    }

    private String insert(Integer key, String value) {
        RbTreeNode node = new RbTreeNode(key, value);
        node.left = node.right = nil;
        RbTreeNode parent = nil;
        RbTreeNode current = root;

        while(current != nil) {
            parent = current;
            if(node.key < current.key)
                current = current.left;
            else if(node.key > current.key)
                current = current.right;
            else {
                String oldValue = current.value;
                current.value = value;
                return oldValue;
            }
        }

        node.parent = parent;
        if(parent == nil)
            root = node;
        else if(node.key < parent.key)
            parent.left = node;
        else parent.right = node;

        fixInsert(node);
        size++;
        return null;
    }

    private void fixInsert(RbTreeNode node) {
        while(node.parent.color == Color.Red) {
            if(node.parent == node.parent.parent.left) {
                RbTreeNode uncle = node.parent.parent.right;
                if(uncle.color == Color.Red) {
                    uncle.color = Color.Black;
                    node.parent.color = Color.Black;
                    node.parent.parent.color = Color.Red;
                    node = node.parent.parent;
                    continue;
                }

                if(node == node.parent.right) {
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = Color.Black;
                node.parent.parent.color = Color.Red;
                rotateRight(node.parent.parent);
            }
            else {
                RbTreeNode uncle = node.parent.parent.left;
                if (uncle.color == Color.Red) {
                    uncle.color = Color.Black;
                    node.parent.color = Color.Black;
                    node.parent.parent.color = Color.Red;
                    node = node.parent.parent;
                    continue;
                }

                if (node == node.parent.left) {
                    node = node.parent;
                    rotateRight(node);
                }
                node.parent.color = Color.Black;
                node.parent.parent.color = Color.Red;
                rotateLeft(node.parent.parent);
            }
        }
        root.color = Color.Black;
    }

    private void rotateLeft(RbTreeNode x) {
        RbTreeNode y = x.right;
        x.right = y.left;

        if(y.left != nil)
            y.left.parent = x;

        y.parent = x.parent;
        if(x.parent == nil)
            root = y;
        else if(x.parent.left == x)
            x.parent.left = y;
        else x.parent.right = y;

        y.left = x;
        x.parent = y;
    }

    private void rotateRight(RbTreeNode x) {
        RbTreeNode y = x.left;
        x.left = y.right;

        if(y.right != nil)
            y.right.parent = x;

        y.parent = x.parent;
        if(x.parent == nil)
            root = y;
        else if(x.parent.left == x)
            x.parent.left = y;
        else x.parent.right = y;

        y.right = x;
        x.parent = y;
    }

    @Override
    public String remove(Object key) {
        return delete((Integer)key);
    }

    private String delete(Integer key) {
        RbTreeNode node = searchByKey(root, key);
        if(node == nil)
            return null;

        String value = node.value;
        RbTreeNode x = nil;
        RbTreeNode y = node;
        Color yOriginalColor = y.color;

        if(node.left == nil) {
            x = node.right;
            transplant(node, node.right);
        }
        else if(node.right == nil) {
            x = node.left;
            transplant(node, node.left);
        }
        else {
            y = getMinNode(node.right);
            yOriginalColor = y.color;
            x = y.right;

            if(y.parent == node) {
                x.parent = y;
            }
            else {
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }

            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.color = node.color;
        }

        if(yOriginalColor == Color.Black)
            fixDelete(x);
        size--;
        return value;
    }

    private void fixDelete(RbTreeNode x) {
        while(x != root && x.color == Color.Black) {
            if(x == x.parent.left) {
                RbTreeNode sibling = x.parent.right;
                if(sibling.color == Color.Red) {
                    sibling.color = Color.Black;
                    x.parent.color = Color.Red;
                    rotateLeft(x.parent);
                    sibling = x.parent.right;
                }
                if(sibling.left.color == Color.Black && sibling.right.color == Color.Black) {
                    sibling.color = Color.Red;
                    x = x.parent;
                }
                else {
                    if(sibling.right.color == Color.Black) {
                        sibling.left.color = Color.Black;
                        sibling.color = Color.Red;
                        rotateRight(sibling);
                        sibling = x.parent.right;
                    }

                    sibling.color = x.parent.color;
                    x.parent.color = Color.Black;
                    sibling.right.color = Color.Black;
                    rotateLeft(x.parent);
                    x = root;
                }
            }
            else {
                RbTreeNode sibling = x.parent.left;
                if(sibling.color == Color.Red) {
                    sibling.color = Color.Black;
                    x.parent.color = Color.Red;
                    rotateRight(x.parent);
                    sibling = x.parent.left;
                }
                if(sibling.right.color == Color.Black && sibling.left.color == Color.Black) {
                    sibling.color = Color.Red;
                    x = x.parent;
                }
                else {
                    if(sibling.left.color == Color.Black) {
                        sibling.right.color = Color.Black;
                        sibling.color = Color.Red;
                        rotateLeft(sibling);
                        sibling = x.parent.left;
                    }

                    sibling.color = x.parent.color;
                    x.parent.color = Color.Black;
                    sibling.left.color = Color.Black;
                    rotateRight(x.parent);
                    x = root;
                }
                x.color = Color.Black;
            }
        }
    }

    private void transplant(RbTreeNode u, RbTreeNode v) {
        if(u.parent == nil)
            root = v;
        else if(u == u.parent.left)
            u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    private RbTreeNode getMinNode(RbTreeNode node) {
        while(node.left != nil)
            node = node.left;
        return node;
    }

    private RbTreeNode getMaxNode(RbTreeNode node) {
        while(node.right != nil)
            node = node.right;
        return node;
    }

    @Override
    public void clear() {
        root = nil;
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

    private void buildString(RbTreeNode node, StringBuilder builder) {
        if(node == nil)
            return;

        buildString(node.left, builder);
        builder.append(node.key);
        builder.append('=');
        builder.append(node.value);
        builder.append(", ");
        buildString(node.right, builder);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {}

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

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }
}

enum Color {
    Red, Black
}

class RbTreeNode {
    Integer key;
    String value;
    RbTreeNode left;
    RbTreeNode right;
    RbTreeNode parent;
    Color color;

    RbTreeNode(Integer key, String value) {
        this.key = key;
        this.value = value;
        this.parent = this.left = this.right = null;
        color = Color.Red;
    }

}