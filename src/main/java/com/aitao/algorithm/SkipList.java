package com.aitao.algorithm;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @Author : AiTao
 * @Create : 2021-12-22 2:34
 * @Description : 跳表
 */
public class SkipList<E extends Comparable<? super E>> {
    //跳表层数，定义成32层理论上对于2^32-1个元素的查询最优
    private final int MAX_LEVEL = 32;
    //当前跳表的层级
    private int level = 0;
    //头结点
    private final Node<E> header = new Node<>(null, MAX_LEVEL);
    //随机数种子
    private final Random random = new SecureRandom();

    //跳表节点类
    static class Node<E extends Comparable<? super E>> {
        //节点存储的值Val
        public E val;
        //节点指向第i层的节点next[i]
        public Node<E>[] next;

        @SuppressWarnings("unchecked")
        public Node(E val, int level) {
            this.next = new Node[level];
            this.val = val;
        }
    }

    /**
     * 检查容器中是否包含指定节点
     *
     * @param val 值
     * @return true or false
     */
    public boolean contains(E val) {
        //curr指向跳表头结点
        Node<E> curr = header;
        /**
         * 从顶层开始查找当前层的链表中是否包含节点node，如果包含node节点，直接返回true；否则在下一层中查找是否包含node节点。
         * 如果最底层的链表也不包含node节点，则放回false。
         */
        for (int i = level; i >= 0; i--) {
            while (curr.next != null && curr.next[i].val.compareTo(val) < 0) {
                curr = curr.next[i];
            }
            if (curr.next[i].val.equals(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加结点
     *
     * @param val 节点值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void add(E val) {
        //指向头节点
        Node<E> curr = header;
        //保存前继节点
        Node<E>[] pre = new Node[MAX_LEVEL];
        //找出每层中待插入节点的前继节点
        for (int i = level; i >= 0; i--) {
            curr = header;
            while (curr.next[i] != null && curr.next[i].val.compareTo(val) < 0) {
                curr = curr.next[i];
            }
            pre[i] = curr;
        }
        curr = curr.next[0];
        //维护索引链表，返回1不建索引、返回2建一级索引、返回3建二级索引、返回4建三级索引依次类推
        int nextLevel = randomLevel();
        //如果待插入节点位置是空，或者与node节点值不同，将新节点插入到跳表中
        if (curr == null || !curr.val.equals(val)) {
            //若新增一层链表
            if (nextLevel > level) {
                pre[nextLevel] = header;
                level = nextLevel;
            }
            Node<E> node = new Node(val, MAX_LEVEL);
            for (int i = level; i >= 0; i--) {
                node.next[i] = pre[i].next[i];
                pre[i].next[i] = node;
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    public void delete(E val) {
        Node<E> curr = header;
        Node<E>[] pre = new Node[MAX_LEVEL];
        //寻找待删除元素在不同层上的前继节点
        for (int i = level; i >= 0; i--) {
            while (curr.next != null && curr.next[i].val.compareTo(val) < 0) {
                curr = curr.next[i];
            }
            pre[i] = curr;
        }
        curr = curr.next[0];
        //若跳表中不含此节点
        if (!curr.val.equals(val)) {
            return;
        }
        for (int i = level; i >= 0; i--) {
            if (!pre[i].next[i].val.equals(val)) {
                continue;
            }
            pre[i].next[i] = curr.next[i];
        }
        //若删除元素val后level层元素数目为0，层数减少一层
        while (level > 0 && header.next[level] == null) {
            level--;
        }
    }

    /**
     * 输出跳表中的元素
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> curr = header.next[0];
        sb.append("{");
        while (curr.next[0] != null) {
            sb.append(curr.val);
            sb.append(",");
            curr = curr.next[0];
        }
        sb.append(curr.val);
        sb.append("}");
        return sb.toString();
    }

    /**
     * 利用随机数发生器来决定是否新增一层
     * 随机生成 1~MAX_LEVEL 之间的数，且 ：
     * 1/2 的概率返回 1
     * 1/4 的概率返回 2
     * 1/8 的概率返回 3
     * 以此类推。
     *
     * @return 返回1不建索引、返回2建一级索引、返回3建二级索引、返回4建三级索引依次类推
     */
    private int randomLevel() {
        double ins = random.nextDouble();
        int nextLevel = level;
        if (ins > Math.E && level < MAX_LEVEL) {
            nextLevel += 1;
        }
        return nextLevel;
    }


    public static void main(String[] args) {
        SkipList<Integer> skipList = new SkipList<>();
        skipList.add(1);
        System.out.println(skipList.toString());
    }
}



