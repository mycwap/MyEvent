package ie.ucc.mid;

public class GetImageName {
public static String getName(String o_name){
	o_name=o_name.replaceAll("0", "zero");
	o_name=o_name.replaceAll("1", "one");
	o_name=o_name.replaceAll("3", "three");
	o_name=o_name.replaceAll("2", "two");
	o_name=o_name.replaceAll("9", "nine");
	o_name=o_name.replaceAll("5", "five");
	o_name=o_name.replaceAll("4", "four");
	return o_name;
	
}
}
