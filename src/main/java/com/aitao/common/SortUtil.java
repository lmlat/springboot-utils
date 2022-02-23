package com.aitao.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Data : 2020/1/14
 * Time : 0:57
 * Author : AiTao
 * Information : 排序工具类(泛型)
 */
public class SortUtil {
    /**
     * 交换数据
     *
     * @param array 交换对象
     * @param i     交换位置i
     * @param j     交换位置j
     * @param <T>   任意数据类型
     */
    private static <T> void swap(T[] array, int i, int j) {
        T t = array[i];
        array[i] = array[j];
        array[j] = t;
    }

    /**
     * split划分数组
     * 思想：split算法使用一个单向的指针来对数组进行遍历，首先将数据首元素设置为比较元素，然后将第二个
     * 开始的元素依次与比较元素比较，如果大于比较元素则跳过；如果小于比较元素，则将其与前面较大的元素交
     * 换，将数组所有元素交换完后，再将比较元素放到中间位置
     *
     * @param array 划分数组对象
     * @param low   起始位置
     * @param high  结束位置
     * @param <T>   任意数组类型
     * @return 返回一个合适的位置
     */
    private static <T> int split(T[] array, int low, int high) {
        int i = low;
        T x = array[low];/**将数组第一个元素设置为比较元素*/
        /**从数组第二个元素开始遍历，若找到的元素大于比较元素，则跳过；否则将当前元素进行交换*/
        for (int j = low + 1; j <= high; j++) {
            if (((Comparable) array[j]).compareTo(x) <= 0) {
                i++;
                if (i != j) {
                    swap(array, i, j);
                }
            }
        }
        swap(array, low, i);/**最后将比较元素交换到期望的位置*/
        return i;
    }

    /**
     * 划分数组
     * 思想：partition算法使用头尾两个方向相反的指针进行遍历，先将数组第一个
     * 元素设置为比较元素，头指针从左至右找到第一个大于比较元素的数，尾指针从
     * 右至左找到第一个小于比较元素的数，全部交换完毕后将比较元素放到中间位置
     *
     * @param array 划分数组对象
     * @param low   起始位置
     * @param high  结束位置
     * @param <T>   任意数组类型
     * @return 返回一个合适的位置
     */
    private static <T> int partition(T[] array, int low, int high) {
        T temp = array[low];//将第一个元素设置为比较元素
        int left = low;
        int right = high;
        while (left < right) {
            while (left < right && ((Comparable) array[right]).compareTo(temp) >= 0) {
                right--;/**从右到左找到第一个小于比较元素的数*/
            }
            while (left < right && ((Comparable) array[left]).compareTo(temp) <= 0) {
                left++;/**从左到右找到第一个大于比较元素的数*/
            }
            swap(array, left, right);
        }
        swap(array, low, left);/**将比较元素交换到期望的位置*/
        return left;/**返回比较元素的位置*/
    }

    /**
     * 快速排序
     * 最佳情况：T(n) = O(nlogn)
     * 最差情况：T(n) = O(n^2)
     * 平均情况：T(n) = O(nlogn)
     * 思想：快排实现重点在于数组的划分，通常是将数组的第一个元素定义为比较元素，
     * 然后将数组中小于比较元素的数放在左边，大于比较元素的放在右边
     *
     * @param array
     * @param low
     * @param high
     * @param <T>
     */
    public static <T extends Comparable> void qsort(T[] array, int low, int high) {
        if (low < high) {
            int mid = partition(array, low, high);//划分数组并获得比较元素位置
            qsort(array, low, mid - 1);//对比较元素左边进行排序
            qsort(array, mid + 1, high);//对比较元素右边进行排序
        }
    }

