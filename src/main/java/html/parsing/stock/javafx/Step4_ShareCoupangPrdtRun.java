package html.parsing.stock.javafx;

public class Step4_ShareCoupangPrdtRun{

	String strUserId = "";
	String strNidAut = "";
	String strNidSes = "";

	public Step4_ShareCoupangPrdtRun() {
//		execute();
	}

	public Step4_ShareCoupangPrdtRun(String strNidAut, String strNidSes) {
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
	}
	
	public Step4_ShareCoupangPrdtRun(String strUserId, String strNidAut, String strNidSes) {
		this.strUserId = strUserId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		System.out.println("strUserId:" + strUserId);
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
	}
	public void execute() {
		start();
	}

	public void start() {
		new ShareCoupangPrdtThread(strUserId, strNidAut, strNidSes).start();
	}

}
