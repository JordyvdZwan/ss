package week4;

public class Exercises {
    public static int countNegativeNumbers(int[] arr) {
        int result = 0;
    	for (int i = 0;i < arr.length; i++) {
        	if (arr[i] < 0) {
        		result++;
        	}
        }
    	return result;
    }

    public static void reverseArray(int[] ints) {
        int[] result = new int[]{};
    	for (int i = 0; i < Math.floor(ints.length/ 2); i++) {
    		int value1 = ints[i];
    		int value2 = ints[ints.length - i - 1];
    		ints[i] = value2;
    		ints[ints.length - i - 1] = value1;
        }
    }

    class SimpleList {
        public class Element {}

        public class Node {
            public Node next;
            public Element element;
        }

        private Node first;

        private Node find(Element element) {
            Node p = first;
            if (p == null) {
                return null;
            }
            while (p.next != null && !p.next.element.equals(element)) {
                p = p.next;
            }
            if (p.next == null) {
                return null;
            } else {
                return p;
            }
        }

        public void remove(Element element) {
            Node p = find(element);
            if (p != null) {
                p.next = p.next.next;
            }
        }
    }
}
