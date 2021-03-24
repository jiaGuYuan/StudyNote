package t;

import java.util.Hashtable;

/**
 * 使用自定义类作为HashTable的key时，需要注意的问题 ---重写equals()和hashCode() .(**对于HashSet等也就遵守这个规定*)
 * public int hashCode()返回该对象的哈希码值。支持此方法是为了提高哈希表（例如 java.util.Hashtable 提供的哈希表）的性能。
 * hashCode 的常规协定是：
 * 在 Java 应用程序执行期间，在对同一对象多次调用 hashCode
 * 方法时，必须一致地返回相同的整数，前提是将对象进行 equals
 * 比较时所用的信息没有被修改。从某一应用程序的一次执行到同一应用程序的另一次执行，该整数无需保持一致。 如果根据 equals(Object)
 * 方法，两个对象是相等的，那么对这两个对象中的每个对象调用 hashCode 方法都必须生成相同的整数结果。 如果根据
 * equals(java.lang.Object) 方法，两个对象不相等，那么对这两个对象中的任一对象上调用 hashCode 方法不
 * 要求一定生成不同的整数结果。但是，程序员应该意识到，为不相等的对象生成不同整数结果可以提高哈希表的性能。 实际上，由 Object 类定义的
 * hashCode 方法确实会针对不同的对象返回不同的整数。（这一般是通过将该对象的内部地址转换成一个整数来实现的，但是 JavaTM
 * 编程语言不需要这种实现技巧。） 返回： 此对象的一个哈希码值。
 */
class Key {
	private int i;

	Key(int i) {
		this.i = i;
	}

	@Override
	public boolean equals(Object obj) {
		System.out.println("equals call");
		return this.i == ((Key) obj).i;
	}

	@Override
	public int hashCode() {
		System.out.println("hashCode call");
		return this.i + 1;
	}

	void add(int i) {
		this.i += i;
	}

	void show() {
		System.out.println("show:" + i);
	}
}

public class HashTableKeyTest {

	public static void main(String[] args) {
		Key b1 = new Key(1);
		Key b2 = new Key(1);

		// System.out.println(b1.equals(b2));
		// System.out.println(b1 == b2);
		//
		// System.out.println(b1);
		// System.out.println(b2);

		/**
		 * 为了成功地在哈希表中存储和获取对象，用作键的对象必须实现 hashCode 方法和 equals 方法。
		 * 两个不同的引用(如b1,b2)对于HashTable而言可能是表示相同的key。 只要作为key的两个对象 的
		 * hashCode()和equals()相等。 如：b1, b2; 它们虽然是不同对象的引用，
		 * 但是由于b1.hashCode()==b2.hashCode()且b1.equals(b2)为true. 所以将它们作为key,
		 * put到HashTable中时， HashTable将它们当作同一个key. ---这个符合情理的：
		 * 两个对象相等(equals)时，它们理应对应同一个key。
		 * 当你只重写了equlas()而没有重写hashCode()时，它们只是"假的相等".
		 * 
		 * 假如Integer没有重写hashCode()。 那么如下代码将发生什么问题：
		 * Hashtable<Integer, String> ht  = new Hashtable<Integer, String>();
		 * ht.put(1, "111");  //ht中将添加一个键值映射
		 * 
		 * ht.put(1, "222");  //ht中将添加另一个键值映射，而是修改上一个键值映射。 因为没有重写hashCode()，而使用了Object的hashCode()--基于内部地址生成的。两次put时自动装箱产生不同的Integer(内部地址不同)。 
		 * ht.get(1);//--将无法获取到数据。因为自动装箱将产生另一个不同的Integer作为key.
		 * 重写hashCode()可以生成基于非地址的hashCode()， 来满足自定义key的需求。 
		 */
		Hashtable<Key, String> ht = new Hashtable<Key, String>();
		String s = ht.put(b1, "hello"); // 返回哈希表中指定键以前的值；如果不存在值，则返回 null
		System.out.println("old: " + s);

		s = ht.put(b2, "world"); // 相当于 ht.put(b1, "world");
		System.out.println("old: " + s);

		System.out.println("ht.size()=" + ht.size());
		System.out.println("b1:" + ht.get(b1));

		// for(String str : ht.values()){
		// System.out.println("str:"+str);
		// }

		// System.out.println("b2:" + ht.get(b2));
		// b1.add(1);
		// System.out.println("b1:" + ht.get(b1));
		// System.out.println("b2:" + ht.get(b2));
	}
}
