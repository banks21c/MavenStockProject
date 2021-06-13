package html.parsing.stock.javafx;

public class CreateAmericanName {
	String familyName[] = { "gahl", "gahng", "geon", "go", "gong", "gwak", "goo", "gook", "gwon", "geum", "gi", "gil",
			"gim", "nah", "nahm", "nahmgoong", "noh", "doh", "rah", "ryu", "mah", "maeng", "myeong", "moh", "mok",
			"moon", "min", "park", "bahn", "bahng", "bae", "baek", "byu", "boo", "bing", "seo", "seok", "seonwoo",
			"seol", "seong", "soh", "son", "song", "sin", "sim", "ahn", "yahng", "eo", "eom", "yeo", "yeon", "yeom",
			"oh", "ok", "wahng", "woo", "won", "wi", "yoo", "yook", "yoon", "lee", "in", "lim", "jahng", "jeon",
			"jeong", "je", "joh", "joo", "ji", "jin", "chah", "chae", "cheon", "choi", "choo", "tahk", "pyo", "hah",
			"hahn", "hahm", "heo", "hyun", "hong", "hwang" };
	String maleNames[] = { "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph", "Thomas",
			"Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Donald", "Mark", "Paul", "Steven", "Andrew",
			"Kenneth", "Joshua", "Kevin", "Brian", "George", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan",
			"Jacob", "Gary", "Nicholas", "Eric", "Jonathan", "Stephen", "Larry", "Justin", "Scott", "Brandon",
			"Benjamin", "Samuel", "Frank", "Gregory", "Raymond", "Alexander", "Patrick", "Jack", "Dennis", "Jerry",
			"Tyler", "Aaron", "Jose", "Henry", "Adam", "Douglas", "Nathan", "Peter", "Zachary", "Kyle", "Walter",
			"Harold", "Jeremy", "Ethan", "Carl", "Keith", "Roger", "Gerald", "Christian", "Terry", "Sean", "Arthur",
			"Austin", "Noah", "Lawrence", "Jesse", "Joe", "Bryan", "Billy", "Jordan", "Albert", "Dylan", "Bruce",
			"Willie", "Gabriel", "Alan", "Juan", "Logan", "Wayne", "Ralph", "Roy", "Eugene", "Randy", "Vincent",
			"Russell", "Louis", "Philip", "Bobby", "Johnny", "Bradley" };
	String femaleName[] = { "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica",
			"Sarah", "Karen", "Nancy", "Lisa", "Margaret", "Betty", "Sandra", "Ashley", "Dorothy", "Kimberly", "Emily",
			"Donna", "Michelle", "Carol", "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca", "Laura", "Sharon",
			"Cynthia", "Kathleen", "Amy", "Shirley", "Angela", "Helen", "Anna", "Brenda", "Pamela", "Nicole",
			"Samantha", "Katherine", "Emma", "Ruth", "Christine", "Catherine", "Debra", "Rachel", "Carolyn", "Janet",
			"Virginia", "Maria", "Heather", "Diane", "Julie", "Joyce", "Victoria", "Kelly", "Christina", "Lauren",
			"Joan", "Evelyn", "Olivia", "Judith", "Megan", "Cheryl", "Martha", "Andrea", "Frances", "Hannah",
			"Jacqueline", "Ann", "Gloria", "Jean", "Kathryn", "Alice", "Teresa", "Sara", "Janice", "Doris", "Madison",
			"Julia", "Grace", "Judy", "Abigail", "Marie", "Denise", "Beverly", "Amber", "Theresa", "Marilyn",
			"Danielle", "Diana", "Brittany", "Natalie", "Sophia", "Rose", "Isabella", "Alexis", "Kayla", "Charlotte" };

	public CreateAmericanName() {
//		for (int i = 0; i < familyName.length; i++) {
//			for (int j = 0; j < maleNames.length; j++) {
//				if (maleNames[j].equals(familyName[i])) {
//					// System.out.println("\t\t\t\t\t\t\t"+maleNames[j]+" "+familyName[i]);
//				} else {
//					System.out.println(maleNames[j] + " " + familyName[i]);
//				}
//			}
//		}
//		System.out.println("==========================================");
//		for (int i = 0; i < familyName.length; i++) {
//			for (int j = 0; j < femaleName.length; j++) {
//				if (femaleName[j].equals(familyName[i])) {
//					// System.out.println("\t\t\t\t\t\t\t"+femaleName[j]+" "+familyName[i]);
//				} else {
//					System.out.println(femaleName[j] + " " + familyName[i]);
//				}
//			}
//		}
//		System.out.println("==========================================");
		for (int j = 0; j < maleNames.length; j++) {
			for (int i = 0; i < familyName.length; i++) {
				if (maleNames[j].equals(familyName[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+maleNames[j]+" "+familyName[i]);
				} else {
					System.out.println(maleNames[j] + " " + familyName[i]);
				}
			}
		}
		System.out.println("==========================================");
		for (int j = 0; j < femaleName.length; j++) {
			for (int i = 0; i < familyName.length; i++) {
				if (femaleName[j].equals(familyName[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+femaleName[j]+" "+familyName[i]);
				} else {
					System.out.println(femaleName[j] + " " + familyName[i]);
				}
			}
		}
	}

	public static void main(String[] args) {
		new CreateAmericanName();
	}

}