    /**
     * 直接插入排序
     * 最佳情况：T(n) = O(n)
     * 最坏情况：T(n) = O(n^2)
     * 平均情况：T(n) = O(n^2)
     * 思想：每一趟将一个待排序的记录，按其关键字的大小插入到已经排好序的一
     * 组记录的适当位置上，直到所有待排序记录全部插入为止
     *
     * @param <T>
     * @param array
     */
    public static <T extends Comparable> void insertSort(T[] array) {
        int j;
        T temp;
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(array[i - 1]) < 0) {
                j = i;
                temp = array[i];
                while (j > 0 && array[j - 1].compareTo(temp) > 0) {
                    array[j] = array[j - 1];
                    j--;
                }
                array[j] = temp;
            }
        }
    }

    /**
     * 希尔排序
     * 思想：首先把较大的数据集合分割成若干个组，然后对每个小组进行直接插入排序
     *
     * @param array
     * @param <T>
     */
    public static <T extends Comparable> void shellSort(T[] array) {
        int N = array.length;
        /**分组：最开始时的增量gap为数组长度的一半*/
        for (int gap = N >>> 1; gap > 0; gap >>>= 1) {//增量每次减半
            /**对各个分组进行直接插入排序*/
            for (int i = gap; i < N; i++) {
                insertSort(array, gap, i);
            }
        }
    }

    private static <T extends Comparable> void insertSort(T[] array, int gap, int i) {
        T temp = array[i];
        int j;
        /**插入时按组进行插入（组内元素两两相隔gap）*/
        for (j = i - gap; j >= 0 && (array[j].compareTo(temp) > 0); j -= gap) {
            array[j + gap] = array[j];
        }
        array[j + gap] = temp;
    }

    /**
     * 冒泡排序
     * 最佳情况：T(n) = O(n)
     * 最差情况：T(n) = O(n^2)
     * 平均情况：T(n) = O(n^2)
     * 思想：两个数比较大小，较大的数下沉，较小的数往上冒
     * <p>
     * 针对问题：数据的顺序排好后，冒泡算法仍然会进行下一轮的比较，直到array.length-1次，
     * 显然后面的比较是没有意义的
     * <p>
     * 优化排序思想：设置标志flag，如果发生了交换flag设置为true；反之设置为false；
     * 这样当一轮比较结束后如果flag仍为false，即：这一轮没有发生交换，说明数据的顺序已经排好，没有必要继续进行下
     *
     * @param array
     * @param <T>
     */
    public static <T extends Comparable> void bubbleSort(T[] array) {
        boolean flag;
        for (int i = 0; i < array.length - 1; i++) {
            flag = false;
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j].compareTo(array[j + 1]) > 0) {
                    swap(array, j, j + 1);
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }
    }

    /**
     * 选择排序
     * 最佳情况：T(n) = O(n^2)
     * 最差情况：T(n) = O(n^2)
     * 平均情况：T(n) = O(n^2)
     * 思想：在长度为N的无序数组中，第一次遍历N-1个数，找到最小的数值与第一个元素交换；
     * 第二次遍历N-2个数，找到最小的数值与第二个元素交换；... 第i次遍历N-i个数，找到最小的数值与第i个元素交换；
     *
     * @param array
     * @param <T>
     */
    public static <T> void selectSort(T[] array) {
        int index;
        for (int i = 0; i < array.length - 1; i++) {
            index = i;
            for (int j = i + 1; j < array.length; j++) {
                if (((Comparable) array[index]).compareTo(array[j]) > 0) {/**寻找最小值，并将其下标保留*/
                    index = j;
                }
            }
            if (index != i) {/**当前最小值与第i个位置进行交换*/
                swap(array, index, i);
            }
        }
    }

    /**
     * 归并排序(将两个有序子数组合并操作)
     * 思想：
     *
     * @param array
     * @param left
     * @param mid
     * @param right
     * @param temp
     */
    private static void mergeSort(Object[] array, int left, int mid, int right, Object[] temp) {
        int low = left;//左序列指针
        int high = mid + 1;//右序列指针
        int t = 0;//临时变量
        /**合并*/
        while (low <= mid && high <= right) {
            if (((Comparable) array[low]).compareTo(array[high]) < 0) {
                temp[t++] = array[low++];
            } else {
                temp[t++] = array[high++];
            }
        }
        /**将左边剩余元素填充进临时数组中*/
        while (low <= mid) {
            temp[t++] = array[low++];
        }
        /**将右边剩余元素填充进临时数组中*/
        while (high <= right) {
            temp[t++] = array[high++];
        }
        /**将临时数组中的所有数组拷贝到原排序数组中*/
        t = 0;
        while (left <= right) {
            array[left++] = temp[t++];
        }
    }

    /**
     * 归并排序
     *
     * @param array
     */
    public static void sort(Object[] array) {
        Object[] temp = new Object[array.length];//定义临时数组
        sort(array, 0, array.length - 1, temp);
    }

    private static void sort(Object[] array, int left, int right, Object[] temp) {
        if (left < right) {
            int mid = (left + right) >>> 1;
            sort(array, left, mid, temp);//左边归并排序
            sort(array, mid + 1, right, temp);//右边归并排序
            mergeSort(array, left, mid, right, temp);//将两个有序子数组合并
        }
    }

    /**
     * 地精排序
     * 思想：先设计一个标识index=0,然后从头开始判断，什么时候index<array.length不成立，
     * 排序什么时候结束。在比较过程中，若标识index=0或只要不交换数据时，index值就自增1；
     * 若数值发生交换，则index需要自减1，依次类推
     *
     * @param array
     * @param <T>
     */
    public static <T> void gnomeSort(T[] array) {
        for (int i = 0; i < array.length; ) {
            /**当i为0时，或只要不交换i就自增*/
            if (i == 0 || ((Comparable) array[i - 1]).compareTo(array[i]) <= 0) {
                i++;
            } else {/**只要发生交换i就自减*/
                swap(array, i, i - 1);
                i--;
            }
        }
    }

    /**
     * 构建堆
     * 描述：最小堆要求节点元素都不大于其左右孩子
     * 最大堆要求节点的元素都要不小于其孩子
     * 定义：大顶堆：arr[i] >= arr[2i+1] && arr[i] >= arr[2i+2]
     * 小顶堆：arr[i] <= arr[2i+1] && arr[i] <= arr[2i+2]
     *
     * @param a
     * @param <T>
     */
    private static <T> void makeMinHeap(T[] a) {
        int len = a.length;
        for (int i = (len >>> 1) - 1; i >= 0; i--) {
            minHeapFixdown(a, i, len);
        }
    }

    /***
     * 从i节点开始调整，len为节点总数，从0开始计算i节点的子节点
     * 为2*i+1,2*i+2
     * @param a
     * @param i
     * @param len 节点总数
     * @param <T>
     */
    private static <T> void minHeapFixdown(T[] a, int i, int len) {
        //子节点
        for (int j = 2 * i + 1; j < len; i = j, j = 2 * j + 1) {
            if (j + 1 < len && ((Comparable) a[j + 1]).compareTo(a[j]) >= 0) {//在左右子节点中寻找最大的
                j++;
            }
            if (((Comparable) a[i]).compareTo(a[j]) > 0) {
                break;
            }
            swap(a, i, j);//较小的节点上移
        }
    }

    /**
     * 堆排序
     * 思想：将待排序序列构造成一个大顶堆，此时，整个序列的最大值就是堆顶的根节点。将其与末尾元素进行交换，
     * 此时末尾就为最大值。然后将剩余n-1个元素重新构造成一个堆，这样会得到n个元素的次小值。依次类推...
     *
     * @param array
     * @param <T>
     */
    public static <T> void heapSort(T[] array) {
        makeMinHeap(array);/**1.将无序序列构建成一个堆*/
        for (int i = array.length - 1; i > 0; i--) {
            swap(array, 0, i);/**2.将堆顶元素与末尾元素交换，将最大元素沉到数组末尾*/
            minHeapFixdown(array, 0, i);/**3.重新调整结构，使用其满足堆定义，然后继续交换堆顶元素与当前末尾元素*/
        }
    }

    /**
     * 桶排序
     *
     * @param array
     */
    public static void bucketSort(Integer[] array) {
        /**1.根据待排序集合中最大元素和最小元素的差值范围和映射规则，来确定申请的桶个数*/
        Integer max = Integer.MIN_VALUE;
        Integer min = Integer.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(array[i], max);
            min = Math.min(array[i], min);
        }
        int bucketNum = (max - min) / array.length + 1;//计算桶的数量 = （最大值-最小值）/ 待排序列总长度 + 1
        //定义桶
        ArrayList<ArrayList<Integer>> bArr = new ArrayList<>(bucketNum);
        for (int i = 0; i < bucketNum; i++) {
            bArr.add(new ArrayList<>());
        }

        /**2.将第个元素放入桶中*/
        for (int i = 0; i < array.length; i++) {
            int num = (array[i] - min) / (array.length);
            bArr.get(num).add(array[i]);
        }
        /**3.对第个桶进行排序*/
        for (int i = 0; i < bucketNum; i++) {
            Collections.sort(bArr.get(i));
        }
        /**4.将桶中的元素赋值到原序列*/
        int index = 0;
        for (int i = 0; i < bArr.size(); i++) {
            for (int j = 0; j < bArr.get(i).size(); j++) {
                array[index++] = bArr.get(i).get(j);
            }
        }
    }

    /**
     * 基数排序
     * 思想：首先创建数组A[MaxValue]；然后将每个数放到相应的位置上（例如17放在下标17的数组位置）；
     * 最后遍历数组，即为排序后的结果。
     *
     * @param array
     */
    public static void radixSort(Integer[] array) {
        //1.计算最大值的位数
        int max_bit = String.valueOf(Collections.max(Arrays.asList(array))).length();
        int k = 0;//记录下标
        int n = 1, m = 1;//控制键值排序依据在哪一位,从个数->十位->百位...
        int[][] temp = new int[10][array.length];//数组的第一维表示可能的余数0-9
        int[] order = new int[10];//数组order用来表示该位是i的数的个数
        while (m <= max_bit) {
            /**1.根据不同的位数，为相应的桶添加数据*/
            for (int i = 0; i < array.length; i++) {
                int lsd = array[i] / n % 10;//当n=1时，取个位；当n=n*10时，取十位；当n=n*100时，取百位；依次类推
                temp[lsd][order[lsd]] = array[i];
                order[lsd]++;
            }
            /**2.根据order记录每个桶中的数据个数，取出每个桶中的数据*/
            for (int i = 0; i < 10; i++) {
                if (order[i] != 0) {
                    for (int j = 0; j < order[i]; j++) {
                        array[k++] = temp[i][j];
                    }
                }
                order[i] = 0;
            }
            k = 0;
            n *= 10;
            m++;
        }
    }

    /**
     * 睡眠排序
     * 计数排序
     */
}

