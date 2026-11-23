package by.it.group510901.petsevich.lesson12;

import org.w3c.dom.Node;

import javax.xml.transform.SourceLocator;
import java.awt.*;
import java.util.*;
import java.util.List;

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

lowerKey(Integer)
floorKey(Integer)
ceilingKey(Integer)
higherKey(Integer)
 */

public class MySplayMap implements NavigableMap<Integer, String> {
    SplayNode nil;
    SplayNode root;
    int size = 0;

    MySplayMap() {
        nil = new SplayNode(null, null);
        nil.left = nil.right = nil.parent = nil;
        root = nil;
        size = 0;
    }

    private void rotateLeft(SplayNode x) {
        SplayNode y = x.right;
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

    private void rotateRight(SplayNode x) {
        SplayNode y = x.left;
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

    private void splay(SplayNode x) {
        while(x.parent != nil) {
            SplayNode p = x.parent;
            SplayNode g = p.parent;

            if(g == nil) {
                if(x == p.left)
                    rotateRight(p);
                else rotateLeft(p);
            }
            else if(x == p.left && p == g.left) {
                rotateRight(g);
                rotateRight(p);
            }
            else if(x == p.right && p == g.right) {
                rotateLeft(g);
                rotateLeft(p);
            }
            else if(x == p.right && p == g.left) {
                rotateLeft(p);
                rotateRight(g);
            }
            else {
                rotateRight(p);
                rotateLeft(g);
            }
        }
        root = x;
    }

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if(key == null)
            throw new NullPointerException();
        SplayNode x = root, best = nil;
        while(x != nil) {
            if(x.key.compareTo(key) < 0) {
                best = x;
                x = x.right;
            }
            else {
                x = x.left;
            }
        }
        if(best == nil)
            return null;
        splay(best);
        return best.key;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if(key == null)
            throw new NullPointerException();
        SplayNode x = root, best = nil;
        while(x != nil) {
            if(x.key.compareTo(key) <= 0) {
                best = x;
                x = x.right;
            }
            else {
                x = x.left;
            }
        }
        if(best == nil)
            return null;
        splay(best);
        return best.key;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if(key == null)
            throw new NullPointerException();
        SplayNode x = root, best = nil;
        while(x != nil) {
            if(x.key.compareTo(key) >= 0) {
                best = x;
                x = x.left;
            }
            else {
                x = x.right;
            }
        }
        if(best == nil)
            return null;
        splay(best);
        return best.key;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if(key == null)
            throw new NullPointerException();
        SplayNode x = root, best = nil;
        while(x != nil) {
            if(x.key.compareTo(key) > 0) {
                best = x;
                x = x.left;
            }
            else {
                x = x.right;
            }
        }
        if(best == nil)
            return null;
        splay(best);
        return best.key;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        if(toKey == null)
            throw new NullPointerException();
        MySplayMap map = new MySplayMap();
        collectHead(root, toKey, inclusive, map);
        return map;
    }

    private void collectHead(SplayNode x, Integer toKey, boolean inclusive, MySplayMap map) {
        if(x == nil) return;
        collectHead(x.left, toKey, inclusive, map);
        int compare = x.key.compareTo(toKey);
        boolean isTaken = inclusive ? (compare <= 0) : (compare < 0);
        if(isTaken) {
            map.put(x.key, x.value);
            collectHead(x.right, toKey, inclusive, map);
        }
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        if(fromKey == null)
            throw new NullPointerException();
        MySplayMap map = new MySplayMap();
        collectTail(root, fromKey, inclusive, map);
        return map;
    }

    private void collectTail(SplayNode x, Integer fromKey, boolean inclusive, MySplayMap map) {
        if(x == nil) return;
        int compare = x.key.compareTo(fromKey);
        boolean isTaken = inclusive ? (compare >= 0) : (compare > 0);
        if(isTaken) {
            collectTail(x.left, fromKey, inclusive, map);
            map.put(x.key, x.value);
        }
        collectTail(x.right, fromKey, inclusive, map);
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public Integer firstKey() {
        if(root == nil)
            throw new NoSuchElementException();
        SplayNode x = getMinNode(root);
        splay(x);
        return x.key;
    }

    @Override
    public Integer lastKey() {
        if(root == nil)
            throw new NoSuchElementException();
        SplayNode x = getMaxNode(root);
        splay(x);
        return x.key;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return searchByKey(root, (Integer) key) != nil;
    }

    private SplayNode searchByKey(SplayNode node, Integer key) {
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

    private SplayNode searchByValue(SplayNode node, String value) {
        if(node == nil || Objects.equals(value, node.value))
            return node;
        if(value.compareTo(node.value) < 0)
            return searchByValue(node.left, value);
        return searchByValue(node.right, value);
    }

    @Override
    public String get(Object key) {
        if(!(key instanceof Integer)) return null;
        SplayNode node = searchByKey(root, (Integer)key);
        if(node == nil) return null;

        splay(node);
        return node.value;
    }

    @Override
    public String put(Integer key, String value) {
        return insert(key, value);
    }

    private String insert(Integer key, String value) {
        if (root == nil) {
            SplayNode n = new SplayNode(key, value);
            n.left = n.right = n.parent = nil;
            root = n;
            size = 1;
            return null;
        }

        SplayNode parent = nil;
        SplayNode cur = root;

        while (cur != nil) {
            int cmp = key.compareTo(cur.key);

            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }

            parent = cur;
            cur = (cmp < 0) ? cur.left : cur.right;
        }

        SplayNode newNode = new SplayNode(key, value);
        newNode.left = newNode.right = newNode.parent = nil;
        newNode.parent = parent;

        if (key.compareTo(parent.key) < 0)
            parent.left = newNode;
        else
            parent.right = newNode;

        splay(newNode);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        return delete((Integer)key);
    }

    private String delete(Integer key) {
        if (root == nil) return null;

        SplayNode cur = root;
        while (cur != nil) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) break;
            cur = (cmp < 0) ? cur.left : cur.right;
        }

        if (cur == nil) return null;

        splay(cur);
        String old = cur.value;

        if (cur.left == nil) {
            root = cur.right;
            if (root != nil) root.parent = nil;
        }
        else if (cur.right == nil) {
            root = cur.left;
            root.parent = nil;
        }
        else {
            SplayNode L = cur.left;
            SplayNode R = cur.right;
            L.parent = nil;
            R.parent = nil;

            SplayNode m = L;
            while (m.right != nil) m = m.right;
            splay(m);

            m.right = R;
            R.parent = m;
            root = m;
        }

        size--;
        return old;
    }

    private SplayNode getMinNode(SplayNode node) {
        while(node.left != nil)
            node = node.left;
        return node;
    }

    private SplayNode getMaxNode(SplayNode node) {
        while(node.right != nil)
            node = node.right;
        return node;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {

    }

    @Override
    public void clear() {
        root = nil;
        size = 0;
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

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        buildString(root, builder);
        if(size != 0)
            builder.setLength(builder.length()-2);
        builder.append('}');
        return builder.toString();
    }

    private void buildString(SplayNode node, StringBuilder builder) {
        if(node == nil)
            return;

        buildString(node.left, builder);
        builder.append(node.key);
        builder.append('=');
        builder.append(node.value);
        builder.append(", ");
        buildString(node.right, builder);
    }
}

class SplayNode {
    Integer key;
    String value;
    SplayNode left;
    SplayNode right;
    SplayNode parent;

    SplayNode(Integer key, String value) {
        this.key = key;
        this.value = value;
        this.left = this.right = this.parent = null;
    }
}